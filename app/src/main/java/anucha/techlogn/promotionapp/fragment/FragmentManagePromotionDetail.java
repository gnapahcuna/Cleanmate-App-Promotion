package anucha.techlogn.promotionapp.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import Adapter.MyAdapterManageList;
import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.GetIPAPI;
import anucha.techlogn.promotionapp.R;

public class FragmentManagePromotionDetail extends Fragment {

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private ProgressDialog dialog;
    private MyAdapterManageList myAdapter;
    ArrayList items;
    private GetIPAPI getIPAPI;
    private DataUserLogin users;
    private String dates_current;
    private ImageView back;
    private Button btn_edit,btn_enable;
    private TextView tName,tService,tCate,tProduct,tCondition,tDiscountType,tDiscount,tBranchGroup,tBranch,tCust,tDateStart,tDateEnd,tTimeStart,tTimeEnd;
    private String sName,sService,sCate,sProduct,sCondition,sDiscountType,sDiscount,sBranchGroup,sBranch,sCust,sDateStart,sDateEnd,sTimeStart,sTimeEnd;
    private ArrayList<String>arrProduct,arrCate,arrCustomer,arrBranchGroup,arrBranch,arrSer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_2_layout_detail, mContainer, false);

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
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentManagePromotion()).commit();
            }
        });

        btn_enable.setOnClickListener(new View.OnClickListener() {
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
                des.setText("ต้องการปิดการใช้งานโปรโมชั่นนี้?");
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                //declineButton.setVisibility(View.GONE);
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Bundle bundle = getArguments();
                        if(bundle!=null) {
                            String url = getIPAPI.IPAddress + "/manage/enable.php?PromotionID="+bundle.getInt("PromotionID");
                            url+="&IsActive="+0;
                            url+="&DeleteBy="+users.ID;
                            new MyAsyncTask().execute("put",url);
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
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle_get = getArguments();
                Bundle bundle_put = new Bundle();
                bundle_put.putInt("PromotionID",bundle_get.getInt("PromotionID"));
                bundle_put.putString("DateStart",sDateStart);
                bundle_put.putString("DateEnd",sDateEnd);
                bundle_put.putString("TimeStart",sTimeStart);
                bundle_put.putString("TimeEnd",sTimeEnd);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentManagePromotionDetailEdit fragmentManagePromotionDetailEdit=new FragmentManagePromotionDetailEdit();
                fragmentManagePromotionDetailEdit.setArguments(bundle_put);
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, fragmentManagePromotionDetailEdit).commit();
            }
        });

        return myView;
    }
    private void initView(View view){
        back=view.findViewById(R.id.img_back);
        btn_edit=view.findViewById(R.id.buttonEdit);
        btn_enable=view.findViewById(R.id.buttonEnable);

        tName=view.findViewById(R.id.textViewName);
        tService=view.findViewById(R.id.textViewService);
        tCate=view.findViewById(R.id.textViewCategory);
        tProduct=view.findViewById(R.id.textViewProduct);
        tCondition=view.findViewById(R.id.textViewCondition);
        tDiscountType=view.findViewById(R.id.textViewDiscountType);
        tDiscount=view.findViewById(R.id.textViewDiscount);
        tBranch=view.findViewById(R.id.textViewBranch);
        tBranchGroup=view.findViewById(R.id.textViewBranchGroup);
        tCust=view.findViewById(R.id.textViewCustomer);
        tDateStart=view.findViewById(R.id.textViewDatestart);
        tDateEnd=view.findViewById(R.id.textViewDateEnd);
        tTimeStart=view.findViewById(R.id.textViewTimeStart);
        tTimeEnd=view.findViewById(R.id.textViewTimeEnd);

        arrProduct=new ArrayList<>();
        arrCate=new ArrayList<>();
        arrCustomer=new ArrayList<>();
        arrBranchGroup=new ArrayList<>();
        arrBranch=new ArrayList<>();
        arrSer=new ArrayList<>();
    }
    private void setDetail(){
        //load data
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        dates_current = "" + df.format("yyyy-MM-dd", new java.util.Date());
        //dates_current="2025-01/12";

        Bundle bundle = getArguments();
        if(bundle!=null) {
            String url = getIPAPI.IPAddress + "/manage/data.php?PromotionID="+bundle.getInt("PromotionID");
            new MyAsyncTask().execute("get",url);
        }
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
            String output = "";
            if(strings[0].equals("get")) {
                try {
                    output="get";
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
                            arrSer.add(jsonObj_pro.getString("ServiceNameTH"));
                            //sService = jsonObj_pro.getString("ServiceNameTH");
                            if (!jsonObj_pro.getString("CategoryNameTH").equals("null")) {
                                arrCate.add(jsonObj_pro.getString("CategoryNameTH"));
                            } else {
                                arrCate.add("เลือกทั้งหมด");
                            }
                            if (!jsonObj_pro.getString("ProductNameTH").equals("null")) {
                                arrProduct.add(jsonObj_pro.getString("ProductNameTH"));
                            } else {
                                arrProduct.add("เลือกทั้งหมด");
                            }
                            //conditon
                            if (jsonObj_pro.getString("ConditionType").equals("" + 1)) {
                                sCondition = "เงื่อนไขแบบจำนวนเท่ากับ " + jsonObj_pro.getString("ConditionAmount");
                            } else if (jsonObj_pro.getString("ConditionType").equals("" + 2)) {
                                sCondition = "เงื่อนไขแบบราคาเท่ากับ " + jsonObj_pro.getString("ConditionPrice");
                            } else {
                                sCondition = "ไม่ได้กำหนด";
                            }
                            //discount type
                            if (jsonObj_pro.getString("DiscountType").equals("" + 1)) {
                                sDiscountType = "ส่วนลดรวม";
                            } else if (jsonObj_pro.getString("DiscountType").equals("" + 2)) {
                                sDiscountType = "ส่วนลดรายชิ้น ชิ้นที่ " + jsonObj_pro.getString("DiscountProductNo");
                            }
                            //discount
                            if (jsonObj_pro.getString("DiscountPriceType").equals("" + 1)) {
                                sDiscount = "ส่วนลด " + jsonObj_pro.getString("DiscountPrice");
                            } else if (jsonObj_pro.getString("DiscountPriceType").equals("" + 2)) {
                                sDiscount = "ส่วนลด " + jsonObj_pro.getString("DiscountRate");
                            }
                        }
                        for (int j = 0; j < jsonArray_pro3.length(); j++) {
                            JSONObject jsonObj_pro = jsonArray_pro3.getJSONObject(j);
                            if (!jsonObj_pro.getString("CustomerName").equals("null")) {
                                arrCustomer.add(jsonObj_pro.getString("CustomerName"));
                            } else {
                                arrCustomer.add("เลือกทั้งหมด");
                            }
                        }
                        for (int j = 0; j < jsonArray_pro4.length(); j++) {
                            JSONObject jsonObj_pro = jsonArray_pro4.getJSONObject(j);
                            if (!jsonObj_pro.getString("BranchNameTH").equals("null")) {
                                arrBranch.add(jsonObj_pro.getString("BranchNameTH"));
                            } else {
                                arrBranch.add("เลือกทั้งหมด");
                            }
                            arrBranchGroup.add(jsonObj_pro.getString("BranchGroupName"));
                        }
                    }
                } catch (Exception ex) {
                    output=response;
                    System.out.println("Error21Detail : " + ex);
                }
            }else{
                System.out.println(strings[0]+", "+response);
            }

            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if(!s.trim().equals("get")){
                Snackbar.make(myView,"บันทึกรายการแล้ว",Snackbar.LENGTH_SHORT).show();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentManagePromotion()).commit();
            }else {
                sService = "";
                sCate = "";
                sProduct = "";
                sCust = "";
                sBranch = "";
                sBranchGroup = "";

                Set<String> set_ser = new HashSet<>(arrSer);
                arrSer.clear();
                arrSer.addAll(set_ser);

                Set<String> set_cate = new HashSet<>(arrCate);
                arrCate.clear();
                arrCate.addAll(set_cate);

                Set<String> set_branch = new HashSet<>(arrBranch);
                arrBranch.clear();
                arrBranch.addAll(set_branch);

                Set<String> set_branchGroup = new HashSet<>(arrBranchGroup);
                arrBranchGroup.clear();
                arrBranchGroup.addAll(set_branchGroup);

                for (int i = 0; i < arrSer.size(); i++) {
                    if (i == arrSer.size() - 1) {
                        sService += arrSer.get(i);
                    } else {
                        sService += arrSer.get(i) + ", ";
                    }
                }
                for (int i = 0; i < arrCate.size(); i++) {
                    if (i == arrCate.size() - 1) {
                        sCate += arrCate.get(i);
                    } else {
                        sCate += arrCate.get(i) + ", ";
                    }
                }
                for (int i = 0; i < arrProduct.size(); i++) {
                    if (i == arrProduct.size() - 1) {
                        sProduct += arrProduct.get(i);
                    } else {
                        sProduct += arrProduct.get(i) + ", ";
                    }
                }
                for (int i = 0; i < arrCustomer.size(); i++) {
                    if (i == arrCustomer.size() - 1) {
                        sCust += arrCustomer.get(i);
                    } else {
                        sCust += arrCustomer.get(i) + ", ";
                    }
                }
                for (int i = 0; i < arrBranch.size(); i++) {
                    if (i == arrBranch.size() - 1) {
                        sBranch += arrBranch.get(i);
                    } else {
                        sBranch += arrBranch.get(i) + ", ";
                    }
                }
                for (int i = 0; i < arrBranchGroup.size(); i++) {
                    if (i == arrBranchGroup.size() - 1) {
                        sBranchGroup += arrBranchGroup.get(i);
                    } else {
                        sBranchGroup += arrBranchGroup.get(i) + ", ";
                    }
                }

                tName.setText(sName);
                tService.setText(sService);
                tCate.setText(sCate);
                tProduct.setText(sProduct);

                tCondition.setText(sCondition);
                tDiscountType.setText(sDiscountType);
                tDiscount.setText(sDiscount);

                tCust.setText(sCust);
                tBranch.setText(sBranch);
                tBranchGroup.setText(sBranchGroup);

                tDateStart.setText(sDateStart);
                tDateEnd.setText(sDateEnd);
                tTimeStart.setText(sTimeStart);
                tTimeEnd.setText(sTimeEnd);


            }

        }
    }
}

