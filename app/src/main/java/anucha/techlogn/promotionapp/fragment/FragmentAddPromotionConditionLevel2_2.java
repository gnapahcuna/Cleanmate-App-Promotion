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

import Adapter.MyAdapterSearchBranch;
import Adapter.MyAdapterSpinnerBranch;
import Adapter.MyAdapterSpinnerBranchGroup;
import CustomItem.CustomItemBranchGroup;
import CustomItem.CustomItemBranchName;
import CustomItem.CustomItemSearchBranch;
import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.R;
import anucha.techlogn.promotionapp.SQLiteHelper;

public class FragmentAddPromotionConditionLevel2_2 extends Fragment{

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private Button btn_back,btn_save;
    ImageView img_add;
    private RecyclerView rcv;
    private ScrollView score;
    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    private MyAdapterSearchBranch myAdapter;
    private ArrayList<CustomItemSearchBranch>items;
    private ArrayList items_check;
    private SharedPreferences sp_branch;
    private ArrayList<String> arrBranchID,arrBranchName,arrBranchGroup,arrBranchGroupID;
    private Spinner spinner_group,spinner_name;
    private MyAdapterSpinnerBranchGroup myAdapterSpinnerBranchGroup;
    private ArrayList<CustomItemBranchGroup> items_group;
    private ArrayList<CustomItemBranchName> items_branch;
    private MyAdapterSpinnerBranch myAdapterSpinnerBranch;
    private DataUserLogin users;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_1_condition_level2_2, mContainer, false);
        users=new DataUserLogin(getActivity());
        mSQLite = SQLiteHelper.getInstance(getActivity());
        //sp_cust = getContext().getSharedPreferences("Customer", Context.MODE_PRIVATE);
        sp_branch = getContext().getSharedPreferences("Branch", Context.MODE_PRIVATE);

        initView(myView);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(items.size()!=0) {
                    for (int i = 0; i < myAdapter.getItemCount(); i++) {
                        //System.out.println(myAdapter.mDataID.size() + " : " + myAdapter.mDataID.get(i));
                        System.out.println(items.get(i).mBranchID+", "+items.get(i).mBranchName+", "+items.get(i).mBranchGroup+", "+items.get(i).mBranchGroupID);
                    }
                    sp_branch.edit().clear().apply();
                    SharedPreferences.Editor editor_product = sp_branch.edit();
                    HashSet<String> mSet1 = new HashSet<>();
                    for (int i = 0; i < myAdapter.getItemCount(); i++) {
                        int id = Integer.parseInt("" + myAdapter.mDataID.get(i));
                        mSet1.add("<a>" + items.get(i).mBranchID);
                        mSet1.add("<b>" + items.get(i).mBranchName);
                        mSet1.add("<c>" + items.get(i).mBranchGroup);
                        mSet1.add("<d>" + items.get(i).mBranchGroupID);
                        editor_product.putStringSet(items.get(i).mBranchID, mSet1);
                        editor_product.apply();
                    }


                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel2()).commit();
                }else{
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel2()).commit();
                }
            }
        });
        items_check=new ArrayList();
        items=new ArrayList<>();


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
        arrBranchID=new ArrayList();
        arrBranchName=new ArrayList();
        arrBranchGroup=new ArrayList();
        arrBranchGroupID=new ArrayList();


        Map<String, ?> entries2 = sp_branch.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            System.out.println(temp+" = "+sp_branch.getStringSet(temp,null));
            for (int i = 0; i < sp_branch.getStringSet(temp, null).size(); i++) {
                getData2 = sp_branch.getStringSet(temp, null).toArray(new String[sp_branch.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk = getData2[i].charAt(1);
                if (chk == 'a') {
                    arrBranchID.add(getData2[i].substring(3));
                }else if(chk == 'b'){
                    arrBranchName.add(getData2[i].substring(3));
                } else if(chk == 'c'){
                    arrBranchGroup.add(getData2[i].substring(3));
                } else if(chk == 'd'){
                    arrBranchGroupID.add(getData2[i].substring(3));
                }
            }
        }
        for(int i=0;i<arrBranchID.size();i++){
            boolean isExist = isExist(arrBranchID.get(i));
            if (!isExist) {
                items_check.add(arrBranchID.get(i));
                items.add(new CustomItemSearchBranch(arrBranchID.get(i),arrBranchGroupID.get(i),
                        arrBranchName.get(i), arrBranchGroup.get(i)));
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
        spinner_group=view.findViewById(R.id.spinner_group);
        spinner_name=view.findViewById(R.id.spinner_name);
        rcv=view.findViewById(R.id.rcv);

        int height=getActivity().getResources().getDisplayMetrics().heightPixels;
        score.getLayoutParams().height=(height*70)/100;
        rcv.getLayoutParams().height=(height*70)/100;
        btn_back.setVisibility(View.GONE);
    }

    //set spinner product
    @Override
    public void onStart() {
        super.onStart();
        mDb = mSQLite.getReadableDatabase();
        String sql_branch = "SELECT * FROM tb_branchgroup";
        Cursor cursor = mDb.rawQuery(sql_branch, null);
        items_group=new ArrayList<>();
        items_group.add(new CustomItemBranchGroup(""+0,"--เลือกกลุ่มสาขา--"));
        while (cursor.moveToNext()) {
            items_group.add(new CustomItemBranchGroup(cursor.getString(0),cursor.getString(1)));
        }
        myAdapterSpinnerBranchGroup = new MyAdapterSpinnerBranchGroup(getActivity(), 0,
                items_group);
        spinner_group.setAdapter(myAdapterSpinnerBranchGroup);

        spinner_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ss = items_group.get(position).mBranchGroupID;
                String ss1 = items_group.get(position).mBranchGroupName;
                //Toast.makeText(getActivity(), ss + ", " + ss1, Toast.LENGTH_SHORT).show();
                getBranchDataSpinner(ss);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*if(Integer.parseInt(users.branchGroup)==1&&Integer.parseInt(users.branchID)==1) {

        }else {
            getBranchDataSpinner(users.branchGroup);
        }*/

    }
    private void getBranchDataSpinner(String id){
        String sql_branch="";
        if(Integer.parseInt(users.branchGroup)==1&&Integer.parseInt(users.branchID)==1) {
            sql_branch = "SELECT b.BranchID,b.BranchCode,b.BranchNameTH,bg.BranchGroupName,bg.BranchGroupID from tb_branch b left join tb_branchgroup bg on b.BranchGroupID=bg.BranchGroupID where bg.BranchGroupID='" + id + "'";

            Cursor cursor = mDb.rawQuery(sql_branch, null);
            items_branch=new ArrayList<>();
            items_branch.add(new CustomItemBranchName(""+0,""+0,""+0,"--เลือกสาขา--",""+0));
            while (cursor.moveToNext()) {
                items_branch.add(new CustomItemBranchName(cursor.getString(0),cursor.getString(4),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            }
            myAdapterSpinnerBranch = new MyAdapterSpinnerBranch(getActivity(), 0,
                    items_branch);
            spinner_name.setAdapter(myAdapterSpinnerBranch);

            spinner_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){

                    }else {
                        boolean isExist = isExist(items_branch.get(position).mBranchID);
                        if (!isExist) {
                            items_check.add(items_branch.get(position).mBranchID);
                            items.add(new CustomItemSearchBranch(items_branch.get(position).mBranchID,
                                    items_branch.get(position).mBranchGroupID,
                                    items_branch.get(position).mBranchName, items_branch.get(position).mBranchGroup));
                            serRCV(items);
                        } else {
                            Snackbar.make(myView, "ข้อมูลลูกค้านี้ถูกเพิ่มแล้ว", Snackbar.LENGTH_SHORT).show();
                           /* items_check.add(items_branch.get(position).mBranchID);
                            items.add(new CustomItemSearchBranch(items_branch.get(position).mBranchID,
                                    items_branch.get(position).mBranchName, items_branch.get(position).mBranchGroup));
                            serRCV(items);*/
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else{
            sql_branch = "SELECT b.BranchID,b.BranchCode,b.BranchNameTH,bg.BranchGroupName,bg.BranchGroupID from tb_branch b left join tb_branchgroup bg on b.BranchGroupID=bg.BranchGroupID where bg.BranchGroupID='" + id + "' AND b.BranchID='"+users.branchID+"'";
            Cursor cursor = mDb.rawQuery(sql_branch, null);
            items_branch=new ArrayList<>();
            items_branch.add(new CustomItemBranchName(""+0,""+0,""+0,"--เลือกสาขา--",""+0));
            //items_branch.add(new CustomItemBranchName(""+1,"เลือกสาขาทั้งหมด"));
            while (cursor.moveToNext()) {
                items_branch.add(new CustomItemBranchName(cursor.getString(0),cursor.getString(4),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            }
            myAdapterSpinnerBranch = new MyAdapterSpinnerBranch(getActivity(), 0,
                    items_branch);
            spinner_name.setAdapter(myAdapterSpinnerBranch);

            spinner_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position==0){

                    }else {
                        boolean isExist = isExist(items_branch.get(position).mBranchID);
                        if (!isExist) {
                            items_check.add(items_branch.get(position).mBranchID);
                            items.add(new CustomItemSearchBranch(items_branch.get(position).mBranchID,
                                    items_branch.get(position).mBranchGroupID,
                                    items_branch.get(position).mBranchName, items_branch.get(position).mBranchGroup));
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
    }
    private void serRCV(ArrayList<CustomItemSearchBranch>items){
        myAdapter = new MyAdapterSearchBranch(getActivity(), items);
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

