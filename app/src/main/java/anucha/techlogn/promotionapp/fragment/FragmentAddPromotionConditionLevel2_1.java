package anucha.techlogn.promotionapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
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
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Adapter.MyAdapterSearchCust;
import CustomItem.CustomItemSearchCust;
import anucha.techlogn.promotionapp.GetIPAPI;
import anucha.techlogn.promotionapp.R;
import anucha.techlogn.promotionapp.SQLiteHelper;

public class FragmentAddPromotionConditionLevel2_1 extends Fragment{

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private Button btn_back,btn_save;
    ImageView img_add;
    private EditText searchText;
    private RecyclerView rcv;
    private ScrollView score;
    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    private ArrayAdapter adapterData,adapterData1;
    private MyAdapterSearchCust myAdapter;
    private ArrayList<CustomItemSearchCust>items;
    private ArrayList items_check;
    private ProgressDialog dialog;
    private GetIPAPI getIPAPI;
    private SharedPreferences sp_cust;
    private ArrayList<String> arrCustID,arrCustName,arrCustPhone;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_1_condition_level2_1, mContainer, false);

        mSQLite = SQLiteHelper.getInstance(getActivity());
        getIPAPI =new GetIPAPI();
        sp_cust = getContext().getSharedPreferences("Customer", Context.MODE_PRIVATE);

        initView(myView);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                if(items.size()!=0) {
                    for (int i = 0; i < myAdapter.getItemCount(); i++) {
                        //System.out.println(myAdapter.mDataID.size() + " : " + myAdapter.mDataID.get(i));
                        System.out.println(items.get(i).mID+", "+items.get(i).mName+", "+items.get(i).mPhone);
                    }
                    sp_cust.edit().clear().apply();
                    SharedPreferences.Editor editor_product = sp_cust.edit();
                    HashSet<String> mSet1 = new HashSet<>();
                    for (int i = 0; i < myAdapter.getItemCount(); i++) {
                        int id = Integer.parseInt("" + myAdapter.mDataID.get(i));
                        mSet1.add("<a>" + items.get(i).mID);
                        mSet1.add("<b>" + items.get(i).mName);
                        mSet1.add("<c>" + items.get(i).mPhone);
                    }
                    editor_product.putStringSet("CustomerID", mSet1);
                    editor_product.apply();

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
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchText.getText().toString().isEmpty()){
                    Snackbar.make(myView,"ไม่มีข้อมูลในการค้นหา",Snackbar.LENGTH_SHORT).show();
                }else {
                    String search = searchText.getText().toString();
                    String url = getIPAPI.IPAddress + "/addpromotion/Customer.php?phone=" + search;
                    new MyAsyncTask().execute(url);
                }

            }
        });
        searchText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchText.setImeActionLabel("ค้นหา", EditorInfo.IME_ACTION_SEARCH);
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(searchText.getText().toString().isEmpty()){
                        Snackbar.make(myView,"ไม่มีข้อมูลในการค้นหา",Snackbar.LENGTH_SHORT).show();
                    }else {
                        String search = searchText.getText().toString();
                        String url = getIPAPI.IPAddress + "/addpromotion/Customer.php?phone=" + search;
                        new MyAsyncTask().execute(url);
                    }

                    return true;
                }
                return false;
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
        arrCustID=new ArrayList();
        arrCustName=new ArrayList();
        arrCustPhone=new ArrayList();


        Map<String, ?> entries2 = sp_cust.getAll();
        Set<String> keys2 = entries2.keySet();
        String[] getData2;
        List<String> list2 = new ArrayList<String>(keys2);
        for (String temp : list2) {
            System.out.println(temp+" = "+sp_cust.getStringSet(temp,null));
            for (int i = 0; i < sp_cust.getStringSet(temp, null).size(); i++) {
                getData2 = sp_cust.getStringSet(temp, null).toArray(new String[sp_cust.getStringSet(temp, null).size()]);
                //System.out.println(temp + " : " + getData2[i]);
                char chk = getData2[i].charAt(1);
                if (chk == 'a') {
                    arrCustID.add(getData2[i].substring(3));
                }else if(chk == 'b'){
                    arrCustName.add(getData2[i].substring(3));
                } else if(chk == 'c'){
                    arrCustPhone.add(getData2[i].substring(3));
                }
            }
        }
        for(int i=0;i<arrCustID.size();i++){
            boolean isExist = isExist(arrCustID.get(i));
            if (!isExist) {
                items_check.add(arrCustID.get(i));
                items.add(new CustomItemSearchCust(arrCustID.get(i),
                        arrCustName.get(i), arrCustPhone.get(i)));
            }else{
                Snackbar.make(myView,"ข้อมูลลูกค้านี้ถูกเพิ่มแล้ว",Snackbar.LENGTH_SHORT).show();
            }
        }
        myAdapter = new MyAdapterSearchCust(getActivity(), items);
        rcv.setAdapter(myAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //init view
    private void initView(View view){
        btn_back=view.findViewById(R.id.btn_back_level2);
        btn_save=view.findViewById(R.id.btn_save_cust);
        score=view.findViewById(R.id.score_level_2_1);
        searchText=view.findViewById(R.id.searchViewCust);
        img_add=view.findViewById(R.id.img_add_cust);
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
        String sql_branch = "SELECT BranchNameTH FROM tb_branch ";
        Cursor cursor = mDb.rawQuery(sql_branch, null);
        ArrayList<String> arr_branch=new ArrayList<>();
        arr_branch.add("--เลือกสาขา--");
        while (cursor.moveToNext()) {
            arr_branch.add(cursor.getString(0) );
        }
        adapterData1= new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, arr_branch);

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
            String response = "";
            try {
                URL url = new URL(strings[0]);
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
            String output = "";
            try {
                if(response.equals("#1")){
                    output=response;
                }else {
                    output=response;
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        boolean isExist = isExist(jsonObj.getString("CustomerID"));
                        if (!isExist) {
                            items_check.add(jsonObj.getString("CustomerID"));
                            items.add(new CustomItemSearchCust(jsonObj.getString("CustomerID"),
                                    jsonObj.getString("FirstName") + " " + jsonObj.getString("LastName"),
                                    jsonObj.getString("TelephoneNo")));
                        }else{
                            output="Dupicate";
                        }

                    }
                }
            } catch (Exception ex) {
                System.out.println("Error21 : " + ex);
            }

            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s.equals("#1")){
                Snackbar.make(myView, "ไม่พบข้อมูลลูกค้า", Snackbar.LENGTH_LONG).show();
            }else if(s.equals("Dupicate")) {
                Snackbar.make(myView, "ข้อมูลลูกค้านี้ถูกเพิ่มแล้ว", Snackbar.LENGTH_LONG).show();
            }else {
                searchText.setText("");
                myAdapter = new MyAdapterSearchCust(getActivity(), items);
                rcv.setAdapter(myAdapter);
                rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }
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

