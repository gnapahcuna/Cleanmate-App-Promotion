package anucha.techlogn.promotionapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import FethData.DataPromotion;
import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.fragment.FragmentAddPromotion;
import anucha.techlogn.promotionapp.fragment.FragmentHomePromotion;
import anucha.techlogn.promotionapp.fragment.FragmentIsPromotion;
import anucha.techlogn.promotionapp.fragment.FragmentManagePromotion;
import anucha.techlogn.promotionapp.fragment.FragmentReportPromotion;
import anucha.techlogn.promotionapp.GetIPAPI;
import Adapter.MenuAdapter;
import anucha.techlogn.promotionapp.R;
import anucha.techlogn.promotionapp.SQLiteHelper;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

import static maes.tech.intentanim.CustomIntent.customType;

public class MainActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private  ProgressDialog dialog;
    //type request
    private String type;
    //data user login
    DataUserLogin users;
    //private String ID, branchID, firstname, latname, branchGroup, sTitle, title, branchName,roleDesc;
    private GetIPAPI getIPAPI;
    private TextView text_title,text_subtitle,text_subtitle1;
    private ArrayList<String> mTitles = new ArrayList<>();

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        DataPromotion dataPromotion=new DataPromotion(MainActivity.this);
        dataPromotion.initProname();
        dataPromotion.initProDetail();
        dataPromotion.initProDiscount();

        initClass();
        initView();

        //getDataUserLogin();
        users = new DataUserLogin(MainActivity.this);
        //init sqlite db
        mSQLite = SQLiteHelper.getInstance(MainActivity.this);

        setHeader();

        handleToolbar();
        handleMenu();
        handleDrawer();

        goToFragment(new FragmentHomePromotion(), false);
        //goToFragment(new FragmentAddPromotionCondition(), false);
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));

        loadData();
    }
    private void initClass(){
        getIPAPI=new GetIPAPI();
        mViewHolder = new ViewHolder();
    }
    private void initView(){
        text_title = findViewById(R.id.duo_view_header_text_title);
        text_subtitle = findViewById(R.id.duo_view_header_text_sub_title);
        text_subtitle1 = findViewById(R.id.duo_view_header_text_sub_title2);
    }
    private void setHeader(){
        text_title.setText(users.firstname+" "+users.latname);
        text_subtitle.setText(users.branchName);
        text_subtitle1.setText("\t("+users.roleDesc+")");
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
    }

    @Override
    public void onFooterClicked() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custon_alert_dialog);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView title = dialog.findViewById(R.id.tv_quit_learning);
        TextView des = dialog.findViewById(R.id.tv_description);
        title.setText("ยืนยัน");
        des.setText("ต้องการออกจากระบบ?");
        Button okButton = dialog.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String url = getIPAPI.IPAddress+"/Logout.php?IsSignOn=" + 0 + "&id=" + users.ID;
                new MyAsyncTask().execute("logout",url);
            }
        });
        Button declineButton = dialog.findViewById(R.id.btn_cancel);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onHeaderClicked() {
        //Toast.makeText(this, "onHeaderClicked", Toast.LENGTH_SHORT).show();
    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.container, fragment).commit();
    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);

        // Navigate to the right fragment
        switch (position) {
            case 0 :
                goToFragment(new FragmentHomePromotion(), false);
                break;
            case 1 :
                goToFragment(new FragmentAddPromotion(), false);
                break;
            case 2 :
                goToFragment(new FragmentManagePromotion(), false);
                break;
            case 3 :
                goToFragment(new FragmentIsPromotion(), false);
                break;
            case 4 :
                goToFragment(new FragmentReportPromotion(), false);
                break;
        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
    }
    private void insertProduct(String productID, String productNameTH,String productNameEN,
                               String productPrice, String imageFile,String colorCode,
                               String serviceType,String categoryID) {
        ContentValues cv=new ContentValues();
        cv.put("ProductID",productID);
        cv.put("ProductNameTH",productNameTH);
        cv.put("ProductNameEN",productNameEN);
        cv.put("ProductPrice",productPrice);
        cv.put("ImageFile",imageFile);
        cv.put("ColorCode",colorCode);
        cv.put("ServiceType",serviceType);
        cv.put("CategoryID",categoryID);
        mDb.insert("tb_product",null,cv);
        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void insertCategory(String categoryID, String categoryNameTH,String categoryNameEN,String colorCode) {
        ContentValues cv=new ContentValues();
        cv.put("CategoryID",categoryID);
        cv.put("CategoryNameTH",categoryNameTH);
        cv.put("CategoryNameEN",categoryNameEN);
        cv.put("ColorCode",colorCode);
        mDb.insert("tb_category",null,cv);
        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void insertService(String serviceType, String serviceNameTH,String serviceNameEN,String imageFile) {
        ContentValues cv=new ContentValues();
        cv.put("ServiceType",serviceType);
        cv.put("ServiceNameTH",serviceNameTH);
        cv.put("ServiceNameEN",serviceNameEN);
        cv.put("ImageFile",imageFile);
        mDb.insert("tb_service",null,cv);
        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void insertBranch(String branchID,String branchCode, String branchNameTH,String branchNameEN,String branchGroupID) {
        ContentValues cv=new ContentValues();
        cv.put("BranchID",branchID);
        cv.put("BranchCode",branchCode);
        cv.put("BranchNameTH",branchNameTH);
        cv.put("BranchNameEN",branchNameEN);
        cv.put("BranchGroupID",branchGroupID);
        mDb.insert("tb_branch",null,cv);
        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void insertBranchGroup(String branchGroupID, String branchGroupName) {
        ContentValues cv=new ContentValues();
        cv.put("BranchGroupID",branchGroupID);
        cv.put("BranchGroupName",branchGroupName);
        mDb.insert("tb_branchgroup",null,cv);
        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void insertCustomer(String customerID, String customerType,String memberTypeID,String title,String firstname,String lastname,String phone) {
        ContentValues cv=new ContentValues();
        cv.put("CustomerID",customerID);
        cv.put("CustomerType",customerType);
        cv.put("MemberTypeID",memberTypeID);
        cv.put("TitleName",title);
        cv.put("FirstName",firstname);
        cv.put("LastName",lastname);
        cv.put("TelephoneNo",phone);
        mDb.insert("tb_customer",null,cv);
        //showMessage("บันทึกข้อมูลแล้ว");
    }
    private void loadData(){
        mDb = mSQLite.getReadableDatabase();
        System.out.println(users.branchGroup);
        String url=getIPAPI.IPAddress+"/home/Service.php?BranchGroupID="+users.branchGroup;
        String url1=getIPAPI.IPAddress+"/home/Category.php";
        String url2=getIPAPI.IPAddress+"/home/Product.php?BranchGroupID="+users.branchGroup;
        String url3=getIPAPI.IPAddress+"/home/Branch.php?BranchGroupID="+users.branchGroup;
        String url4=getIPAPI.IPAddress+"/home/BranchGroup.php?BranchGroupID="+users.branchGroup;
        //String url4=getIPAPI.IPAddress+"/home/Customer.php";
        new MyAsyncTask().execute("data",url,url1,url2,url3,url4);

    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
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
            String response1 = "";
            String response2 = "";
            String response3 = "";
            String response4 = "";
            String response5 = "";

            String output = "";
            System.out.println(strings[0]);
            if (strings[0].equals("output")) {
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

                if (strings[0].equals("logout")) {
                    type = strings[0];
                }
            }else{
                type = strings[0];
                try {
                    URL url1 = new URL(strings[1]);
                    URL url2 = new URL(strings[2]);
                    URL url3 = new URL(strings[3]);
                    URL url4 = new URL(strings[4]);
                    URL url5 = new URL(strings[5]);

                    HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                    HttpURLConnection httpURLConnection2 = (HttpURLConnection) url2.openConnection();
                    HttpURLConnection httpURLConnection3 = (HttpURLConnection) url3.openConnection();
                    HttpURLConnection httpURLConnection4 = (HttpURLConnection) url4.openConnection();
                    HttpURLConnection httpURLConnection5 = (HttpURLConnection) url5.openConnection();

                    httpURLConnection1.setDoOutput(true);
                    httpURLConnection2.setDoOutput(true);
                    httpURLConnection3.setDoOutput(true);
                    httpURLConnection4.setDoOutput(true);
                    httpURLConnection5.setDoOutput(true);
                    httpURLConnection1.connect();
                    httpURLConnection2.connect();
                    httpURLConnection3.connect();
                    httpURLConnection4.connect();
                    httpURLConnection5.connect();

                    InputStream inputStream1 = httpURLConnection1.getInputStream();
                    InputStream inputStream2 = httpURLConnection2.getInputStream();
                    InputStream inputStream3 = httpURLConnection3.getInputStream();
                    InputStream inputStream4 = httpURLConnection4.getInputStream();
                    InputStream inputStream5 = httpURLConnection5.getInputStream();

                    Scanner scanner1 = new Scanner(inputStream1, "UTF-8");
                    Scanner scanner2 = new Scanner(inputStream2, "UTF-8");
                    Scanner scanner3 = new Scanner(inputStream3, "UTF-8");
                    Scanner scanner4 = new Scanner(inputStream4, "UTF-8");
                    Scanner scanner5 = new Scanner(inputStream5, "UTF-8");

                    response1 = scanner1.useDelimiter("\\A").next();
                    response2 = scanner2.useDelimiter("\\A").next();
                    response3 = scanner3.useDelimiter("\\A").next();
                    response4 = scanner4.useDelimiter("\\A").next();
                    response5 = scanner5.useDelimiter("\\A").next();

                } catch (Exception ex) {
                    System.out.println("Error3 : "+ex.getMessage());
                }
                try {
                    JSONArray jsonArray = new JSONArray(response1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        try {
                            insertService(
                                    jsonObject.optString("ServiceType"),
                                    jsonObject.optString("ServiceNameTH"),
                                    jsonObject.optString("ServiceNameEN"),
                                    jsonObject.optString("ImageFile")
                            );
                        }catch (Exception ex){
                            //test_err+=ex.getMessage();
                        }
                    }
                    JSONArray jsonArray1 = new JSONArray(response2);
                    for (int i = 0; i < jsonArray1.length(); i++) {
                        JSONObject jsonObj = jsonArray1.getJSONObject(i);
                        insertCategory(jsonObj.getString("CategoryID"),
                                jsonObj.getString("CategoryNameTH"),
                                jsonObj.getString("CategoryNameEN"),
                                jsonObj.getString("ColorCode"));
                    }
                    JSONArray jsonArray2 = new JSONArray(response3);
                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject jsonObj = jsonArray2.getJSONObject(i);
                        //System.out.println(jsonObj.getString("ProductID"));
                        insertProduct(
                                jsonObj.getString("ProductID"),
                                jsonObj.getString("ProductNameTH"),
                                jsonObj.getString("ProductNameEN"),
                                jsonObj.getString("ProductPrice"),
                                jsonObj.getString("ImageFile"),
                                jsonObj.getString("ColorCode"),
                                jsonObj.getString("ServiceType"),
                                jsonObj.getString("CategoryID")
                        );
                    }
                    JSONArray jsonArray4 = new JSONArray(response4);
                    for (int i = 0; i < jsonArray4.length(); i++) {
                        JSONObject jsonObj = jsonArray4.getJSONObject(i);
                        insertBranch(
                                jsonObj.getString("BranchID"),
                                jsonObj.getString("BranchCode"),
                                jsonObj.getString("BranchNameTH"),
                                jsonObj.getString("BranchNameEN"),
                                jsonObj.getString("BranchGroupID")
                        );
                    }
                    JSONArray jsonArray5 = new JSONArray(response5);
                    for (int i = 0; i < jsonArray5.length(); i++) {
                        JSONObject jsonObj = jsonArray5.getJSONObject(i);
                        insertBranchGroup(
                                jsonObj.getString("BranchGroupID"),
                                jsonObj.getString("BranchGroupName")
                        );
                    }

                    /*JSONArray jsonArray5 = new JSONArray(response5);
                    for (int i = 0; i < jsonArray5.length(); i++) {
                        JSONObject jsonObj = jsonArray5.getJSONObject(i);
                        insertCustomer(
                                jsonObj.getString("CustomerID"),
                                jsonObj.getString("CustomerType"),
                                jsonObj.getString("MemberTypeID"),
                                jsonObj.getString("TitleName"),
                                jsonObj.getString("FirstName"),
                                jsonObj.getString("LastName"),
                                jsonObj.getString("TelephoneNo")
                        );
                    }*/

                } catch (Exception ex) {
                    System.out.println("Error2 : "+ex.getMessage());
                }
            }
            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(type.equals("logout")) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                customType(MainActivity.this, "fadein-to-fadeout");
                finish();
            }else{

            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.custon_alert_dialog);
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            TextView title = dialog.findViewById(R.id.tv_quit_learning);
            TextView des = dialog.findViewById(R.id.tv_description);
            title.setText("ยืนยัน");
            des.setText("ต้องการออกจากระบบ?");
            Button okButton = dialog.findViewById(R.id.btn_ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    String url = getIPAPI.IPAddress+"/Logout.php?IsSignOn=" + 0 + "&id=" + users.ID;
                    new MyAsyncTask().execute("logout",url);
                }
            });
            Button declineButton = dialog.findViewById(R.id.btn_cancel);
            declineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
