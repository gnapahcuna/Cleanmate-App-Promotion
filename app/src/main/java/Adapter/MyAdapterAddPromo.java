package Adapter;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import CustomItem.CustomItemAddPromo;
import CustomItem.CustomItemSection;
import FethData.DataUserLogin;
import ViewHolder.ViewHolderAddPromo;
import ViewHolder.ViewHolderSection;
import anucha.techlogn.promotionapp.GetIPAPI;
import anucha.techlogn.promotionapp.SQLiteHelper;
import anucha.techlogn.promotionapp.fragment.FragmentAddPromotionCondition;
import anucha.techlogn.promotionapp.fragment.FragmentAddPromotionConditionLevel1;
import anucha.techlogn.promotionapp.fragment.FragmentAddPromotionConditionLevel2;
import anucha.techlogn.promotionapp.R;

public class MyAdapterAddPromo extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList mItems;
    private final int SECTION_ITEM = 0;
    private final int CHILD_ITEM = 1;
    private boolean mIsFirstChild =  true;
    private LayoutInflater inflater;
    private SharedPreferences sp;
    private ProgressDialog dialog;
    private boolean IsResponse;
    private String msgResponse;
    private String mPromotionID;
    private GetIPAPI getIPAPI;
    private DataUserLogin users;

    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;

    public MyAdapterAddPromo(Context context, ArrayList items) {
        mContext = context;
        mItems = items;
        sp = context.getSharedPreferences("Standard", Context.MODE_PRIVATE);
        getIPAPI=new GetIPAPI();
        users=new DataUserLogin(mContext);
        mSQLite=SQLiteHelper.getInstance(mContext);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        /*LayoutInflater inflater = LayoutInflater.from(mContext);
        final RecyclerView.ViewHolder vHolder;
        View v = inflater.inflate(R.layout.fragment_1_layout, parent, false);
        vHolder = new ViewHolderHome(v);
        return vHolder;*/
        final RecyclerView.ViewHolder vHolder;
        if(viewType == SECTION_ITEM) {
            View v = inflater.inflate(R.layout.section_layout, parent, false);
            vHolder = new ViewHolderSection(v);
            return vHolder;
        } else if(viewType == CHILD_ITEM) {
            View v = inflater.inflate(R.layout.frag1_list_promo_item, parent, false);
            vHolder = new ViewHolderAddPromo(v);
            return vHolder;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {
        int type = getItemViewType(position);
        if(type == SECTION_ITEM) {
            CustomItemSection item = (CustomItemSection) mItems.get(position);
            ViewHolderSection secHolder = (ViewHolderSection) vHolder;
            secHolder.textView.setText(item.sectionText);
            mIsFirstChild = true;
        } else if(type == CHILD_ITEM) {
            final CustomItemAddPromo item = (CustomItemAddPromo) mItems.get(position);
            ViewHolderAddPromo secHolder = (ViewHolderAddPromo) vHolder;
            //secHolder.Score.getLayoutParams().width=(mContext.getResources().getDisplayMetrics().widthPixels*70)/100;
            secHolder.textPromotion.setText(item.mPromotionName);
            secHolder.textExam.setText("ชื่อเต็ม : " + item.mExam);
            secHolder.imgAddPromo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPromotionID=item.mPromotionID;
                    mDb = mSQLite.getReadableDatabase();
                    String sql_service = "SELECT distinct pd.ServiceType,pd.CategoryID,pd.ProductID FROM tb_promotion p left join tb_promotionDetail pd on p.PromotionID=pd.PromotionID where p.PromotionID='"+item.mPromotionID+"'";
                    Cursor cursor = mDb.rawQuery(sql_service, null);

                    JsonArray jsonArray = new JsonArray();
                    JsonObject jsonObject = new JsonObject();
                    String url="";
                    if(cursor.getCount()==1) {
                        if (cursor.moveToNext()) {
                            String serviceType = cursor.getString(0);
                            String categoryID = cursor.getString(1);
                            String productIID = cursor.getString(2);
                            System.out.println(serviceType + " , " + categoryID + " , " + productIID);
                            if (categoryID.isEmpty() && productIID.isEmpty()) {
                                jsonObject.addProperty("ServiceType", serviceType);
                                jsonObject.addProperty("BranchID", users.branchID);
                                jsonObject.addProperty("BranchGroupID", users.branchGroup);
                                jsonArray.add(jsonObject);
                                url = getIPAPI.IPAddress + "/check/check1.php";
                                new MyAsyncTask().execute(url, String.valueOf(jsonArray));
                            } else if (!categoryID.isEmpty() && productIID.isEmpty()) {
                                jsonObject.addProperty("ServiceType", serviceType);
                                jsonObject.addProperty("CategoryID", categoryID);
                                jsonObject.addProperty("BranchID", users.branchID);
                                jsonObject.addProperty("BranchGroupID", users.branchGroup);
                                jsonArray.add(jsonObject);
                                url = getIPAPI.IPAddress + "/check/check2.php";
                                new MyAsyncTask().execute(url, String.valueOf(jsonArray));
                            }
                            //System.out.println(cursor.getString(0)+", "+cursor.getString(1)+", "+cursor.getString(2));
                        }
                    }else{
                        while (cursor.moveToNext()) {
                            String serviceType = cursor.getString(0);
                            String categoryID = cursor.getString(1);
                            String productIID = cursor.getString(2);
                            System.out.println(serviceType + " , " + categoryID + " , " + productIID);
                            if (!categoryID.isEmpty() && !productIID.isEmpty()){
                                jsonObject.addProperty("ServiceType", serviceType);
                                jsonObject.addProperty("CategoryID", categoryID);
                                jsonObject.addProperty("ProductID", productIID);
                                jsonObject.addProperty("BranchID", users.branchID);
                                jsonObject.addProperty("BranchGroupID", users.branchGroup);
                                jsonArray.add(jsonObject);
                                url = getIPAPI.IPAddress + "/check/check3.php";
                            }
                        }
                        new MyAsyncTask().execute(url, String.valueOf(jsonArray));
                    }

                    //current
                    /*SharedPreferences.Editor editor_pro_name = sp.edit();
                    editor_pro_name.putBoolean("keys", true);
                    editor_pro_name.putString("PromotionID", item.mPromotionID);
                    editor_pro_name.apply();

                    FragmentTransaction transaction = ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction();
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel2()).commit();*/
                }
            });

            boolean isLastOfSection = position < mItems.size() - 1 && getItemViewType(position + 1) == SECTION_ITEM;  //รายการสุดท้ายของกลุ่ม
            boolean isLastOfAll = position == mItems.size() - 1;   //รายการสุดท้ายของทั้งหมด
            boolean isLastChild = isLastOfSection || isLastOfAll;
            if(mIsFirstChild && isLastChild) {   //กรณีที่มีเพียงรายการเดียวในกลุ่ม ให้โค้งทั้ง 4 มุม
                secHolder.textPromotion.getRootView().setBackgroundResource(R.drawable.one_item_state);
                mIsFirstChild = false;
            } else if(mIsFirstChild || position == 0) {   //position == 0 คือเผื่อกรณีที่ให้รายการแรกสุดเป็น Child Item เลย โดยไม่ได้เริ่มจากการสร้าง Section ขึ้นมาก่อน
                secHolder.textPromotion.getRootView().setBackgroundResource(R.drawable.top_item_state);
                mIsFirstChild = false;
            } else if(isLastChild) {
                secHolder.textPromotion.getRootView().setBackgroundResource(R.drawable.bottom_item_state);
            }
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(mItems.get(position) instanceof CustomItemSection) {
            return SECTION_ITEM;
        } else if(mItems.get(position) instanceof CustomItemAddPromo) {
            return CHILD_ITEM;
        }
        return -1;
    }

    interface OnItemClickListener {
        void onItemClick(int _id);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
    private void getData(String id){
        Toast.makeText(mContext,id,Toast.LENGTH_SHORT).show();
        /*mDb = mSQLite.getReadableDatabase();
        String sql_service = "SELECT * FROM tb_promotionDetail where PromotionID='"+id+"'";
        Cursor cursor = mDb.rawQuery(sql_service, null);
        while (cursor.moveToNext()) {
            Toast.makeText(mContext,cursor.getString(0),Toast.LENGTH_SHORT).show();
        }*/
    }

    class MyAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(mContext);
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
                final Dialog dialog = new Dialog(mContext);
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
                SharedPreferences.Editor editor_pro_name = sp.edit();
                editor_pro_name.putBoolean("keys", true);
                editor_pro_name.putString("PromotionID", mPromotionID);
                editor_pro_name.apply();

                FragmentTransaction transaction = ((AppCompatActivity)mContext).getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel2()).commit();
            }
        }
    }

}

