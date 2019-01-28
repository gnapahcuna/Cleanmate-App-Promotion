package anucha.techlogn.promotionapp.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.GetIPAPI;
import anucha.techlogn.promotionapp.R;

public class FragmentManagePromotionDetailEdit extends Fragment {

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private ProgressDialog dialog;
    ArrayList items;
    private GetIPAPI getIPAPI;
    private DataUserLogin users;
    private RadioButton rdbTime1,rdbTime2;
    private ImageView img_date_start,img_date_end,img_time_start,img_time_end;
    private EditText edt_date_start,edt_date_end,edt_time_start,edt_time_end;
    private Button btn_back,btn_next;
    private RelativeLayout group1,group2;
    private ScrollView score;
    String get_dates="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_2_layout_detail_edit, mContainer, false);

        items=new ArrayList<>();
        getIPAPI=new GetIPAPI();
        users=new DataUserLogin(getActivity());

        initView(myView);
        isTime();
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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle_get=getArguments();
                Bundle bundle_put = new Bundle();
                bundle_put.putInt("PromotionID",bundle_get.getInt("PromotionID"));
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentManagePromotionDetail fragmentManagePromotionDetail=new FragmentManagePromotionDetail();
                fragmentManagePromotionDetail.setArguments(bundle_put);
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, fragmentManagePromotionDetail).commit();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
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
                des.setText("ต้องการบันทึกการเปลี่ยนแปลงนี้?");
                Button declineButton = dialog.findViewById(R.id.btn_cancel);
                //declineButton.setVisibility(View.GONE);
                Button okButton = dialog.findViewById(R.id.btn_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(checkEmpty()==true){
                            Snackbar.make(myView,"กรุณากรอกข้อมูลให้ครบ",Snackbar.LENGTH_SHORT).show();
                        }else {
                            Bundle bundle = getArguments();
                            if (bundle != null) {
                                if (bundle.getString("TimeStart").equals("ไม่ได้กำหนด")) {
                                    String url = getIPAPI.IPAddress + "/manage/update1.php?PromotionID=" + bundle.getInt("PromotionID");
                                    url += "&DateStart=" + edt_date_start.getText().toString();
                                    url += "&DateEnd=" + edt_date_end.getText().toString();
                                    url += "&UpdateBy=" + users.ID;
                                    new MyAsyncTask().execute(url);
                                } else {
                                    String url = getIPAPI.IPAddress + "/manage/update2.php?PromotionID=" + bundle.getInt("PromotionID");
                                    url += "&DateStart=" + edt_date_start.getText().toString();
                                    url += "&DateEnd=" + edt_date_end.getText().toString();
                                    url += "&TimeStart=" + edt_time_start.getText().toString();
                                    url += "&TimeEnd=" + edt_time_end.getText().toString();
                                    url += "&UpdateBy=" + users.ID;
                                    new MyAsyncTask().execute(url);
                                }
                            }
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

        img_date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("กำหนดวันที่เริ่มใช้งานโปรโมชั่น","start");
            }
        });
        img_date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog("กำหนดวันที่สิ้นสุดใช้งานโปรโมชั่น","end");
            }
        });

        img_time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog Tp = new TimePickerDialog(getContext(),R.style.abirStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //mTimetext.setText(hourOfDay+ ":" +minute);
                        String hh,mm;
                        if(String.valueOf(hourOfDay).length()==1){
                            hh="0"+hourOfDay;
                        }else{
                            hh=""+hourOfDay;
                        }
                        if(String.valueOf(minute).length()==1){
                            mm="0"+minute;
                        }else{
                            mm=""+minute;
                        }
                        edt_time_start.setText(hh+ ":" +mm);

                    }
                },1,0,true);
                Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Tp.show();
            }
        });
        img_time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog Tp = new TimePickerDialog(getContext(),R.style.abirStyle, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //mTimetext.setText(hourOfDay+ ":" +minute);
                        String hh,mm;
                        if(String.valueOf(hourOfDay).length()==1){
                            hh="0"+hourOfDay;
                        }else{
                            hh=""+hourOfDay;
                        }
                        if(String.valueOf(minute).length()==1){
                            mm="0"+minute;
                        }else{
                            mm=""+minute;
                        }
                        edt_time_end.setText(hh+ ":" +mm);
                    }
                },1,0,true);
                Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = Tp.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                Tp.show();
            }
        });

        edt_date_start.setEnabled(true);
        edt_date_end.setEnabled(true);
        img_time_start.setEnabled(true);
        edt_time_end.setEnabled(true);

        return myView;
    }
    private boolean checkEmpty(){
        if(rdbTime2.isChecked()==true){
            if(edt_date_start.getText().toString().isEmpty()||edt_date_end.getText().toString().isEmpty()){
                return true;
            }else{
                return false;
            }
        }else if(rdbTime1.isChecked()==true){
            if(edt_time_start.getText().toString().isEmpty()||edt_time_end.getText().toString().isEmpty()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    private void initView(View view){
        btn_back=view.findViewById(R.id.btn_back_level3);
        btn_next=view.findViewById(R.id.btn_save_level3);
        score=view.findViewById(R.id.score_condi_level2);
        group1=view.findViewById(R.id.group_time_start);
        group2=view.findViewById(R.id.group_time_end);
        rdbTime1=view.findViewById(R.id.rdbTime1);
        rdbTime2=view.findViewById(R.id.rdbTime2);

        img_date_start=view.findViewById(R.id.img_date_start);
        img_date_end=view.findViewById(R.id.img_date_end);
        img_time_start=view.findViewById(R.id.img_time_start);
        img_time_end=view.findViewById(R.id.img_time_end);

        edt_date_start=view.findViewById(R.id.edt_date_start);
        edt_date_end=view.findViewById(R.id.edt_date_end);
        edt_time_start=view.findViewById(R.id.edt_time_start);
        edt_time_end=view.findViewById(R.id.edt_time_end);

        rdbTime2.setChecked(true);
        group1.setVisibility(View.GONE);
        group2.setVisibility(View.GONE);

        int height=getActivity().getResources().getDisplayMetrics().heightPixels;
        score.getLayoutParams().height=(height*70)/100;

    }
    private void setDetail(){
        Bundle bundle=getArguments();
        if(bundle!=null) {
            edt_date_start.setText(bundle.getString("DateStart"));
            edt_date_end.setText(bundle.getString("DateEnd"));
            if(bundle.getString("TimeStart").equals("ไม่ได้กำหนด")){

            }else{
                rdbTime1.setChecked(true);
                edt_time_start.setText(bundle.getString("TimeStart"));
                edt_time_end.setText(bundle.getString("TimeEnd"));
            }

        }
    }
    private void isTime(){
        rdbTime2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group1.setVisibility(View.VISIBLE);
                group2.setVisibility(View.VISIBLE);
            }
        });
        rdbTime1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group1.setVisibility(View.GONE);
                group2.setVisibility(View.GONE);
            }
        });
    }

    //set dialog
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDialog(String sTitle, final String isState){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_calendar);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
        TextView title = dialog.findViewById(R.id.tv_quit_learning);
        final CalendarView calendar = dialog.findViewById(R.id.calendarView);
        //System.out.println(System.currentTimeMillis() - 1000);
        if(isState.equals("start")) {
            calendar.setDate(Calendar.getInstance().getTimeInMillis(), false, true);
            calendar.setMinDate(System.currentTimeMillis() - 1000);
        }else if(isState.equals("end")){
            //calendar.setDate(Calendar.getInstance().getTimeInMillis(), false, true);
            try {
                String dateString = edt_date_start.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(dateString);

                long startDate = date.getTime();
                calendar.setDate(startDate, false, true);
                calendar.setMinDate(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String mm,dd;
                int m=month+1;
                if(String.valueOf(""+month).length()==1){
                    mm="0"+m;
                }else{
                    mm=""+m;
                }
                if(String.valueOf(""+dayOfMonth).length()==1){
                    dd="0"+dayOfMonth;
                }else{
                    dd=""+dayOfMonth;
                }
                get_dates=year + "-" + mm + "-" + dd;

                dialog.dismiss();
                if(isState.equals("start")) {
                    setDateStart(get_dates);
                }else if(isState.equals("end")){
                    setDateEnd(get_dates);
                }
            }
        });
        title.setText(sTitle);
        Button declineButton = dialog.findViewById(R.id.btn_cancel);
        declineButton.setVisibility(View.GONE);
        Button okButton = dialog.findViewById(R.id.btn_ok);
        okButton.setVisibility(View.GONE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(isState.equals("start")) {
                    setDateStart(get_dates);
                }else if(isState.equals("end")){
                    setDateEnd(get_dates);
                }
            }
        });

    }
    private void setDateStart(String datestart){
        edt_date_start.setText(datestart);
    }
    private void setDateEnd(String dateend){
        edt_date_end.setText(dateend);
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
                System.out.println("Error1"+ex.getMessage());
            }
            String output = "";
            System.out.println(response);

            return output;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            Snackbar.make(myView,"บันทึกรายการแล้ว",Snackbar.LENGTH_SHORT).show();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (false) {
                transaction.addToBackStack(null);
            }
            transaction.replace(R.id.container, new FragmentManagePromotion()).commit();

        }
    }
}

