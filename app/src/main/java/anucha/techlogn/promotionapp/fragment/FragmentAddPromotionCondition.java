package anucha.techlogn.promotionapp.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Adapter.MyAdapterSpinner;
import Adapter.MyAdapterSpinnerCategory;
import Adapter.MyAdapterSpinnerService;
import CustomItem.StateVO;
import CustomItem.StateVOCategory;
import CustomItem.StateVOService;
import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.GetIPAPI;
import anucha.techlogn.promotionapp.R;
import anucha.techlogn.promotionapp.SQLiteHelper;
import anucha.techlogn.promotionapp.activities.LoginActivity;
import anucha.techlogn.promotionapp.activities.MainActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FragmentAddPromotionCondition extends Fragment {

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private Spinner spinner_product,spinner_service,spinner_cate;
    private Button btn_next,btn_clear;
    private MyAdapterSpinner myAdapter;
    private MyAdapterSpinnerService myAdapterService;
    private MyAdapterSpinnerCategory myAdapterCategory;
    private EditText edt_proname;
    //private CheckBox chkAllCate,chkAllPro;
    private SharedPreferences sp_pro_name,sp_service,sp_cate,sp_product,sp_condition,sp_cust,sp_branch,
            sp_date,sp_time,sp_productAll,sp_branch_all,sp_cust_all,sp_standard;
    private ArrayList<Integer>productList;
    private RelativeLayout group_pro;
    private ScrollView score;
    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    private TextView textProductItems;
    private String isSelectService,isSelectCate;
    private ArrayList<StateVO> listVOsProduct;
    ArrayList<StateVOService> listVOsService;
    ArrayList<StateVOCategory> listVOsCategory;
    private ProgressDialog dialog;
    private GetIPAPI getIPAPI;
    private DataUserLogin users;
    private boolean IsResponse;
    private String msgResponse;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_1_condition, mContainer, false);
        mSQLite=SQLiteHelper.getInstance(getActivity());
        getIPAPI=new GetIPAPI();
        users=new DataUserLogin(getActivity());

        initView(myView);
        initSaveData();

        spinner_service.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isSelectService = myAdapterService.getItem(position).getTitle();
                String sql_category = "select CategoryNameTH,cate.CategoryID from tb_service ser left join (tb_product pro left join tb_category cate on pro.CategoryID=cate.CategoryID) on ser.ServiceType=pro.ServiceType where ser.ServiceNameTH='" + isSelectService + "' Group By cate.CategoryID,CategoryNameTH";
                Cursor cursor = mDb.rawQuery(sql_category, null);
                ArrayList<String> arr_category = new ArrayList<>();
                ArrayList<Integer> arr_categoryID = new ArrayList<>();
                arr_category.add("--เลือกหมวดหมู่--");
                arr_category.add("เลือกหมวดหมู่ทั้งหมด");
                arr_categoryID.add(-1);
                arr_categoryID.add(0);
                while (cursor.moveToNext()) {
                    arr_category.add(cursor.getString(0));
                    arr_categoryID.add(Integer.parseInt(cursor.getString(1)));
                }

                setSpinnerCategory(arr_category,arr_categoryID, spinner_cate);
                spinner_cate.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_cate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                isSelectCate=myAdapterCategory.getItem(position).getTitle();
                ArrayList<String> arr_product = new ArrayList<>();
                if(position==1) {
                    //name
                    /*SharedPreferences.Editor editor_pro_name = sp_pro_name.edit();
                    editor_pro_name.putString("Name", edt_proname.getText().toString());
                    editor_pro_name.apply();

                    sp_cate.edit().clear().apply();
                    sp_service.edit().clear().apply();

                    Bundle bundle = new Bundle();
                    bundle.putString("CategoryID", myAdapterCategory.getItem(position).getCategoryID());
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    FragmentAddPromotionCondition_1 frag = new FragmentAddPromotionCondition_1();
                    frag.setArguments(bundle);
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, frag).commit();*/


                    arr_product.add("--เลือกสินค้า--");
                    arr_product.add("เลือกสินค้าทั้งหมด");
                }else if(position>1){
                    arr_product.add("--เลือกสินค้า--");
                    arr_product.add("เลือกสินค้าทั้งหมด");
                    arr_product.add("เลือกสินค้าแบบระบุรายการ");
                }
                setSpinnerProduct(arr_product);
                /*String sql_product = "select ProductNameTH,pro.ProductID from tb_service ser left join (tb_product pro left join tb_category cate on pro.CategoryID=cate.CategoryID) on ser.ServiceType=pro.ServiceType where cate.CategoryNameTH='"+isSelectCate+"' Group By pro.ProductID,ProductNameTH";
                Cursor cursor = mDb.rawQuery(sql_product, null);
                ArrayList<String> arr_product = new ArrayList<>();
                arr_product.add("--เลือกสินค้า--");
                arr_product.add("เลือกสินค้าทั้งหมด");
                if (cursor.moveToNext()) {
                    //arr_product.add(cursor.getString(0));

                }*/

                //spinner_product.setEnabled(true);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==2) {
                    sp_productAll.edit().clear().apply();
                    //name
                    SharedPreferences.Editor editor_pro_name = sp_pro_name.edit();
                    editor_pro_name.putString("Name", edt_proname.getText().toString());
                    editor_pro_name.apply();
                    //service
                    SharedPreferences.Editor editor_service = sp_service.edit();
                    int ser = Integer.parseInt( myAdapterService.getItem(spinner_service.getSelectedItemPosition()).getServiceType());
                    //String ser= listVOsService.get(spinner_service.getSelectedItemPosition()).getTitle();
                    editor_service.putInt("ServiceType", ser);
                    editor_service.putInt("Position", spinner_service.getSelectedItemPosition());
                    // editor_service.putString("ServiceNameTH", isSelectService);
                    editor_service.apply();
                    //category
                    SharedPreferences.Editor editor_cate = sp_cate.edit();
                    int id_cate = Integer.parseInt(myAdapterCategory.getItem(spinner_cate.getSelectedItemPosition()).getCategoryID());
                    editor_cate.putInt("CategoryID", id_cate);
                    editor_cate.putInt("Position", spinner_cate.getSelectedItemPosition());
                    //editor_cate.putString("CategoryNameTH", isSelectCate);
                    editor_cate.apply();


                    Bundle bundle = new Bundle();
                    bundle.putString("ServiceType", myAdapterService.getItem(spinner_service.getSelectedItemPosition()).getServiceType());
                    bundle.putString("CategoryID", myAdapterCategory.getItem(spinner_cate.getSelectedItemPosition()).getCategoryID());
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    FragmentAddPromotionCondition_1 frag = new FragmentAddPromotionCondition_1();
                    frag.setArguments(bundle);
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, frag).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return myView;
    }
    //set dialog
    private void setDialog(String sTitle,String sDesc){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custon_alert_dialog);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView title = dialog.findViewById(R.id.tv_quit_learning);
        TextView des = dialog.findViewById(R.id.tv_description);
        title.setText(sTitle);
        des.setText(sDesc);
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
                clearData();
            }
        });

    }
    //init sharepref
    private void initSaveData(){
        sp_pro_name = getContext().getSharedPreferences("PromotionName", Context.MODE_PRIVATE);
        sp_service = getContext().getSharedPreferences("Service", Context.MODE_PRIVATE);
        sp_cate = getContext().getSharedPreferences("Category", Context.MODE_PRIVATE);
        sp_product = getContext().getSharedPreferences("Product", Context.MODE_PRIVATE);
        sp_productAll = getContext().getSharedPreferences("ProductAll", Context.MODE_PRIVATE);
        sp_condition = getContext().getSharedPreferences("DataCondition1", Context.MODE_PRIVATE);
        sp_branch = getContext().getSharedPreferences("Branch", Context.MODE_PRIVATE);
        sp_cust = getContext().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        sp_date = getContext().getSharedPreferences("Date", Context.MODE_PRIVATE);
        sp_time = getContext().getSharedPreferences("Time", Context.MODE_PRIVATE);
        sp_branch_all = getContext().getSharedPreferences("BranchAll", Context.MODE_PRIVATE);
        sp_cust_all = getContext().getSharedPreferences("CustomerAll", Context.MODE_PRIVATE);
        sp_standard = getContext().getSharedPreferences("Standard", Context.MODE_PRIVATE);
    }
    //load data
    private void getDataPage(){
        //System.out.println(sp_service.getInt("ServiceType",-1));
        int id_service=sp_service.getInt("Position",-1);
        int id_cate=sp_cate.getInt("Position",-1);
        int id_proAll=sp_productAll.getInt("Position",-1);
        //System.out.println("Position : "+id_proAll);
        if(sp_productAll.getAll().size()>0){
            spinner_product.setSelection(id_proAll);
        }else {
            spinner_product.setSelection(0);
        }
        edt_proname.setText(sp_pro_name.getString("Name",null));
        spinner_service.setSelection(id_service);
        spinner_cate.setSelection(id_cate);

        //chkAllCate.setChecked(sp_cate.getBoolean("IsChecked",false));
        productList=new ArrayList<>();
        Map<String, ?> entries2 = sp_product.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            //System.out.println(temp+" = "+sp_product.getStringSet(temp,null));
            for (int i = 0; i < sp_product.getStringSet(temp, null).size(); i++) {
                getData2 = sp_product.getStringSet(temp, null).toArray(new String[sp_product.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk = getData2[i].charAt(1);
                if (chk == 'a') {
                    productList.add(Integer.parseInt(getData2[i].substring(3)));
                }else if(chk == 'b'){
                    System.out.println("PID : "+getData2[i].substring(3));
                }
            }
        }

        if(sp_product.getAll().size()>0){
            textProductItems.setVisibility(View.VISIBLE);
        }

    }
    //set data when click button next
    private void setDataPage(){
        if(checkEmpty()==true){
            //Snackbar.make(myView,"กรุณากรอกข้อมูลให้ครบก่อน",Snackbar.LENGTH_SHORT).show();
        }else {
            //name
            SharedPreferences.Editor editor_pro_name = sp_pro_name.edit();
            editor_pro_name.putString("Name", edt_proname.getText().toString());
            editor_pro_name.apply();
            //service
            SharedPreferences.Editor editor_service = sp_service.edit();
            int ser = Integer.parseInt( myAdapterService.getItem(spinner_service.getSelectedItemPosition()).getServiceType());
            //String ser= listVOsService.get(spinner_service.getSelectedItemPosition()).getTitle();
            editor_service.putInt("ServiceType", ser);
            editor_service.putInt("Position", spinner_service.getSelectedItemPosition());
           // editor_service.putString("ServiceNameTH", isSelectService);
            editor_service.apply();
            //category
            SharedPreferences.Editor editor_cate = sp_cate.edit();
            int id_cate = Integer.parseInt(myAdapterCategory.getItem(spinner_cate.getSelectedItemPosition()).getCategoryID());
            editor_cate.putInt("CategoryID", id_cate);
            editor_cate.putInt("Position", spinner_cate.getSelectedItemPosition());
            //editor_cate.putString("CategoryNameTH", isSelectCate);
            editor_cate.apply();

            if(spinner_cate.getSelectedItemPosition()==1){
                sp_product.edit().clear().apply();
            }
            System.out.println("test : "+spinner_product.getSelectedItemPosition());
            if(spinner_product.getSelectedItemPosition()==1){
                sp_product.edit().clear().apply();
            }

            SharedPreferences.Editor editor_pro_all = sp_productAll.edit();
            int id_all = spinner_product.getSelectedItemPosition();
            editor_pro_all.putInt("Position", id_all);
            editor_pro_all.apply();
            //got to level1

        }
    }
    //check empty text
    private boolean checkEmpty(){
        boolean check=false;
        if(sp_product.getAll().size()==0&&sp_pro_name.getAll().size()==0&&
                sp_service.getAll().size()==0&&sp_cate.getAll().size()==0){
            check=true;
        }
        if(check==true){
            if(edt_proname.getText().toString().isEmpty()||
                    (spinner_service.getSelectedItemPosition()==0||
                    (spinner_cate.getSelectedItemPosition()==0)||spinner_product.getSelectedItemPosition()==0)){
                return true;
            }else{
                return false;
            }
        }else{
            /*if(sp_pro_name.getAll().size()==0&&sp_service.getAll().size()==0&&
                    sp_cate.getAll().size()==0&&(sp_product.getAll().size()==0&&spinner_product.getSelectedItemPosition()!=1)){
                return true;
            }else{
                return false;
            }*/
            if(edt_proname.getText().toString().isEmpty()||
                    (spinner_service.getSelectedItemPosition()==0||
                            (spinner_cate.getSelectedItemPosition()==0)||
                            (spinner_product.getSelectedItemPosition()==0&&sp_product.getAll().size()==0))){
                return true;
            }else if(sp_pro_name.getAll().size()==0&&sp_service.getAll().size()==0&&
                    sp_cate.getAll().size()==0&&(sp_product.getAll().size()==0&&spinner_product.getSelectedItemPosition()!=1)){
                return true;
            } else{
                return false;
            }
        }
    }
    //clear
    private void clearData(){
        sp_pro_name.edit().clear().apply();
        sp_service.edit().clear().apply();
        sp_cate.edit().clear().apply();
        sp_product.edit().clear().apply();
        sp_productAll.edit().clear().apply();
        sp_condition.edit().clear().apply();
        sp_branch.edit().clear().apply();
        sp_cust.edit().clear().apply();

        edt_proname.setText("");
        spinner_service.setSelection(0);
        spinner_cate.setSelection(0);
        spinner_product.setSelection(0);

        sp_date.edit().clear().apply();
        sp_time.edit().clear().apply();
        sp_branch_all.edit().clear().apply();
        sp_cust_all.edit().clear().apply();
        sp_standard.edit().clear().apply();

        textProductItems.setVisibility(View.GONE);
    }
    //init view
    private void initView(View view){
        spinner_service = view.findViewById(R.id.spinner_service);
        spinner_product = view.findViewById(R.id.spinner_product);
        spinner_cate=view.findViewById(R.id.spinner_cate);
        btn_next=view.findViewById(R.id.btn_next_condition);
        btn_clear=view.findViewById(R.id.btn_clear_condi);
        edt_proname=view.findViewById(R.id.edt_promotion_name);
        group_pro=view.findViewById(R.id.group_pro);
        score=view.findViewById(R.id.score_condi_level0);

        textProductItems=view.findViewById(R.id.textProductItem);
        textProductItems.setVisibility(View.GONE);

        int height=getActivity().getResources().getDisplayMetrics().heightPixels;
        score.getLayoutParams().height=(height*70)/100;

        spinner_cate.setEnabled(false);
        spinner_product.setEnabled(false);
        //group_pro.setVisibility(View.GONE);
    }
    //set spinner ser&cate
    private void setSpinnerProduct(ArrayList<String> select_qualification){
        listVOsProduct = new ArrayList<>();

        for (int i = 0; i < select_qualification.size(); i++) {
            StateVO stateVO = new StateVO();
            stateVO.setProductName(select_qualification.get(i));
            stateVO.setProductID(i);
            stateVO.setSelected(false);
            listVOsProduct.add(stateVO);
        }
        myAdapter = new MyAdapterSpinner(getActivity(), 0,
                listVOsProduct);
        spinner_product.setAdapter(myAdapter);


        productList=new ArrayList<>();
        Map<String, ?> entries2 = sp_product.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        boolean isChecked = false;
        for (String temp : list2) {
            for (int i = 0; i < sp_product.getStringSet(temp, null).size(); i++) {
                getData2 = sp_product.getStringSet(temp, null).toArray(new String[sp_product.getStringSet(temp, null).size()]);
                char chk = getData2[i].charAt(1);
                if (chk == 'a') {
                    productList.add(Integer.parseInt(getData2[i].substring(3)));
                }else if(chk == 'b'){
                    isChecked=Boolean.parseBoolean(getData2[i].substring(3));
                }
            }
        }
        int id_proAll=sp_productAll.getInt("Position",-1);
        spinner_product.setSelection(id_proAll);
    }
    //set spinner product
    private void setSpinnerService(ArrayList<String> serTH,ArrayList<Integer>serType,Spinner spinner){
        listVOsService = new ArrayList<>();

        for (int i = 0; i < serTH.size(); i++) {
            StateVOService stateVO = new StateVOService();
            stateVO.setTitle(serTH.get(i));
            stateVO.setServiceTyp(""+serType.get(i));
            listVOsService.add(stateVO);
        }
        myAdapterService = new MyAdapterSpinnerService(getActivity(), 0,
                listVOsService);
        spinner.setAdapter(myAdapterService);
    }
    private void setSpinnerCategory(ArrayList<String> arrCate,ArrayList<Integer>arrCateID,Spinner spinner){
        listVOsCategory = new ArrayList<>();
        for (int i = 0; i < arrCate.size(); i++) {
            StateVOCategory stateVO = new StateVOCategory();
            stateVO.setTitle(arrCate.get(i));
            stateVO.setCategoryID(""+arrCateID.get(i));
            listVOsCategory.add(stateVO);
        }
        myAdapterCategory = new MyAdapterSpinnerCategory(getActivity(), 0,
                listVOsCategory);
        spinner.setAdapter(myAdapterCategory);

        int cate_id=sp_cate.getInt("Position",0);
        if(cate_id!=0) {
            spinner_cate.setSelection(cate_id);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mDb = mSQLite.getReadableDatabase();
        String sql_service = "SELECT ServiceNameTH,ServiceType FROM tb_service ";
        Cursor cursor = mDb.rawQuery(sql_service, null);
        ArrayList<String> arr_service=new ArrayList<>();
        ArrayList<Integer> arr_serviceType=new ArrayList<>();
        arr_service.add("--เลือกบริการ--");
        arr_serviceType.add(0);
        while (cursor.moveToNext()) {
            arr_service.add(cursor.getString(0) );
            arr_serviceType.add(Integer.parseInt(cursor.getString(1)));
        }
        setSpinnerService(arr_service,arr_serviceType,spinner_service);

        if(sp_pro_name.getAll().size()==0&&sp_service.getAll().size()==0&&
                sp_cate.getAll().size()==0&&sp_product.getAll().size()==0){
            //System.out.print("loadWhenStart");

        }else{
            //loadWhenStart();
            loadWhenSelected();
            //System.out.print("loadWhenSelected");
        }


//        int id_service=sp_service.getInt("ServiceType",0);
        int id_cate=sp_cate.getInt("CategoryID",0);
        spinner_cate.setSelection(sp_cate.getInt("Position",0));
        if(sp_product.getAll().size()>0){
            spinner_product.setSelection(1);
        }
        getDataPage();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String url=getIPAPI.IPAddress+"/check/check.php?BranchID="+users.branchID+"&BranchGroupID="+users.branchGroup;
                url+="&ServiceType=1";
                url+="&Category=1";
                //url+="&ServiceType=1";
                new MyAsyncTask().execute(url);*/
                if (checkEmpty()) {
                    Snackbar.make(myView, "กรุณากรอกข้อมูลให้ครบก่อน", Snackbar.LENGTH_SHORT).show();
                } else {
                    JsonArray jsonArray = new JsonArray();
                    JsonObject jsonObject = new JsonObject();
                    String url;

                    String serviceType = myAdapterService.getItem(spinner_service.getSelectedItemPosition()).getServiceType();
                    String categoryID = myAdapterCategory.getItem(spinner_cate.getSelectedItemPosition()).getCategoryID();
                    String productItem = myAdapter.getItem(spinner_product.getSelectedItemPosition()).getProductName();
                    System.out.println(serviceType + " , " + categoryID + " , " + productItem);
                    if (Integer.parseInt(categoryID) == 0 && productItem.equals("เลือกสินค้าทั้งหมด")) {
                        jsonObject.addProperty("ServiceType", serviceType);
                        jsonObject.addProperty("BranchID", users.branchID);
                        jsonObject.addProperty("BranchGroupID", users.branchGroup);
                        jsonArray.add(jsonObject);
                        url = getIPAPI.IPAddress + "/check/check1.php";
                        new MyAsyncTask().execute(url, String.valueOf(jsonArray));
                    } else if (Integer.parseInt(categoryID) != 0 && productItem.equals("เลือกสินค้าทั้งหมด")) {
                        jsonObject.addProperty("ServiceType", serviceType);
                        jsonObject.addProperty("CategoryID", categoryID);
                        jsonObject.addProperty("BranchID", users.branchID);
                        jsonObject.addProperty("BranchGroupID", users.branchGroup);
                        jsonArray.add(jsonObject);
                        url = getIPAPI.IPAddress + "/check/check2.php";
                        new MyAsyncTask().execute(url, String.valueOf(jsonArray));
                    } else {
                        productList = new ArrayList<>();
                        Map<String, ?> entries2 = sp_product.getAll();
                        Set<String> keys2 = entries2.keySet();
                        String[] getData2;
                        List<String> list2 = new ArrayList<String>(keys2);
                        for (String temp : list2) {
                            for (int i = 0; i < sp_product.getStringSet(temp, null).size(); i++) {
                                getData2 = sp_product.getStringSet(temp, null).toArray(new String[sp_product.getStringSet(temp, null).size()]);
                                char chk = getData2[i].charAt(1);
                                if (chk == 'a') {
                                    productList.add(Integer.parseInt(getData2[i].substring(3)));
                                }
                            }
                        }

                        for (int i = 0; i < productList.size(); i++) {
                            jsonObject.addProperty("ServiceType", serviceType);
                            jsonObject.addProperty("CategoryID", categoryID);
                            jsonObject.addProperty("ProductID", productList.get(i));
                            jsonObject.addProperty("BranchID", users.branchID);
                            jsonObject.addProperty("BranchGroupID", users.branchGroup);
                            jsonArray.add(jsonObject);
                        }
                        url = getIPAPI.IPAddress + "/check/check3.php";
                        new MyAsyncTask().execute(url, String.valueOf(jsonArray));
                    }
                }


                //current
                /*if(sp_standard.getBoolean("keys",false)==true){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel1()).commit();
                }else {
                    if (checkEmpty()) {
                        Snackbar.make(myView, "กรุณากรอกข้อมูลให้ครบก่อน", Snackbar.LENGTH_SHORT).show();
                    } else {
                        setDataPage();

                        System.out.println(sp_product.getAll().size());
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        if (false) {
                            transaction.addToBackStack(null);
                        }
                        transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel1()).commit();
                    }
                }*/
            }
        });
        if(sp_standard.getBoolean("keys",false)==true){
            btn_clear.setVisibility(View.GONE);
        }
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(checkEmpty()==true){
                    Toast.makeText(getActivity(),"ยังไม่มีข้อมูลในหน้านี้",Toast.LENGTH_SHORT).show();
                }else {
                    setDialog("ยืนยัน", "ยืนยันการล้างข้อมูลในหน้านี้?");
                }*/
                setDialog("ยืนยัน", "ยืนยันการล้างข้อมูลในหน้านี้?");
            }
        });

    }
    private void loadWhenSelected(){
        //add data cate
        String ser = sp_service.getString("ServiceNameTH","");
        String sql_category = "select CategoryNameTH,cate.CategoryID from tb_service ser left join (tb_product pro left join tb_category cate on pro.CategoryID=cate.CategoryID) on ser.ServiceType=pro.ServiceType where ser.ServiceNameTH='" + ser + "' Group By cate.CategoryID,CategoryNameTH";
        Cursor cursor = mDb.rawQuery(sql_category, null);
        ArrayList<String> arr_category = new ArrayList<>();
        ArrayList<Integer> arr_categoryID = new ArrayList<>();
        arr_category.add("--เลือกหมวดหมู่--");
        arr_category.add("เลือกหมวดหมู่ทั้งหมด");
        arr_categoryID.add(-1);
        arr_categoryID.add(0);
        while (cursor.moveToNext()) {
            arr_category.add(cursor.getString(0));
            arr_categoryID.add(Integer.parseInt(cursor.getString(1)));
        }
        //System.out.print("cate size 2 : "+arr_category.size());
        setSpinnerCategory(arr_category,arr_categoryID, spinner_cate);

        //add data product
        String cate=sp_cate.getString("CategoryNameTH","");
        String sql_product = "select ProductNameTH from tb_service ser left join (tb_product pro left join tb_category cate on pro.CategoryID=cate.CategoryID) on ser.ServiceType=pro.ServiceType where cate.CategoryNameTH='"+cate+"' Group By pro.ProductID,ProductNameTH";
        cursor = mDb.rawQuery(sql_product, null);
        ArrayList<String> arr_product = new ArrayList<>();
        arr_product.add("--เลือกสินค้า--");
        arr_product.add("เลือกสินค้าทั้งหมด");
        while (cursor.moveToNext()) {
            arr_product.add(cursor.getString(0));
        }
        setSpinnerProduct(arr_product);

        //getDataPage();

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
            String response="";
            String output="";

            String str_url = strings[0];
            String JsonDATA = strings[1];
            HttpURLConnection httpURLConnection = null;
            try {
                URL url = new URL(str_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                // is output buffer writter
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                //set headers and method
                Writer writer = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                writer.write(JsonDATA);
                // json data
                writer.close();

                InputStream inputStream = httpURLConnection.getInputStream();

                Scanner scanner = new Scanner(inputStream, "UTF-8");
                response = scanner.useDelimiter("\\A").next();

            } catch (Exception ex) {
                System.out.println("Error1 : " + ex.getMessage());
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }

                try {
                    System.out.println("response1 : "+response);
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        IsResponse=jsonObj.getBoolean("IsCheck");
                        msgResponse=jsonObj.getString("CheckName");
                    }
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

            if(IsResponse==true){
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custon_alert_dialog);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                TextView title = dialog.findViewById(R.id.tv_quit_learning);
                TextView des = dialog.findViewById(R.id.tv_description);
                title.setText("แจ้งเตือน");
                des.setText("เงื่อนไขของคุณ อาจซ้ำกับโปรฯ "+msgResponse +" เดิมที่มีอยู่แล้ว");
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                declineButton.setVisibility(View.GONE);
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }else{
                if(sp_standard.getBoolean("keys",false)==true){
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel1()).commit();
                }else {
                    if (checkEmpty()) {
                        Snackbar.make(myView, "กรุณากรอกข้อมูลให้ครบก่อน", Snackbar.LENGTH_SHORT).show();
                    } else {
                        setDataPage();

                        System.out.println(sp_product.getAll().size());
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        if (false) {
                            transaction.addToBackStack(null);
                        }
                        transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel1()).commit();
                    }
                }
            }
        }
    }
}

