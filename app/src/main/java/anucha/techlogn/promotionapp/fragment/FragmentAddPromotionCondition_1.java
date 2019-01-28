package anucha.techlogn.promotionapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Adapter.MyAdapterSearchProduct;
import Adapter.MyAdapterSpinnerProduct;
import CustomItem.CustomItemProduct;
import CustomItem.CustomItemSearchProduct;
import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.R;
import anucha.techlogn.promotionapp.SQLiteHelper;

public class FragmentAddPromotionCondition_1 extends Fragment {

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private Button btn_back,btn_save;
    ImageView img_add;
    private RecyclerView rcv;
    private ScrollView score;
    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    private MyAdapterSearchProduct myAdapter;
    private ArrayList<CustomItemSearchProduct>items;
    private ArrayList items_check;
    private SharedPreferences sp_product;
    private Spinner spinner_product;
    private ArrayList<CustomItemProduct> items_product;
    private MyAdapterSpinnerProduct myAdapterSpinnerProduct;
    private DataUserLogin users;
    private ArrayList<String>arrProID,arrProTH,arrProEN,arrProPrice,arrSer,arrCate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_1_condition_1, mContainer, false);
        mSQLite=SQLiteHelper.getInstance(getActivity());
        initView(myView);
        sp_product = getContext().getSharedPreferences("Product", Context.MODE_PRIVATE);
        items_check=new ArrayList();
        items=new ArrayList<>();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(myAdapter!=null) {
                    for (int i = 0; i < myAdapter.getItemCount(); i++) {
                        //System.out.println(myAdapter.mDataID.size() + " : " + myAdapter.mDataID.get(i));
                        System.out.println(items.get(i).mProductID+", "+items.get(i).mProductNameTH+", "+items.get(i).mProductNameEN+", "+items.get(i).mProductPrice+", "+items.get(i).mServiceType+", "+items.get(i).mCategoryID);
                    }
                    sp_product.edit().clear().apply();
                    SharedPreferences.Editor editor_product = sp_product.edit();
                    HashSet<String> mSet1;
                    for (int i = 0; i < myAdapter.getItemCount(); i++) {
                        mSet1 = new HashSet<>();
                        mSet1.add("<a>" + items.get(i).mProductID);
                        mSet1.add("<b>" + items.get(i).mProductNameTH);
                        mSet1.add("<c>" + items.get(i).mProductNameEN);
                        mSet1.add("<d>" + items.get(i).mProductPrice);
                        mSet1.add("<e>" + items.get(i).mServiceType);
                        mSet1.add("<f>" + items.get(i).mCategoryID);
                        editor_product.putStringSet(items.get(i).mProductID, mSet1);
                        editor_product.apply();
                    }


                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, new FragmentAddPromotionCondition()).commit();
                }else{
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, new FragmentAddPromotionCondition()).commit();
                }
            }
        });
        myView.setFocusableInTouchMode(true);
        myView.requestFocus();
        myView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Toast.makeText(getActivity(),"กรุณากดปุ่มตกลง",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        getData();

        return myView;
    }
    private void getData(){
        arrProID=new ArrayList();
        arrProTH=new ArrayList();
        arrProEN=new ArrayList();
        arrProPrice=new ArrayList();
        arrSer=new ArrayList();
        arrCate=new ArrayList();

        Map<String, ?> entries2 = sp_product.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            System.out.println(temp+" = "+sp_product.getStringSet(temp,null));
            for (int i = 0; i < sp_product.getStringSet(temp, null).size(); i++) {
                getData2 = sp_product.getStringSet(temp, null).toArray(new String[sp_product.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk = getData2[i].charAt(1);
                if (chk == 'a') {
                    arrProID.add(getData2[i].substring(3));
                }else if(chk == 'b'){
                    arrProTH.add(getData2[i].substring(3));
                } else if(chk == 'c'){
                    arrProEN.add(getData2[i].substring(3));
                } else if(chk == 'd'){
                    arrProPrice.add(getData2[i].substring(3));
                } else if(chk == 'e'){
                    arrSer.add(getData2[i].substring(3));
                }else if(chk == 'f'){
                    arrCate.add(getData2[i].substring(3));
                }
            }
        }
        for(int i=0;i<arrProID.size();i++){
            boolean isExist = isExist(arrProID.get(i));
            if (!isExist) {
                items_check.add(arrProID.get(i));
                items.add(new CustomItemSearchProduct(arrProID.get(i),
                        arrProTH.get(i),
                        arrProEN.get(i),
                        arrProPrice.get(i),
                        arrSer.get(i),
                        arrCate.get(i)));
                serRCV(items);
            }else{
                Snackbar.make(myView,"ข้อมูลลูกค้านี้ถูกเพิ่มแล้ว",Snackbar.LENGTH_SHORT).show();
            }
        }
    }


    //init view
    private void initView(View view){
        btn_back=view.findViewById(R.id.btn_back_level2);
        btn_save=view.findViewById(R.id.btn_save_cust);
        score=view.findViewById(R.id.score_level_2_2);
        spinner_product=view.findViewById(R.id.spinner_product);
        rcv=view.findViewById(R.id.rcv);

        int height=getActivity().getResources().getDisplayMetrics().heightPixels;
        score.getLayoutParams().height=(height*70)/100;
        rcv.getLayoutParams().height=(height*70)/100;
        btn_back.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mDb = mSQLite.getReadableDatabase();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String cateID = bundle.getString("CategoryID");
            String serviceType = bundle.getString("ServiceType");
            System.out.println(cateID);
            String sql_branch = "SELECT pro.ProductID,pro.ProductNameTH,pro.ProductNameEN,pro.ProductPrice,pro.ServiceType,pro.CategoryID FROM tb_product pro left join tb_category cate on pro.CategoryID = cate.CategoryID where cate.CategoryID='"+cateID+"' AND pro.ServiceType='"+serviceType+"'";
            Cursor cursor = mDb.rawQuery(sql_branch, null);
            items_product=new ArrayList<>();
            items_product.add(new CustomItemProduct(""+0,"--เลือกสินค้า--",""+0,""+0,""+0,""+0));
            while (cursor.moveToNext()) {
                items_product.add(new CustomItemProduct(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            }
            myAdapterSpinnerProduct = new MyAdapterSpinnerProduct(getActivity(), 0,
                    items_product);
            spinner_product.setAdapter(myAdapterSpinnerProduct);

            spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String ss = items_product.get(position).mProductID;
                    getBranchDataSpinner(ss);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            spinner_product.setEnabled(false);
        }

    }
    private void getBranchDataSpinner(String id) {
        String sql_product = "";
        sql_product = "SELECT ProductID,ProductNameTH,ProductNameEN,ProductPrice,ServiceType,CategoryID FROM tb_product where ProductID='" + id + "'";
        Cursor cursor = mDb.rawQuery(sql_product, null);
        spinner_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                } else {
                    boolean isExist = isExist(items_product.get(position).mProductID);
                    if (!isExist) {
                        items_check.add(items_product.get(position).mProductID);
                        items.add(new CustomItemSearchProduct(items_product.get(position).mProductID,
                                items_product.get(position).mProductNameTH,
                                items_product.get(position).mProductNameEN,
                                items_product.get(position).mProductPrice,
                                items_product.get(position).mServiceType,
                                items_product.get(position).mCategoryID));
                        serRCV(items);
                    } else {
                        Snackbar.make(myView, "ข้อมูลลูกค้านี้ถูกเพิ่มแล้ว", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void serRCV(ArrayList<CustomItemSearchProduct>items){
        myAdapter = new MyAdapterSearchProduct(getActivity(), items);
        rcv.setAdapter(myAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public boolean isExist(String strNama) {
        for (int i = 0; i < items_check.size(); i++) {
            if (items_check.get(i).equals(strNama)) {
                return true;
            }
        }

        return false;
    }
}

