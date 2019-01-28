package anucha.techlogn.promotionapp.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import Adapter.MyAdapterSearchCust;
import Adapter.MyAdapterSpinnerBranch;
import Adapter.MyAdapterSpinnerCust;
import CustomItem.CustomItemSearchCust;
import CustomItem.StateVOCust;
import anucha.techlogn.promotionapp.R;
import anucha.techlogn.promotionapp.SQLiteHelper;

public class FragmentAddPromotionConditionLevel2 extends Fragment{

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private SharedPreferences sp_date,sp_time,sp_pro_name,sp_service,sp_cate,sp_product,sp_condition,sp_branch,sp_cust,
            sp_productAll,sp_branch_all,sp_cust_all,sp_standard;
    private RadioButton rdb1,rdb2,rdb4,rdb5;
    private Button btn_back,btn_next;
    private RelativeLayout group1,group3;
    private ScrollView score;
    private MyAdapterSpinnerBranch myAdapterBranch;
    private MyAdapterSpinnerCust myAdapterCust;
    private ArrayList<Integer>customerID,branchID;
    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    private Button searchCustomer,searchBranch;
    ArrayList<CustomItemSearchCust> items;
    private ArrayAdapter adapterData,adapterData1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_1_condition_level2, mContainer, false);

        mSQLite = SQLiteHelper.getInstance(getActivity());
        customerID=new ArrayList<>();
        branchID=new ArrayList<>();

        initView(myView);

        /*final String[] select_qualification = {
                "--เลือกลูกค้า--", "10th / Below", "12th", "Diploma", "UG",
                "PG", "Phd"};
        ArrayList<String> arr=new ArrayList<>();
        for(int i=0;i<select_qualification.length;i++){
            arr.add(select_qualification[i]);
        }
        setSpinnerBranch(arr,spinner_branch);
        setSpinnerCust(arr,spinner_cust2);*/
        //setSpinnerCust(arr,spinner_cust2);

        items=new ArrayList();
        //items.add(new CustomItemHome(jsonObj.getString("PromotionName"),jsonObj.getString("IsActive"), start,end,createBy));
        searchCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setDialog("เพิ่มลูกค้า",items,false);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel2_1()).commit();
            }
        });
        searchBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel2_2()).commit();
            }
        });

        isDiscount();
        isBrnach();
        initSaveData();
        getDataPage();

        if(sp_standard.getBoolean("keys",false)==true){
            btn_back.setText("แก้ไขเพิ่มเติม");
            //btn_back.setVisibility(View.GONE);
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to level0
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel1()).commit();
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataPage();
            }
        });

        return myView;
    }
    //set dialog
    private void setDialog(String sTitle, final ArrayList<CustomItemSearchCust> arrayList, boolean isStatus){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_alert_customer);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView title = dialog.findViewById(R.id.tv_quit_learning);
        final RecyclerView rcv=dialog.findViewById(R.id.rcv);
        final AutoCompleteTextView edit=dialog.findViewById(R.id.searchViewCust);

       /* MyAdapterSearchCust myAdapter = new MyAdapterSearchCust(getActivity(),arrayList);
        rcv.setAdapter(myAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));*/
        final ArrayList<CustomItemSearchCust> arrayList1=new ArrayList<>();
        ImageView add =dialog.findViewById(R.id.img_add_cust);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList1.add(new CustomItemSearchCust("xhk","asd","asdas"));
                MyAdapterSearchCust myAdapter = new MyAdapterSearchCust(dialog.getContext(),arrayList1);
                rcv.setAdapter(myAdapter);
                rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
                Toast.makeText(getActivity(),""+edit.getText().toString(),Toast.LENGTH_SHORT).show();
                edit.setText("");
            }
        });
        MyAdapterSearchCust myAdapter = new MyAdapterSearchCust(getActivity(),arrayList1);
        rcv.setAdapter(myAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));



        edit.setAdapter(adapterData);
        edit.setThreshold(0);

        title.setText(sTitle);
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

    }
    //init sharepref
    private void initSaveData(){
        /*sp_branch = getContext().getSharedPreferences("Branch", Context.MODE_PRIVATE);
        sp_cust = getContext().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        sp_branch_all = getContext().getSharedPreferences("BranchAll", Context.MODE_PRIVATE);
        sp_cust_all = getContext().getSharedPreferences("CustomerAll", Context.MODE_PRIVATE);*/

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
    //init view
    private void initView(View view){
        rdb1 = view.findViewById(R.id.rdbCust1);
        rdb2 = view.findViewById(R.id.rdbCust2);
        rdb4=view.findViewById(R.id.rdbBranch1);
        rdb5=view.findViewById(R.id.rdbBranch2);
        btn_back=view.findViewById(R.id.btn_back_level2);
        btn_next=view.findViewById(R.id.btn_next_condition_level2);
        group1=view.findViewById(R.id.group_cust1);
        group3=view.findViewById(R.id.group_branch);
        score=view.findViewById(R.id.score_condi_level2);
        searchCustomer=view.findViewById(R.id.btn_select_cust);
        searchBranch=view.findViewById(R.id.btn_select_branch);

        group1.setVisibility(View.GONE);
        group3.setVisibility(View.GONE);
        rdb1.setChecked(true);
        rdb4.setChecked(true);

        int height=getActivity().getResources().getDisplayMetrics().heightPixels;
        score.getLayoutParams().height=(height*70)/100;
    }
    private void isDiscount(){
        rdb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    group1.setVisibility(View.GONE);
                }
            }
        });
        rdb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                   group1.setVisibility(View.VISIBLE);
                }
            }
        });

    }
    private void isBrnach(){
        rdb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group3.setVisibility(View.VISIBLE);
            }
        });
        rdb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group3.setVisibility(View.GONE);
            }
        });
    }
    //load data
    private void getDataPage(){
       /* Map<String, ?> entries = sp_cust.getAll();
        Set<String> keys = entries.keySet();
        String[] getData;
        List<String> list = new ArrayList<String>(keys);
        boolean isChecked1_1 = false;
        boolean isChecked1_2 = false;
        for (String temp : list) {
            //System.out.println(temp+" = "+sharedPreferences.getStringSet(temp,null));
            for (int i = 0; i < sp_cust.getStringSet(temp, null).size(); i++) {
                getData = sp_cust.getStringSet(temp, null).toArray(new String[sp_cust.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk = getData[i].charAt(1);
                if (chk == 'a') {
                    customerID.add(Integer.parseInt(getData[i].substring(3)));
                }else if(chk == 'b'){
                    isChecked1_1=Boolean.parseBoolean(getData[i].substring(3));
                }else if(chk == 'c'){
                    isChecked1_2=Boolean.parseBoolean(getData[i].substring(3));
                }
            }
        }*/
       if(sp_cust.getAll().size()>0){
           rdb1.setChecked(false);
           rdb2.setChecked(true);
       }else{
           rdb1.setChecked(true);
           rdb2.setChecked(false);
       }
       if(rdb2.isChecked()==true){
            rdb2.setChecked(true);
            group1.setVisibility(View.VISIBLE);
        }

        //set branch
        /*Map<String, ?> entries2 = sp_branch.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        boolean isChecked2_1 = false;
        boolean isChecked2_2 = false;
        for (String temp : list2) {
            //System.out.println(temp+" = "+sharedPreferences.getStringSet(temp,null));
            for (int i = 0; i < sp_branch.getStringSet(temp, null).size(); i++) {
                getData2 = sp_branch.getStringSet(temp, null).toArray(new String[sp_branch.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk = getData2[i].charAt(1);
                if (chk == 'a') {
                    branchID.add(Integer.parseInt(getData2[i].substring(3)));
                }else if(chk == 'b'){
                    isChecked2_1=Boolean.parseBoolean(getData2[i].substring(3));
                }else if(chk == 'c'){
                    isChecked2_2=Boolean.parseBoolean(getData2[i].substring(3));
                }
            }
        }*/
        if(sp_branch.getAll().size()>0){
            rdb4.setChecked(false);
            rdb5.setChecked(true);
        }else{
            rdb4.setChecked(true);
            rdb5.setChecked(false);
        }
        if(rdb5.isChecked()==true){
            //rdb5.setChecked(true);
            group3.setVisibility(View.VISIBLE);
        }
        //rdb4.setChecked(isChecked2);
    }
    private void setDataPage() {
        if(checkEmpty()==true){
            Snackbar.make(myView,"กรุณากรอกข้อมูลให้ครบก่อน",Snackbar.LENGTH_SHORT).show();
        }else {
            SharedPreferences.Editor editor_branch = sp_branch_all.edit();
            boolean checked = rdb4.isChecked();
            editor_branch.putBoolean("CheckAll", checked);
            editor_branch.apply();

            SharedPreferences.Editor editor_cust = sp_cust_all.edit();
            boolean checked1 = rdb1.isChecked();
            editor_cust.putBoolean("CheckAll", checked1);
            editor_cust.apply();

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (false) {
                transaction.addToBackStack(null);
            }
            transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel3()).commit();
        }
    }
    //check empty text
    private boolean checkEmpty(){
        System.out.println(rdb2.isChecked()+", "+sp_cust.getAll().size()+", "+rdb5.isChecked());
        /*if(rdb2.isChecked()==true&&myAdapterCust.listGetData.size()==0){
            return true;
        }else */
        if(rdb2.isChecked()==true&&sp_cust.getAll().size()==0||(rdb5.isChecked()==true&&sp_branch.getAll().size()==0)){
            return true;
        }else{
            return false;
        }
    }
    //set spinner product
    private void setSpinnerCust(ArrayList<String> select_qualification, Spinner spinner){
        ArrayList<StateVOCust> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.size(); i++) {
            StateVOCust stateVO = new StateVOCust();
            stateVO.setCustomerName(select_qualification.get(i));
            stateVO.setCustomerID(i);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        myAdapterCust = new MyAdapterSpinnerCust(getActivity(), 0,
                listVOs);
        spinner.setAdapter(myAdapterCust);
    }

    private void checkStandrdBoard(){
        boolean isStandard = sp_standard.getBoolean("keys",false);
        if(isStandard==true){
            String promoID=sp_standard.getString("PromotionID","");
            mDb = mSQLite.getReadableDatabase();
            String sql_service = "sEleCt p.PromotionName,pd.ServiceType,pd.CategoryID,pd.ProductID,pd.ConditionType,pd.ConditionAmount,pd.ConditionPrice,pmd.DiscountType,pmd.DiscountPriceType,pmd.DiscountRate,pmd.DiscountPrice,pmd.DiscountProductNo,pdt.ProductNameTH,pdt.ProductNameEN,pdt.ProductPrice,p.CategorySelect,p.ProductSelect fRoM tb_promotion p left join ((tb_promotionDetail pd left join tb_promotionDiscount pmd on pd.PromotionDetailID=pmd.PromotionDetailID) left join tb_product pdt on pd.ProductID=pdt.ProductID) on p.PromotionID=pd.PromotionID wHeRe p.PromotionID='"+promoID+"'";
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
            int selcCate=0,selcPro=0;
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

                selcCate=Integer.parseInt(cursor.getString(15));
                selcPro=Integer.parseInt(cursor.getString(16));

                if(!cursor.getString(3).isEmpty()){
                    arr_pro_id.add(productID);
                    arr_pro_name_th.add(cursor.getString(12));
                    arr_pro_name_en.add(cursor.getString(13));
                    arr_pro_price.add(cursor.getString(14));
                }else{
                    position=1;
                }
            }
            /*for(int i=0;i<arr_pro_id.size();i++){
                System.out.println(arr_pro_id.get(i)+","+arr_pro_name_th.get(i)+","+arr_pro_name_en.get(i)+","+arr_pro_price.get(i));
            }*/


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
            editor_cate.putInt("Position", selcCate);
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
            editor_pro_all.putInt("Position", selcPro);
            editor_pro_all.apply();


            System.out.println(sp_product.getAll().size());
        }
    }
    @Override
    public void onStart() {
        super.onStart();

        checkStandrdBoard();

        mDb = mSQLite.getReadableDatabase();
        String sql_branch = "SELECT BranchNameTH FROM tb_branch ";
        Cursor cursor = mDb.rawQuery(sql_branch, null);
        ArrayList<String> arr_branch=new ArrayList<>();
        arr_branch.add("--เลือกสาขา--");
        while (cursor.moveToNext()) {
            arr_branch.add(cursor.getString(0) );
        }
        adapterData1= new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, arr_branch);


    }
}

