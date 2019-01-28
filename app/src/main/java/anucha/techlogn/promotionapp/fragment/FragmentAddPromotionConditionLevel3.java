package anucha.techlogn.promotionapp.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.GetIPAPI;
import anucha.techlogn.promotionapp.R;
import anucha.techlogn.promotionapp.SQLiteHelper;

public class FragmentAddPromotionConditionLevel3 extends Fragment{

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private Button btn_back,btn_next;
    private RelativeLayout group1,group2;
    private ScrollView score;
    private SharedPreferences sp_date,sp_time,sp_pro_name,sp_service,sp_cate,sp_product,sp_condition,sp_branch,sp_cust,
            sp_productAll,sp_branch_all,sp_cust_all,sp_standard;
    private RadioButton rdbTime1,rdbTime2;
    private ImageView img_date_start,img_date_end,img_time_start,img_time_end;
    private EditText edt_date_start,edt_date_end,edt_time_start,edt_time_end;
    String get_dates="";
    private DataUserLogin users;
    private GetIPAPI getIPAPI;
    private ArrayList<String>BranchG;
    ProgressDialog dialog;
    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_1_condition_level3, mContainer, false);
        users=new DataUserLogin(getActivity());
        getIPAPI=new GetIPAPI();
        BranchG=new ArrayList<>();
        mSQLite=SQLiteHelper.getInstance(getActivity());

        initView(myView);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to level0
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel2()).commit();
            }
        });
        final String[] select_qualification = {
                "--เลือกลูกค้า--", "10th / Below", "12th", "Diploma", "UG",
                "PG", "Phd"};
        ArrayList<String> arr=new ArrayList<>();
        for(int i=0;i<select_qualification.length;i++){
            arr.add(select_qualification[i]);
        }

        initSaveData();
        isTime();
        getDataPage();


        img_date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("กำหนดวันที่เริ่มใช้งานโปรโมชั่น","start");
            }
        });
        img_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("กำหนดวันที่สิ้นสุดใช้งานโปรโมชั่น","end");
            }
        });

        img_time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog Tp = new TimePickerDialog(getContext(),R.style.abirStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //mTimetext.setText(hourOfDay+ ":" +minute);
                        String hh,mm;
                        if(String.valueOf(hourOfDay).length()==1){
                            hh="0"+hourOfDay;
                        }else{
                            hh=""+hourOfDay;
                        }
                        if(String.valueOf(minute).length()==1){
                            mm="0"+minute;
                        }else{
                            mm=""+minute;
                        }
                        edt_time_start.setText(hh+ ":" +mm);

                    }
                },1,0,true);
                Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Tp.show();
            }
        });
        img_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog Tp = new TimePickerDialog(getContext(),R.style.abirStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //mTimetext.setText(hourOfDay+ ":" +minute);
                        String hh,mm;
                        if(String.valueOf(hourOfDay).length()==1){
                            hh="0"+hourOfDay;
                        }else{
                            hh=""+hourOfDay;
                        }
                        if(String.valueOf(minute).length()==1){
                            mm="0"+minute;
                        }else{
                            mm=""+minute;
                        }
                        edt_time_end.setText(hh+ ":" +mm);
                    }
                },1,0,true);
                Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = Tp.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                Tp.show();
            }
        });

        edt_date_start.setEnabled(false);
        edt_date_end.setEnabled(false);
        img_time_start.setEnabled(false);
        edt_time_end.setEnabled(false);

        return myView;
    }
    //init sharepref
    private void initSaveData(){
        sp_pro_name = getContext().getSharedPreferences("PromotionName", Context.MODE_PRIVATE);
        sp_service = getContext().getSharedPreferences("Service", Context.MODE_PRIVATE);
        sp_cate = getContext().getSharedPreferences("Category", Context.MODE_PRIVATE);
        sp_product = getContext().getSharedPreferences("Product", Context.MODE_PRIVATE);
        sp_condition = getContext().getSharedPreferences("DataCondition1", Context.MODE_PRIVATE);
        sp_branch = getContext().getSharedPreferences("Branch", Context.MODE_PRIVATE);
        sp_cust = getContext().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        sp_date = getContext().getSharedPreferences("Date", Context.MODE_PRIVATE);
        sp_time = getContext().getSharedPreferences("Time", Context.MODE_PRIVATE);
        sp_productAll = getContext().getSharedPreferences("ProductAll", Context.MODE_PRIVATE);
        sp_branch_all = getContext().getSharedPreferences("BranchAll", Context.MODE_PRIVATE);
        sp_cust_all = getContext().getSharedPreferences("CustomerAll", Context.MODE_PRIVATE);
        sp_standard = getContext().getSharedPreferences("Standard", Context.MODE_PRIVATE);
    }
    //load data
    private void getDataPage(){
        String date_start=sp_date.getString("DateStart","");
        String date_end=sp_date.getString("DateEnd","");
        boolean checked=sp_date.getBoolean("IsCheck",false);
        String time_start=sp_date.getString("TimeStart","");
        String time_end=sp_date.getString("TimeEnd","");

        //System.out.println(date_start+" : "+date_end);
        edt_date_start.setText(date_start);
        edt_date_end.setText(date_end);
        rdbTime1.setChecked(checked);
        edt_time_start.setText(time_start);
        edt_time_end.setText(time_end);
        if(checked==false){
            group1.setVisibility(View.GONE);
            group2.setVisibility(View.GONE);
        }else{
            group1.setVisibility(View.VISIBLE);
            group2.setVisibility(View.VISIBLE);
        }
    }

    private void setDataPage(){
        if(checkEmpty()==true){
            Snackbar.make(myView,"กรุณากรอกข้อมูลให้ครบก่อน",Snackbar.LENGTH_SHORT).show();
        }else {
            //date
            SharedPreferences.Editor editor_date = sp_date.edit();
            editor_date.putString("DateStart", edt_date_start.getText().toString());
            editor_date.putString("DateEnd", edt_date_end.getText().toString());
            editor_date.apply();
            //time
            SharedPreferences.Editor editor_time = sp_date.edit();
            editor_time.putString("TimeStart", edt_time_start.getText().toString());
            editor_time.putString("TimeEnd", edt_time_end.getText().toString());
            editor_time.putBoolean("IsCheck", rdbTime1.isChecked());
            editor_time.apply();
        }
    }
    //set dialog
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDialog(String sTitle, final String isState){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_calendar);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView title = dialog.findViewById(R.id.tv_quit_learning);
        final CalendarView calendar = dialog.findViewById(R.id.calendarView);
        //System.out.println(System.currentTimeMillis() - 1000);
        if(isState.equals("start")) {
            calendar.setDate(Calendar.getInstance().getTimeInMillis(), false, true);
            calendar.setMinDate(System.currentTimeMillis() - 1000);
        }else if(isState.equals("end")){
            //calendar.setDate(Calendar.getInstance().getTimeInMillis(), false, true);
            try {
                String dateString = edt_date_start.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(dateString);

                long startDate = date.getTime();
                calendar.setDate(startDate, false, true);
                calendar.setMinDate(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String mm,dd;
                int m=month+1;
                if(String.valueOf(""+month).length()==1){
                    mm="0"+m;
                }else{
                    mm=""+m;
                }
                if(String.valueOf(""+dayOfMonth).length()==1){
                    dd="0"+dayOfMonth;
                }else{
                    dd=""+dayOfMonth;
                }
                get_dates=year + "-" + mm + "-" + dd;

                dialog.dismiss();
                if(isState.equals("start")) {
                    setDateStart(get_dates);
                }else if(isState.equals("end")){
                    setDateEnd(get_dates);
                }
            }
        });
        title.setText(sTitle);
        Button declineButton = dialog.findViewById(R.id.btn_cancel);
        declineButton.setVisibility(View.GONE);
        Button okButton = dialog.findViewById(R.id.btn_ok);
        okButton.setVisibility(View.GONE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(isState.equals("start")) {
                    setDateStart(get_dates);
                }else if(isState.equals("end")){
                    setDateEnd(get_dates);
                }
            }
        });

    }
    /*private void checkStandrdBoard(){
        boolean isStandard = sp_standard.getBoolean("keys",false);
        if(isStandard==true){
            String promoID=sp_standard.getString("PromotionID","");
            mDb = mSQLite.getReadableDatabase();
            String sql_service = "sEleCt p.PromotionName,pd.ServiceType,pd.CategoryID,pd.ProductID,pd.ConditionType,pd.ConditionAmount,pd.ConditionPrice,pmd.DiscountType,pmd.DiscountPriceType,pmd.DiscountRate,pmd.DiscountPrice,pmd.DiscountProductNo,pdt.ProductNameTH,pdt.ProductNameEN,pdt.ProductPrice fRoM tb_promotion p left join ((tb_promotionDetail pd left join tb_promotionDiscount pmd on pd.PromotionDetailID=pmd.PromotionDetailID) left join tb_product pdt on pd.ProductID=pdt.ProductID) on p.PromotionID=pd.PromotionID wHeRe p.PromotionID='"+promoID+"'";
            Cursor cursor = mDb.rawQuery(sql_service, null);

            //page1
            String promoName="";
            String serviceType="";
            String categoryID="";
            String productID="";
            //page2
            String conditionType="";
            String conditionAmount="";
            String conditionPrice="";
            String discountType="";
            String discountProductNo="";
            String discountPrice="";
            String discountRate="";
            String discountPriceType="";
            //page 1 product
            ArrayList<String>arr_pro_id=new ArrayList<>();
            ArrayList<String>arr_pro_name_th=new ArrayList<>();
            ArrayList<String>arr_pro_name_en=new ArrayList<>();
            ArrayList<String>arr_pro_price=new ArrayList<>();

            int position=0;
            while (cursor.moveToNext()) {
                promoName=cursor.getString(0);
                serviceType=cursor.getString(1);
                categoryID=cursor.getString(2);
                productID=cursor.getString(3);
                conditionType=cursor.getString(4);
                conditionAmount=cursor.getString(5);
                conditionPrice=cursor.getString(6);
                discountType=cursor.getString(7);
                discountPriceType=cursor.getString(8);
                discountRate=cursor.getString(9);
                discountPrice=cursor.getString(10);
                discountProductNo=cursor.getString(11);

                if(!cursor.getString(3).isEmpty()){
                    arr_pro_id.add(productID);
                    arr_pro_name_th.add(cursor.getString(12));
                    arr_pro_name_en.add(cursor.getString(13));
                    arr_pro_price.add(cursor.getString(14));
                }else{
                    position=1;
                }
            }
            *//*for(int i=0;i<arr_pro_id.size();i++){
                System.out.println(arr_pro_id.get(i)+","+arr_pro_name_th.get(i)+","+arr_pro_name_en.get(i)+","+arr_pro_price.get(i));
            }*//*


            //name
            SharedPreferences.Editor editor_pro_name = sp_pro_name.edit();
            editor_pro_name.putString("Name", promoName);
            editor_pro_name.apply();
            //service
            SharedPreferences.Editor editor_service = sp_service.edit();
            int ser = Integer.parseInt(serviceType);
            editor_service.putInt("ServiceType", ser);
            editor_service.putInt("Position", ser);
            editor_service.apply();
            //category
            SharedPreferences.Editor editor_cate = sp_cate.edit();
            int id_cate;
            int posi=0;
            if(categoryID.isEmpty()){
                id_cate =0;
                posi=1;
            }else{
                id_cate=Integer.parseInt(categoryID);
            }
            editor_cate.putInt("CategoryID", id_cate);
            editor_service.putInt("Position", posi);
            editor_cate.apply();
            //System.out.println(promoName+","+serviceType+","+categoryID+","+productID);

            SharedPreferences.Editor editor = sp_condition.edit();
            editor.putString("ConditionType",conditionType);
            editor.putString("ConditionAmount",conditionAmount);
            editor.putString("ConditionPrice",conditionPrice);
            editor.putString("DiscountType",discountType);
            editor.putString("DiscountProductNo",discountProductNo);
            editor.putString("DiscountPrice",discountPrice);
            editor.putString("DiscountRate",discountRate);
            editor.putString("DiscountPriceType",discountPriceType);
            editor.apply();

            //data product
            sp_product.edit().clear().apply();
            SharedPreferences.Editor editor_product = sp_product.edit();
            HashSet<String> mSet1;
            for (int i = 0; i < arr_pro_id.size(); i++) {
                mSet1 = new HashSet<>();
                mSet1.add("<a>" + arr_pro_id.get(i));
                mSet1.add("<b>" + arr_pro_name_th.get(i));
                mSet1.add("<c>" + arr_pro_name_en.get(i));
                mSet1.add("<d>" + arr_pro_price.get(i));
                mSet1.add("<e>" + serviceType);
                mSet1.add("<f>" + categoryID);
                editor_product.putStringSet(arr_pro_id.get(i), mSet1);
                editor_product.apply();
            }

            SharedPreferences.Editor editor_pro_all = sp_productAll.edit();
            editor_pro_all.putInt("Position", position);
            editor_pro_all.apply();


            System.out.println(sp_product.getAll().size());
        }
    }*/
    private void setDateStart(String datestart){
        edt_date_start.setText(datestart);
    }
    private void setDateEnd(String dateend){
        edt_date_end.setText(dateend);
    }
    //init view
    private void initView(View view){

        btn_back=view.findViewById(R.id.btn_back_level3);
        btn_next=view.findViewById(R.id.btn_save_level3);
        score=view.findViewById(R.id.score_condi_level2);
        group1=view.findViewById(R.id.group_time_start);
        group2=view.findViewById(R.id.group_time_end);
        rdbTime1=view.findViewById(R.id.rdbTime1);
        rdbTime2=view.findViewById(R.id.rdbTime2);

        img_date_start=view.findViewById(R.id.img_date_start);
        img_date_end=view.findViewById(R.id.img_date_end);
        img_time_start=view.findViewById(R.id.img_time_start);
        img_time_end=view.findViewById(R.id.img_time_end);

        edt_date_start=view.findViewById(R.id.edt_date_start);
        edt_date_end=view.findViewById(R.id.edt_date_end);
        edt_time_start=view.findViewById(R.id.edt_time_start);
        edt_time_end=view.findViewById(R.id.edt_time_end);

        rdbTime2.setChecked(true);
        group1.setVisibility(View.GONE);
        group2.setVisibility(View.GONE);

        int height=getActivity().getResources().getDisplayMetrics().heightPixels;
        score.getLayoutParams().height=(height*70)/100;


    }
    private void isTime(){
        rdbTime2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group1.setVisibility(View.VISIBLE);
                group2.setVisibility(View.VISIBLE);
            }
        });
        rdbTime1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group1.setVisibility(View.GONE);
                group2.setVisibility(View.GONE);
            }
        });
    }

    //check empty text
    private boolean checkEmpty() {
        if (edt_date_start.getText().toString().isEmpty() || edt_date_end.getText().toString().isEmpty()||
                (rdbTime1.isChecked()==true&&(edt_time_start.getText().toString().isEmpty()||edt_time_end.getText().toString().isEmpty()))) {
            return true;
        }else {
            return false;
        }
    }
    private void AddPromo(ArrayList<String>arrBranchGroup) {

        int Category = sp_cate.getInt("CategoryID", -1);
        int Product = sp_productAll.getInt("Position", -1);
        int ServiceType = sp_service.getInt("ServiceType", -1);

        //Page1
        String promotionName=sp_pro_name.getString("Name","");
        String date_start=sp_date.getString("DateStart","");
        String date_end=sp_date.getString("DateEnd","");
        boolean checked=sp_date.getBoolean("IsCheck",false);
        String time_start,time_end;
        if(sp_date.getString("TimeStart","").isEmpty()){
            time_start="NULL";
        }else{
            time_start=sp_date.getString("TimeStart","");
        }
        if(sp_date.getString("TimeEnd","").isEmpty()){
            time_end="NULL";
        }else{
            time_end=sp_date.getString("TimeEnd","");
        }
        String IsActive=""+1;

        JsonArray jsonArray_promo=new JsonArray();
        JsonObject jsonObject_promo=new JsonObject();
        String url_promo;
        if(checked==true) {
            jsonObject_promo.addProperty("Proname", promotionName);
            jsonObject_promo.addProperty("IsActive", IsActive);
            jsonObject_promo.addProperty("StartDate", date_start);
            jsonObject_promo.addProperty("StopDate", date_end);
            jsonObject_promo.addProperty("TimeStart", time_start);
            jsonObject_promo.addProperty("TimeEnd", time_end);
            jsonObject_promo.addProperty("User", users.ID);
            jsonArray_promo.add(jsonObject_promo);

            url_promo= getIPAPI.IPAddress+"/add1/AddPro.php";
        }else{
            jsonObject_promo.addProperty("Proname", promotionName);
            jsonObject_promo.addProperty("IsActive", IsActive);
            jsonObject_promo.addProperty("StartDate", date_start);
            jsonObject_promo.addProperty("StopDate", date_end);
            jsonObject_promo.addProperty("User", users.ID);
            jsonArray_promo.add(jsonObject_promo);

            url_promo= getIPAPI.IPAddress+"/add1/AddPro1.php";
        }

        //page2
        String conditionType;
        String conditionAmount;
        String conditionPrice;
        String discountType;
        String discountProductNo;
        String discountPrice;
        String discountRate;
        String discountPriceType;
        if (sp_condition.getString("ConditionType", "").isEmpty()) {
            conditionType = "NULL";
        } else {
            conditionType = sp_condition.getString("ConditionType", "");
        }
        if (sp_condition.getString("ConditionAmount", "").isEmpty()) {
            conditionAmount = "NULL";
        } else {
            conditionAmount = sp_condition.getString("ConditionAmount", "");
        }
        if (sp_condition.getString("ConditionPrice", "").isEmpty()) {
            conditionPrice = "NULL";
        } else {
            conditionPrice = sp_condition.getString("ConditionPrice", "");
        }
        if (sp_condition.getString("DiscountType", "").isEmpty()) {
            discountType = "NULL";
        } else {
            discountType = sp_condition.getString("DiscountType", "");
        }
        if (sp_condition.getString("DiscountProductNo", "").isEmpty()) {
            discountProductNo = "NULL";
        } else {
            discountProductNo = sp_condition.getString("DiscountProductNo", "");
        }
        if (sp_condition.getString("DiscountPrice", "").isEmpty()) {
            discountPrice = "NULL";
        } else {
            discountPrice = sp_condition.getString("DiscountPrice", "");
        }
        if (sp_condition.getString("DiscountRate", "").isEmpty()) {
            discountRate = "NULL";
        } else {
            discountRate = sp_condition.getString("DiscountRate", "");
        }
        if (sp_condition.getString("DiscountPriceType", "").isEmpty()) {
            discountPriceType = "NULL";
        } else {
            discountPriceType = sp_condition.getString("DiscountPriceType", "");
        }

        boolean customerAll = sp_cust_all.getBoolean("CheckAll", false);
        boolean branchAll = sp_branch_all.getBoolean("CheckAll", false);


        final JsonArray jsonArray_addDetail=new JsonArray();
        final JsonArray jsonArray_addCust=new JsonArray();
        final JsonArray jsonArray_addBranch=new JsonArray();


        String url_addDetail=getIPAPI.IPAddress + "/add1/AddPromotion.php";
        String url_addCust=getIPAPI.IPAddress + "/add1/AddCustomer.php";
        String url_addBranch=getIPAPI.IPAddress + "/add1/AddBranch.php";

        //getProduct
        ArrayList<String> arr_product = new ArrayList<>();
        ArrayList<String> arr_service = new ArrayList<>();
        ArrayList<String> arr_category = new ArrayList<>();
        Map<String, ?> entries2 = sp_product.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        boolean isChecked = false;
        for (String temp : list2) {
            System.out.println(temp+" = "+sp_product.getStringSet(temp,null));
            for (int i = 0; i < sp_product.getStringSet(temp, null).size(); i++) {
                getData2 = sp_product.getStringSet(temp, null).toArray(new String[sp_product.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk = getData2[i].charAt(1);
                if (chk == 'a') {
                    arr_product.add(getData2[i].substring(3));
                }else if (chk == 'e') {
                    arr_service.add(getData2[i].substring(3));
                }else if (chk == 'f') {
                    arr_category.add(getData2[i].substring(3));
                }
            }
        }

        System.out.println(ServiceType+" :: "+Category+" :: "+arr_product.size());
        if (Product == 1&&Category == 0 && arr_product.size()==0) {
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("DisType",discountType);
            jsonObject.addProperty("DisPiece",discountProductNo);
            jsonObject.addProperty("DisPriceType",discountPriceType);
            jsonObject.addProperty("DisPrice",discountPrice);
            jsonObject.addProperty("DisPer",discountRate);
            jsonObject.addProperty("ConType",conditionType);
            jsonObject.addProperty("ConPiece",conditionAmount);
            jsonObject.addProperty("ConPrice",conditionPrice);
            jsonObject.addProperty("ProductID","NULL");
            jsonObject.addProperty("ServiceType",""+ServiceType);
            jsonObject.addProperty("CategoryID","NULL");
            jsonArray_addDetail.add(jsonObject);

        } else if (Product == 1&& Category != 0 && arr_product.size()==0) {
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("DisType",discountType);
            jsonObject.addProperty("DisPiece",discountProductNo);
            jsonObject.addProperty("DisPriceType",discountPriceType);
            jsonObject.addProperty("DisPrice",discountPrice);
            jsonObject.addProperty("DisPer",discountRate);
            jsonObject.addProperty("ConType",conditionType);
            jsonObject.addProperty("ConPiece",conditionAmount);
            jsonObject.addProperty("ConPrice",conditionPrice);
            jsonObject.addProperty("ProductID","NULL");
            jsonObject.addProperty("ServiceType",""+ServiceType);
            jsonObject.addProperty("CategoryID",""+Category);
            jsonArray_addDetail.add(jsonObject);

        } else if (arr_product.size()>0) {
            /*ArrayList<String> arr_product = new ArrayList<>();
            Map<String, ?> entries2 = sp_product.getAll();
            Set<String> keys2 = entries2.keySet();
            String[] getData2;
            List<String> list2 = new ArrayList<String>(keys2);
            boolean isChecked = false;
            for (String temp : list2) {
                System.out.println(temp+" = "+sp_product.getStringSet(temp,null));
                for (int i = 0; i < sp_product.getStringSet(temp, null).size(); i++) {
                    getData2 = sp_product.getStringSet(temp, null).toArray(new String[sp_product.getStringSet(temp, null).size()]);
                    //System.out.println(temp + " : " + getData2[i]);
                    char chk = getData2[i].charAt(1);
                    if (chk == 'a') {
                        //arr_product.add(getData2[i].substring(3));
                    } else if (chk == 'b') {
                        arr_product.add(getData2[i].substring(3));
                    }
                }
            }*/
            for (int i = 0; i < arr_product.size(); i++) {
                String Pro = arr_product.get(i);
                String Serv = "" + arr_service.get(i);
                String Cate = "" + arr_category.get(i);
                System.out.println(Serv+" , "+Cate);

                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("DisType",discountType);
                jsonObject.addProperty("DisPiece",discountProductNo);
                jsonObject.addProperty("DisPriceType",discountPriceType);
                jsonObject.addProperty("DisPrice",discountPrice);
                jsonObject.addProperty("DisPer",discountRate);
                jsonObject.addProperty("ConType",conditionType);
                jsonObject.addProperty("ConPiece",conditionAmount);
                jsonObject.addProperty("ConPrice",conditionPrice);
                jsonObject.addProperty("ProductID",Pro);
                jsonObject.addProperty("ServiceType",Serv);
                jsonObject.addProperty("CategoryID",Cate);
                jsonArray_addDetail.add(jsonObject);
            }

        }
        if (customerAll == true) {
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("CustomerID", "NULL");
            jsonObject.addProperty("MemberTypeID","NULL");
            jsonObject.addProperty("TelephoneNo","NULL");
            jsonArray_addCust.add(jsonObject);

        } else {
            ArrayList<String> arr_cust = new ArrayList<>();
            //ArrayList<String>arr_branch=new ArrayList<>();
            Map<String, ?> entries_cust = sp_cust.getAll();
            Set<String> keys_cust = entries_cust.keySet();
            String[] getData_cust;
            List<String> list_cust = new ArrayList<String>(keys_cust);
            for (String temp : list_cust) {
                //System.out.println(temp+" = "+sp_product.getStringSet(temp,null));
                for (int i = 0; i < sp_cust.getStringSet(temp, null).size(); i++) {
                    getData_cust = sp_cust.getStringSet(temp, null).toArray(new String[sp_cust.getStringSet(temp, null).size()]);
                    //System.out.println(temp + " : " + getData2[i]);
                    char chk = getData_cust[i].charAt(1);
                    if (chk == 'a') {
                        arr_cust.add(getData_cust[i].substring(3));
                    } else if (chk == 'b') {
                        //arr_product.add(getData_cust[i].substring(3));
                    }
                }
            }
            for (int i = 0; i < arr_cust.size(); i++) {
                String Tel = arr_cust.get(i);
                String ID = arr_cust.get(i);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("CustomerID", ID);
                jsonObject.addProperty("MemberTypeID","1");
                jsonObject.addProperty("TelephoneNo",Tel);
                jsonArray_addCust.add(jsonObject);
            }
        }
        System.out.println("branchAll : "+branchAll);
        if (branchAll == true) {
            //branchAll
            for (int i = 0; i < arrBranchGroup.size(); i++) {
                String IDBranch = "NULL";
                String IDGROUP = arrBranchGroup.get(i);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("BranchID", IDBranch);
                jsonObject.addProperty("GroupID",IDGROUP);
                jsonArray_addBranch.add(jsonObject);
            }
        } if(branchAll==false){
            ArrayList<String> arr_branchID = new ArrayList<>();
            ArrayList<String> arr_branchGroup = new ArrayList<>();
            Map<String, ?> entries_cust = sp_branch.getAll();
            Set<String> keys_cust = entries_cust.keySet();
            String[] getData_cust;
            List<String> list_cust = new ArrayList<String>(keys_cust);
            for (String temp : list_cust) {
                arr_branchID.add(temp);
                System.out.println(temp+" = "+sp_branch.getStringSet(temp,null));
                for (int i = 0; i < sp_branch.getStringSet(temp, null).size(); i++) {
                    getData_cust = sp_branch.getStringSet(temp, null).toArray(new String[sp_branch.getStringSet(temp, null).size()]);
                    //System.out.println(temp + " : " + getData2[i]);
                    char chk = getData_cust[i].charAt(1);
                    if (chk == 'a') {

                    } else if (chk == 'd') {
                        arr_branchGroup.add(getData_cust[i].substring(3));
                    }
                }
            }
            System.out.println(arr_branchID.size()+" :: "+arr_branchGroup.size());
            for (int i = 0; i < arr_branchID.size(); i++) {
                String IDBranch = arr_branchID.get(i);
                String IDGROUP = arr_branchGroup.get(i);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("BranchID", IDBranch);
                jsonObject.addProperty("GroupID",IDGROUP);
                jsonArray_addBranch.add(jsonObject);
            }
        }


        new MyAsyncTask().execute(url_promo,url_addDetail,url_addCust,url_addBranch,
                String.valueOf(jsonArray_promo),String.valueOf(jsonArray_addDetail),String.valueOf(jsonArray_addCust),
                String.valueOf(jsonArray_addBranch));
    }
    //clear
    private void clearData(){
        sp_pro_name.edit().clear().apply();
        sp_service.edit().clear().apply();
        sp_cate.edit().clear().apply();
        sp_product.edit().clear().apply();
        sp_condition.edit().clear().apply();
        sp_branch.edit().clear().apply();
        sp_cust.edit().clear().apply();

        sp_date.edit().clear().apply();
        sp_time.edit().clear().apply();
        sp_branch_all.edit().clear().apply();
        sp_cust_all.edit().clear().apply();
        sp_standard.edit().clear().apply();
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setIcon(R.mipmap.ic_launcher);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กำลังตรวจสอบข้อมูล");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            String response1 = "";
            String response2 = "";
            String response3 = "";
            String response4 = "";

            String output = "";

            String str_url1 = strings[0];
            String str_url2 = strings[1];
            String str_url3 = strings[2];
            String str_url4 = strings[3];

            String JsonDATA1 = strings[4];
            String JsonDATA2 = strings[5];
            String JsonDATA3 = strings[6];
            String JsonDATA4 = strings[7];

            HttpURLConnection httpURLConnection1 = null;
            HttpURLConnection httpURLConnection2 = null;
            HttpURLConnection httpURLConnection3 = null;
            HttpURLConnection httpURLConnection4 = null;
            try {
                URL url1 = new URL(str_url1);
                URL url2 = new URL(str_url2);
                URL url3 = new URL(str_url3);
                URL url4 = new URL(str_url4);

                httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                httpURLConnection2 = (HttpURLConnection) url2.openConnection();
                httpURLConnection3 = (HttpURLConnection) url3.openConnection();
                httpURLConnection4 = (HttpURLConnection) url4.openConnection();

                httpURLConnection1.setDoOutput(true);
                httpURLConnection2.setDoOutput(true);
                httpURLConnection3.setDoOutput(true);
                httpURLConnection4.setDoOutput(true);
                //httpURLConnection.connect();
                // is output buffer writter
                httpURLConnection1.setRequestMethod("POST");
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection3.setRequestMethod("POST");
                httpURLConnection4.setRequestMethod("POST");

                httpURLConnection1.setRequestProperty("Content-Type", "application/json");
                httpURLConnection2.setRequestProperty("Content-Type", "application/json");
                httpURLConnection3.setRequestProperty("Content-Type", "application/json");
                httpURLConnection4.setRequestProperty("Content-Type", "application/json");

                httpURLConnection1.setRequestProperty("Accept", "application/json");
                httpURLConnection2.setRequestProperty("Accept", "application/json");
                httpURLConnection3.setRequestProperty("Accept", "application/json");
                httpURLConnection4.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer1 = new BufferedWriter(new OutputStreamWriter(httpURLConnection1.getOutputStream(), "UTF-8"));
                Writer writer2 = new BufferedWriter(new OutputStreamWriter(httpURLConnection2.getOutputStream(), "UTF-8"));
                Writer writer3 = new BufferedWriter(new OutputStreamWriter(httpURLConnection3.getOutputStream(), "UTF-8"));
                Writer writer4 = new BufferedWriter(new OutputStreamWriter(httpURLConnection4.getOutputStream(), "UTF-8"));

                writer1.write(JsonDATA1);
                writer2.write(JsonDATA2);
                writer3.write(JsonDATA3);
                writer4.write(JsonDATA4);
                // json data
                writer1.close();
                writer2.close();
                writer3.close();
                writer4.close();

                InputStream inputStream1 = httpURLConnection1.getInputStream();
                InputStream inputStream2 = httpURLConnection2.getInputStream();
                InputStream inputStream3 = httpURLConnection3.getInputStream();
                InputStream inputStream4 = httpURLConnection4.getInputStream();

                Scanner scanner1 = new Scanner(inputStream1, "UTF-8");
                Scanner scanner2 = new Scanner(inputStream2, "UTF-8");
                Scanner scanner3 = new Scanner(inputStream3, "UTF-8");
                Scanner scanner4 = new Scanner(inputStream4, "UTF-8");

                response1 = scanner1.useDelimiter("\\A").next();
                response2 = scanner2.useDelimiter("\\A").next();
                response3 = scanner3.useDelimiter("\\A").next();
                response4 = scanner4.useDelimiter("\\A").next();

            } catch (Exception ex) {
                System.out.println("Error1 : " + ex.getMessage());
            } finally {
                if (httpURLConnection1 != null) {
                    httpURLConnection1.disconnect();
                }
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                if (httpURLConnection3 != null) {
                    httpURLConnection3.disconnect();
                }
                if (httpURLConnection4 != null) {
                    httpURLConnection4.disconnect();
                }


                try {
                    System.out.println("response1 : "+response1);
                    System.out.println("response2 : "+response2);
                    System.out.println("response3 : "+response3);
                    System.out.println("response4 : "+response4);
                    //output = response;
                } catch (Exception ex) {
                    System.out.println("Error2 : " + ex.getMessage());
                }
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            clearData();

            Snackbar.make(myView,"สร้างโปรโมชั่นสำเร็จ",Snackbar.LENGTH_SHORT).show();

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (false) {
                transaction.addToBackStack(null);
            }
            transaction.replace(R.id.container, new FragmentAddPromotion()).commit();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        //checkStandrdBoard();
        final ArrayList<String> arr_bg = new ArrayList<>();
        boolean branchAll = sp_branch_all.getBoolean("CheckAll", false);
        if(branchAll==true) {
            mDb = mSQLite.getReadableDatabase();
            String sql_bg = "SELECT BranchGroupID FROM tb_branchgroup ";
            Cursor cursor = mDb.rawQuery(sql_bg, null);

            while (cursor.moveToNext()) {
                arr_bg.add(cursor.getString(0));
            }
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataPage();
                if(checkEmpty()==true){
                    //Snackbar.make(myView,"กรุณากรอกข้อมูลให้ครบก่อน",Snackbar.LENGTH_SHORT).show();
                }else {
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.custon_alert_dialog);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    TextView title = dialog.findViewById(R.id.tv_quit_learning);
                    TextView des = dialog.findViewById(R.id.tv_description);
                    title.setText("ยืนยัน");
                    des.setText("ยืนยันการสร้างโปรโมชั่นนี้?");
                    Button declineButton = dialog.findViewById(R.id.btn_cancel);
                    declineButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    //declineButton.setVisibility(View.GONE);
                    Button okButton = dialog.findViewById(R.id.btn_ok);
                    okButton.setOnClickListener(new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            AddPromo(arr_bg);
                        }
                    });
                }
            }
        });
    }
}

