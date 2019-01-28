package anucha.techlogn.promotionapp.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import Adapter.MyAdapterHome;
import CustomItem.CustomItemHome;
import CustomItem.CustomItemSection;
import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.GetIPAPI;
import anucha.techlogn.promotionapp.R;


public class FragmentHomePromotion extends Fragment {

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private ProgressDialog dialog;
    private ViewPager mPager;
    private GetIPAPI getIPAPI;
    private DataUserLogin users;
    private TextView textWelcome,textName,textBranch;
    private RecyclerView rcv;
    private MyAdapterHome myAdapter;
    ArrayList items;
    private  String dates_current;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_home_layout, mContainer, false);


        items=new ArrayList<>();
        getIPAPI=new GetIPAPI();
        users=new DataUserLogin(getActivity());

        initView(myView);
        setDetail();

        return myView;
    }
    private void initView(View view){
        textWelcome=view.findViewById(R.id.textTitleHome);
        textName=view.findViewById(R.id.textName);
        textBranch=view.findViewById(R.id.textDescHome);
        rcv=view.findViewById(R.id.rcv);
    }
    private void setDetail(){
        textName.setText(users.firstname+" "+users.latname);
        textBranch.setText("สาขา"+users.branchName);

        //load data
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        dates_current = "" + df.format("yyyy-MM-dd", new java.util.Date());
        //dates_current="2025-01/12";

        String url=getIPAPI.IPAddress+"/home/data.php?branchID="+users.branchID+"&dates="+dates_current+"&branchGroupID="+users.branchGroup;
        new MyAsyncTask().execute(url);
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
                JSONArray jsonArray = new JSONArray(response);
                String active="",lastActive="",str="";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);
                    JSONObject jsonObject1 = new JSONObject(jsonObj.getString("EffectiveDate"));
                    JSONObject jsonObject2 = new JSONObject(jsonObj.getString("ExpirationDate"));

                    String sTimeStart,sTimeEnd;
                    if(!jsonObj.getString("TimeStart").equals("null")) {
                        JSONObject jsonObjectTimeStart = new JSONObject(jsonObj.getString("TimeStart"));
                        JSONObject jsonObjectTimeEnd = new JSONObject(jsonObj.getString("TimeEnd"));
                        String[]splits=jsonObjectTimeStart.getString("date").split(" ");
                        sTimeStart=splits[1].substring(0,5)+" น.";
                        String[]splits1=jsonObjectTimeEnd.getString("date").split(" ");
                        sTimeEnd=splits1[1].substring(0,5)+" น.";
                    }else{
                        sTimeStart="-";
                        sTimeEnd="-";
                    }

                    String[]splits=jsonObject1.getString("date").split(" ");
                    String start=splits[0];
                    String[]splits1=jsonObject2.getString("date").split(" ");
                    String end=splits1[0];
                    String createBy="";
                    if(jsonObj.getString("CreatedBy").equals(""+0)){
                        createBy="ไม่ได้กำหนด";
                    }else{
                        createBy=jsonObj.getString("CreatedBy");
                    }
                    active = jsonObj.getString("BranchNameTH");
                    if (!active.trim().equals(lastActive)) {
                        str = jsonObj.getString("BranchNameTH");
                        items.add(new CustomItemSection(str));
                        lastActive = active;
                    }
                    items.add(new CustomItemHome(jsonObj.getString("PromotionName"),jsonObj.getString("IsActives"),
                            start,end,sTimeStart,sTimeEnd,createBy,jsonObj.getString("Num"),jsonObj.getString("Total")));

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

            myAdapter = new MyAdapterHome(getActivity(),items);
            rcv.setAdapter(myAdapter);
            rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}

