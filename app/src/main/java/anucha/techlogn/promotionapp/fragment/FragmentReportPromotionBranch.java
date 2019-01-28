package anucha.techlogn.promotionapp.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import Adapter.MyAdapterReportBranch;
import CustomItem.CustomItemBranchReport;
import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.GetIPAPI;
import anucha.techlogn.promotionapp.R;

public class FragmentReportPromotionBranch extends Fragment {

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private ProgressDialog dialog;
    private RecyclerView rcv;
    private MyAdapterReportBranch myAdapter;
    ArrayList items;
    private GetIPAPI getIPAPI;
    private TextView textTitle;
    private ImageView back;
    private DataUserLogin users;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_4_layout_branch, mContainer, false);

        items=new ArrayList<>();
        getIPAPI=new GetIPAPI();
        users=new DataUserLogin(getActivity());

        initView(myView);
        setDetail();

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle bundle = new Bundle();
                Bundle bundle_get = new Bundle();
                bundle.putInt("PromotionID", bundle_get.getInt("PromotionID"));
                bundle.putInt("PromotionName", bundle_get.getInt("PromotionName"));
                FragmentReportPromotionBranch fragmentReportPromotionBranch=new FragmentReportPromotionBranch();
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, fragmentReportPromotionBranch).commit();*/
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentReportPromotion()).commit();


            }
        });

        return myView;
    }
    private void initView(View view){
        rcv=view.findViewById(R.id.rcv_list_create_promo);
        textTitle=view.findViewById(R.id.textTitleListPromo);
        back=view.findViewById(R.id.imgBack);
    }
    private void setDetail(){
        Bundle bundle=getArguments();
        if(bundle!=null){
            textTitle.setText(bundle.getString("PromotionName"));
            String url = getIPAPI.IPAddress + "/report/branch.php?PromotionID="+bundle.getInt("PromotionID");
            url+="&branchID="+users.branchID;
            url+="&branchGroupID="+users.branchGroup;
            new MyAsyncTask().execute(url);
        }
       /* //load data
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        dates_current = "" + df.format("yyyy-MM-dd", new java.util.Date());
        //dates_current="2025-01/12";
        String url = getIPAPI.IPAddress + "/home/data.php?branchID=" + users.branchID + "&dates=" + dates_current + "&branchGroupID=" + users.branchGroup;
        new MyAsyncTask().execute(url);*/

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
                String date = "";
                String lastDate = "";
                String str="";
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObj = jsonArray.getJSONObject(i);

                    Bundle bundle=getArguments();
                    items.add(new CustomItemBranchReport(bundle.getInt("PromotionID"),
                            bundle.getString("PromotionName"),jsonObj.getString("BranchNameTH"),
                            jsonObj.getString("Num"),jsonObj.getString("Total"),jsonObj.getString("BranchID")));

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

            myAdapter = new MyAdapterReportBranch(getActivity(),items);
            rcv.setAdapter(myAdapter);
            rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }
}

