package anucha.techlogn.promotionapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import Adapter.MyAdapterAddPromo;
import CustomItem.CustomItemAddPromo;
import CustomItem.CustomItemSection;
import anucha.techlogn.promotionapp.R;
import anucha.techlogn.promotionapp.SQLiteHelper;

public class FragmentAddPromotion extends Fragment {

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private MyAdapterAddPromo myAdapter;
    private ArrayList items;
    private RecyclerView rcv;
    private Button btn_set_condition;
    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    private SharedPreferences sp_pro_name,sp_service,sp_cate,sp_product,sp_condition,sp_branch,sp_cust,sp_date,sp_time,sp_productAll,sp_branch_all,sp_cust_all,sp_standard;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_1_layout, mContainer, false);
        mSQLite=SQLiteHelper.getInstance(getActivity());
        items=new ArrayList<>();
        initView(myView);
        //setItem();
        initSaveData();

        btn_set_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentAddPromotionCondition()).commit();
            }
        });


        return myView;
    }
    private void initView(View view){
        rcv=view.findViewById(R.id.rcv_list_create_promo);
        btn_set_condition=view.findViewById(R.id.btn_condition);
    }

    @Override
    public void onStart() {
        super.onStart();

        mDb = mSQLite.getReadableDatabase();
        String sql_service = "SELECT distinct p.PromotionID,PromotionName FROM tb_promotion p left join tb_promotionDetail pd on p.PromotionID=pd.PromotionID";
        Cursor cursor = mDb.rawQuery(sql_service, null);

        String date = "";
        String lastDate = "";
        String str="";
        int i=0;
        while (cursor.moveToNext()) {
            i++;
            /*date = "โปรโมชั่น standard";
            //date = jsonObj1.getString("OrderDate");
            if (!date.trim().equals(lastDate)) {
                str = "รายการโปรโมชั่น standard";
                //str = "วันที่ทำรายการ : "+jsonObj1.getString("OrderDate");
                items.add(new CustomItemSection(str));
                lastDate = date;
            }*/
            String title;
            if(cursor.getString(1).length()>25){
                title=i+". "+cursor.getString(1).substring(0,25)+"...";
            }else{
                title=i+". "+cursor.getString(1);
            }
            items.add(new CustomItemAddPromo(cursor.getString(0),title,cursor.getString(1)));
            //arr_service.add(cursor.getString(0) );
        }
        myAdapter = new MyAdapterAddPromo(getActivity(),items);
        rcv.setAdapter(myAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));

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
}

