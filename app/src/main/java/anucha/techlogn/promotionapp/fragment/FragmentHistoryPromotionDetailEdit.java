package anucha.techlogn.promotionapp.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.view.KeyEvent;
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

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.Scanner;

import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.GetIPAPI;
import anucha.techlogn.promotionapp.R;

public class FragmentHistoryPromotionDetailEdit extends Fragment {

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private ProgressDialog dialog;
    ArrayList items;
    private GetIPAPI getIPAPI;
    private DataUserLogin users;
    private RadioButton rdbTime1,rdbTime2;
    private ImageView img_date_start,img_date_end,img_time_start,img_time_end;
    private EditText edt_date_start,edt_date_end,edt_time_start,edt_time_end;
    private Button btn_back,btn_next;
    private RelativeLayout group1,group2;
    private ScrollView score;
    String get_dates="";

    private String sName,sDateStart,sDateEnd,sTimeStart,sTimeEnd;
    private ArrayList<String>arrProductID,arrCateID,arrServiceType, arrCustomerID,arrBranchGroupID,arrBranchID;
    String conditionType;
    String conditionAmount;
    String conditionPrice;
    String discountType;
    String discountProductNo;
    String discountPrice;
    String discountRate;
    String discountPriceType;
    String ServiceType,Category;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_3_layout_detail_edit, mContainer, false);

        items=new ArrayList<>();
        getIPAPI=new GetIPAPI();
        users=new DataUserLogin(getActivity());

        initView(myView);
        isTime();
        //setDetail();
        loadData();

        myView.setFocusableInTouchMode(true);
        myView.requestFocus();
        myView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        //Toast.makeText(getActivity(),"กรุณากดปุ่มตกลง",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle_get=getArguments();
                Bundle bundle_put = new Bundle();
                bundle_put.putInt("PromotionID",bundle_get.getInt("PromotionID"));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentHistoryPromotionDetail fragmentHistoryPromotionDetail=new FragmentHistoryPromotionDetail();
                fragmentHistoryPromotionDetail.setArguments(bundle_put);
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, fragmentHistoryPromotionDetail).commit();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custon_alert_dialog);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                TextView des = dialog.findViewById(R.id.tv_description);
                title.setText("ยืนยัน");
                des.setText("ต้องการบันทึกการใช้งานโปรโมชั่นนี้อีกครัง?");
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                //declineButton.setVisibility(View.GONE);
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(checkEmpty()==true){
                            Snackbar.make(myView,"กรุณากรอกข้อมูลให้ครบ",Snackbar.LENGTH_SHORT).show();
                        }else {
                            putData();
                        }
                    }
                });
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        img_date_start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setDialog("กำหนดวันที่เริ่มใช้งานโปรโมชั่น","start");
            }
        });
        img_date_end.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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

        edt_date_start.setEnabled(true);
        edt_date_end.setEnabled(true);
        img_time_start.setEnabled(true);
        edt_time_end.setEnabled(true);

        return myView;
    }
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


        arrProductID=new ArrayList<>();
        arrCateID=new ArrayList<>();
        arrServiceType=new ArrayList<>();
        arrCustomerID=new ArrayList<>();
        arrBranchGroupID=new ArrayList<>();
        arrBranchID=new ArrayList<>();
    }
    private void setDetail(){
        Bundle bundle=getArguments();
        if(bundle!=null) {
            edt_date_start.setText(bundle.getString("DateStart"));
            edt_date_end.setText(bundle.getString("DateEnd"));
            if(bundle.getString("TimeStart").equals("ไม่ได้กำหนด")){

            }else{
                rdbTime1.setChecked(true);
                edt_time_start.setText(bundle.getString("TimeStart"));
                edt_time_end.setText(bundle.getString("TimeEnd"));
            }

        }
    }
    private void loadData(){
        Bundle bundle = getArguments();
        if(bundle!=null) {
            String url = getIPAPI.IPAddress + "/manage/data.php?PromotionID="+bundle.getInt("PromotionID");
            new MyAsyncTask().execute("get",url);
        }
    }
    private void putData(){
        JsonArray jsonArray_promo=new JsonArray();
        JsonObject jsonObject_promo=new JsonObject();
        String url_promo;
        sTimeStart=edt_time_start.getText().toString();
        sDateEnd=edt_time_end.getText().toString();
        if(!sTimeStart.isEmpty()&&!sTimeEnd.isEmpty()&&rdbTime1.isChecked()==true) {
            jsonObject_promo.addProperty("Proname", sName);
            jsonObject_promo.addProperty("IsActive", 1);
            jsonObject_promo.addProperty("StartDate", edt_date_start.getText().toString());
            jsonObject_promo.addProperty("StopDate", edt_date_end.getText().toString());
            jsonObject_promo.addProperty("TimeStart", edt_time_start.getText().toString());
            jsonObject_promo.addProperty("TimeEnd", edt_time_end.getText().toString());
            jsonObject_promo.addProperty("User", users.ID);
            jsonArray_promo.add(jsonObject_promo);

            url_promo= getIPAPI.IPAddress+"/add1/AddPro.php";
        }else{
            jsonObject_promo.addProperty("Proname", sName);
            jsonObject_promo.addProperty("IsActive", 2);
            jsonObject_promo.addProperty("StartDate", edt_date_start.getText().toString());
            jsonObject_promo.addProperty("StopDate", edt_date_end.getText().toString());
            jsonObject_promo.addProperty("User", users.ID);
            jsonArray_promo.add(jsonObject_promo);

            url_promo= getIPAPI.IPAddress+"/add1/AddPro1.php";
        }
        String url_addDetail=getIPAPI.IPAddress + "/add1/AddPromotion.php";
        String url_addCust=getIPAPI.IPAddress + "/add1/AddCustomer.php";
        String url_addBranch=getIPAPI.IPAddress + "/add1/AddBranch.php";

        final JsonArray jsonArray_addDetail=new JsonArray();
        final JsonArray jsonArray_addCust=new JsonArray();
        final JsonArray jsonArray_addBranch=new JsonArray();

        System.out.println(discountType);
        System.out.println(discountProductNo);
        System.out.println(discountPriceType);
        System.out.println(discountPrice);
        System.out.println(discountRate);

        if (arrProductID.size()==0&& arrCateID.size()==0) {
            System.out.println("case1");
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

        } else if (arrProductID.size()==0&& arrCateID.size()!=0) {
            System.out.println("case2");
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

        } else if (!arrProductID.get(0).equals("เลือกทั้งหมด")&&arrProductID.size()>0) {
            System.out.println("case3");
            for (int i = 0; i < arrProductID.size(); i++) {
                String Pro = arrProductID.get(i);
                String Serv = "" + arrServiceType.get(i);
                String Cate = "" + arrCateID.get(i);

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

        if (arrCustomerID.size() == 0) {
            JsonObject jsonObject=new JsonObject();
            jsonObject.addProperty("CustomerID", "NULL");
            jsonObject.addProperty("MemberTypeID","NULL");
            jsonObject.addProperty("TelephoneNo","NULL");
            jsonArray_addCust.add(jsonObject);

        } else {
            for (int i = 0; i < arrCustomerID.size(); i++) {
                String Tel = "NULL";
                String ID = arrCustomerID.get(i);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("CustomerID", ID);
                jsonObject.addProperty("MemberTypeID","1");
                jsonObject.addProperty("TelephoneNo",Tel);
                jsonArray_addCust.add(jsonObject);
            }
        }
        if (arrBranchID.size() == 0) {
            //branchAll
            for (int i = 0; i < arrBranchGroupID.size(); i++) {
                String IDBranch = "NULL";
                String IDGROUP = arrBranchGroupID.get(i);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("BranchID", IDBranch);
                jsonObject.addProperty("GroupID",IDGROUP);
                jsonArray_addBranch.add(jsonObject);
            }
        } if(arrBranchID.size() > 0){
            for (int i = 0; i < arrBranchID.size(); i++) {
                String IDBranch = arrBranchID.get(i);
                String IDGROUP = arrBranchGroupID.get(i);
                JsonObject jsonObject=new JsonObject();
                jsonObject.addProperty("BranchID", IDBranch);
                jsonObject.addProperty("GroupID",IDGROUP);
                jsonArray_addBranch.add(jsonObject);
            }
        }

        new MyAsyncTask().execute("put",url_promo,url_addDetail,url_addCust,url_addBranch,
                String.valueOf(jsonArray_promo),String.valueOf(jsonArray_addDetail),String.valueOf(jsonArray_addCust),
                String.valueOf(jsonArray_addBranch));
    }
    private boolean checkEmpty(){
        if(rdbTime2.isChecked()==true){
            if(edt_date_start.getText().toString().isEmpty()||edt_date_end.getText().toString().isEmpty()){
                return true;
            }else{
                return false;
            }
        }else if(rdbTime1.isChecked()==true){
            if(edt_date_start.getText().toString().isEmpty()||edt_date_end.getText().toString().isEmpty()||
                    edt_time_start.getText().toString().isEmpty()||edt_time_end.getText().toString().isEmpty()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
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
    private void setDateStart(String datestart){
        edt_date_start.setText(datestart);
    }
    private void setDateEnd(String dateend){
        edt_date_end.setText(dateend);
    }
    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setIcon(R.mipmap.loading);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("กำลังตรวจสอบข้อมูล");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(strings[0].equals("get")) {
                String response = "";
                String output = strings[0];
                try {
                    URL url = new URL(strings[1]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    //httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    Scanner scanner = new Scanner(inputStream, "UTF-8");
                    response = scanner.useDelimiter("\\A").next();

                } catch (Exception ex) {
                    System.out.println("Error1");
                }
                try {
                    output = "get";
                    String branch = "";
                    String lastBranch = "";
                    String str = "";
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        JSONArray jsonArray_pro1 = new JSONArray(jsonObj.getString("ProName"));
                        JSONArray jsonArray_pro2 = new JSONArray(jsonObj.getString("ProDetail"));
                        JSONArray jsonArray_pro3 = new JSONArray(jsonObj.getString("Cust"));
                        JSONArray jsonArray_pro4 = new JSONArray(jsonObj.getString("Branch"));
                        for (int j = 0; j < jsonArray_pro1.length(); j++) {
                            JSONObject jsonObj_pro = jsonArray_pro1.getJSONObject(j);
                            sName = jsonObj_pro.getString("PromotionName");

                            JSONObject jsonObject1 = new JSONObject(jsonObj_pro.getString("EffectiveDate"));
                            JSONObject jsonObject2 = new JSONObject(jsonObj_pro.getString("ExpirationDate"));
                            String[] splits = jsonObject1.getString("date").split(" ");
                            String[] splits1 = jsonObject2.getString("date").split(" ");
                            sDateStart = splits[0];
                            sDateEnd = splits1[0];
                            if (!jsonObj_pro.getString("TimeStart").equals("null") && !jsonObj_pro.getString("TimeEnd").equals("null")) {
                                JSONObject jsonObject3 = new JSONObject(jsonObj_pro.getString("TimeStart"));
                                JSONObject jsonObject4 = new JSONObject(jsonObj_pro.getString("TimeEnd"));
                                String[] splits2 = jsonObject3.getString("date").split(" ");
                                String[] splits3 = jsonObject4.getString("date").split(" ");
                                sTimeStart = splits2[1];
                                sTimeEnd = splits3[1];
                            } else {
                                sTimeStart = "ไม่ได้กำหนด";
                                sTimeEnd = "ไม่ได้กำหนด";
                            }

                        }
                        for (int j = 0; j < jsonArray_pro2.length(); j++) {
                            JSONObject jsonObj_pro = jsonArray_pro2.getJSONObject(j);

                            if (!jsonObj_pro.getString("ServiceType").equals("null")) {
                                ServiceType = jsonObj_pro.getString("ServiceType");
                            } else {
                                ServiceType = "NULL";
                            }
                            if (!jsonObj_pro.getString("CategoryID").equals("null")) {
                                Category = jsonObj_pro.getString("CategoryID");
                            } else {
                                Category = "NULL";
                            }


                            if (!jsonObj_pro.getString("CategoryNameTH").equals("null")) {
                                arrCateID.add(jsonObj_pro.getString("CategoryID"));
                            }
                            if (!jsonObj_pro.getString("ProductNameTH").equals("null")) {
                                arrProductID.add(jsonObj_pro.getString("ProductID"));
                            }
                            if (!jsonObj_pro.getString("ServiceNameTH").equals("null")) {
                                arrServiceType.add(jsonObj_pro.getString("ServiceType"));
                            }
                            //conditon
                            if (jsonObj_pro.getString("ConditionType").equals("" + 1)) {
                                conditionType = jsonObj_pro.getString("ConditionType");
                                conditionAmount = jsonObj_pro.getString("ConditionAmount");
                                conditionPrice = "NULL";
                            } else if (jsonObj_pro.getString("ConditionType").equals("" + 2)) {
                                conditionType = jsonObj_pro.getString("ConditionType");
                                conditionPrice = jsonObj_pro.getString("ConditionPrice");
                                conditionAmount = "NULL";

                                conditionType = jsonObj_pro.getString("ConditionType");
                            } else {
                                conditionType = "NULL";
                                conditionPrice = "NULL";
                                conditionAmount = "NULL";

                            }
                            //discount type
                            if (jsonObj_pro.getString("DiscountType").equals("" + 1)) {
                                discountType = jsonObj_pro.getString("DiscountType");
                                discountProductNo="NULL";

                            } else if (jsonObj_pro.getString("DiscountType").equals("" + 2)) {
                                discountType = jsonObj_pro.getString("DiscountType");
                                discountProductNo=jsonObj_pro.getString("DiscountProductNo");

                            }
                            //discount
                            if (jsonObj_pro.getString("DiscountPriceType").equals("" + 1)) {
                                discountPriceType = jsonObj_pro.getString("DiscountPriceType");
                                discountPrice = jsonObj_pro.getString("DiscountPrice");
                                discountRate = "NULL";

                            } else if (jsonObj_pro.getString("DiscountPriceType").equals("" + 2)) {
                                discountPriceType = jsonObj_pro.getString("DiscountPriceType");
                                discountRate = jsonObj_pro.getString("DiscountRate");
                                discountPrice = "NULL";

                            }
                        }
                        for (int j = 0; j < jsonArray_pro3.length(); j++) {
                            JSONObject jsonObj_pro = jsonArray_pro3.getJSONObject(j);
                            if (!jsonObj_pro.getString("CustomerName").equals("null")) {
                                arrCustomerID.add(jsonObj_pro.getString("CustomerID"));
                            }
                        }
                        for (int j = 0; j < jsonArray_pro4.length(); j++) {
                            JSONObject jsonObj_pro = jsonArray_pro4.getJSONObject(j);
                            if (!jsonObj_pro.getString("BranchNameTH").equals("null")) {
                                arrBranchID.add(jsonObj_pro.getString("BranchID"));
                            }
                            arrBranchGroupID.add(jsonObj_pro.getString("BranchGroupID"));
                        }
                    }
                } catch (Exception ex) {
                    //output = response;
                    System.out.println("Error21 : " + ex);
                }
                return output;
            }else{
                String response1 = "";
                String response2 = "";
                String response3 = "";
                String response4 = "";

                String output = strings[0];

                String str_url1 = strings[1];
                String str_url2 = strings[2];
                String str_url3 = strings[3];
                String str_url4 = strings[4];

                String JsonDATA1 = strings[5];
                String JsonDATA2 = strings[6];
                String JsonDATA3 = strings[7];
                String JsonDATA4 = strings[8];

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
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(s.equals("put")) {
                Snackbar.make(myView, "บันทึกรายการแล้ว", Snackbar.LENGTH_SHORT).show();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentIsPromotion()).commit();
            }

        }
    }
}

