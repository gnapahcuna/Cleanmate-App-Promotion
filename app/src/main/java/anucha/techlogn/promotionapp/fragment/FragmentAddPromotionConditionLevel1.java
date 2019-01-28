package anucha.techlogn.promotionapp.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import anucha.techlogn.promotionapp.R;

public class FragmentAddPromotionConditionLevel1 extends Fragment{

    private View myView;
    private LayoutInflater mLayoutInflater;
    private ViewGroup mContainer;
    private SharedPreferences sp_condition,sp_standard;
    private RadioButton rdb1,rdb2,rdb3,rdb4,rdb5,rdb6,rdb7;
    private EditText edt_condi_num,edt_condi_price,edt_condi_no,edt_discount_price,edt_discount_rate;
    private Button btn_back,btn_next;
    private RelativeLayout group1,group2,group3,group4,group5;
    private ScrollView score;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutInflater = inflater;
        mContainer = container;
        myView = mLayoutInflater.inflate(R.layout.fragment_1_condition_level1, mContainer, false);

        initView(myView);


        isCondition();
        isDistcount();
        isDiscountType();
        initSaveData();
        getDataPage();

        if(sp_standard.getBoolean("keys",false)==true){
            btn_back.setText("แก้ไขเพิ่มเติม");
            //btn_back.setVisibility(View.GONE);
        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to level0
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                if (false) {
                    transaction.addToBackStack(null);
                }
                transaction.replace(R.id.container, new FragmentAddPromotionCondition()).commit();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataPage();
            }
        });

        return myView;
    }
    //set dialog
    private void setDialog(String sTitle,String sDesc){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custon_alert_dialog);
        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView title = dialog.findViewById(R.id.tv_quit_learning);
        TextView des = dialog.findViewById(R.id.tv_description);
        title.setText(sTitle);
        des.setText(sDesc);
        Button declineButton = dialog.findViewById(R.id.btn_cancel);
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //declineButton.setVisibility(View.GONE);
        Button okButton = dialog.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    //init sharepref
    private void initSaveData(){
        sp_condition = getContext().getSharedPreferences("DataCondition1", Context.MODE_PRIVATE);
        sp_standard = getContext().getSharedPreferences("Standard", Context.MODE_PRIVATE);
    }
    //init view
    private void initView(View view){
        rdb1 = view.findViewById(R.id.rdbCondi1);
        rdb2 = view.findViewById(R.id.rdbCondi2);
        rdb3=view.findViewById(R.id.rdbCondi3);
        rdb4=view.findViewById(R.id.rdbDiscount1);
        rdb5=view.findViewById(R.id.rdbDiscount2);
        rdb6=view.findViewById(R.id.rdbDiscountPrice);
        rdb7=view.findViewById(R.id.rdbDiscountRate);
        edt_condi_num=view.findViewById(R.id.edt_condi_num);
        edt_condi_price=view.findViewById(R.id.edt_condi_price);
        btn_back=view.findViewById(R.id.btn_back_level0);
        btn_next=view.findViewById(R.id.btn_next_condition_level1);
        group1=view.findViewById(R.id.group_condi1_level1);
        group2=view.findViewById(R.id.group_condi2_level1);
        group3=view.findViewById(R.id.group_condi3_level1);
        group4=view.findViewById(R.id.group_discount1);
        group5=view.findViewById(R.id.group_discount2);
        score=view.findViewById(R.id.score_condi_level1);
        edt_condi_no=view.findViewById(R.id.edt_condi_no);
        edt_discount_price=view.findViewById(R.id.edt_discount_price);
        edt_discount_rate=view.findViewById(R.id.edt_discount_rate);

        group1.setVisibility(View.GONE);
        group2.setVisibility(View.GONE);
        group3.setVisibility(View.GONE);
        group5.setVisibility(View.GONE);
        rdb3.setChecked(true);
        rdb4.setChecked(true);
        rdb6.setChecked(true);

        int height=getActivity().getResources().getDisplayMetrics().heightPixels;
        score.getLayoutParams().height=(height*70)/100;
    }
    private void isCondition(){
        /*RadioButton[]rb=new RadioButton[]{rdb1,rdb2,rdb3};
        for (RadioButton r:rb
             ) {
            r.setOnCheckedChangeListener(this);
        }*/
        rdb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                   group1.setVisibility(View.VISIBLE);
                   group2.setVisibility(View.GONE);
                }
            }
        });
        rdb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    group1.setVisibility(View.GONE);
                    group2.setVisibility(View.VISIBLE);
                }
            }
        });
        rdb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    group1.setVisibility(View.GONE);
                    group2.setVisibility(View.GONE);
                }
            }
        });
    }
    private void isDistcount(){
        rdb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group3.setVisibility(View.VISIBLE);
            }
        });
        rdb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group3.setVisibility(View.GONE);
            }
        });
    }
    private void isDiscountType(){
        //rate
        rdb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group5.setVisibility(View.VISIBLE);
                group4.setVisibility(View.GONE);
                //Toast.makeText(getActivity(),buttonView.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        //price
        rdb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                group4.setVisibility(View.VISIBLE);
                group5.setVisibility(View.GONE);
                //Toast.makeText(getActivity(),buttonView.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataPage(){
        String conditionType=sp_condition.getString("ConditionType","");
        String conditionAmount=sp_condition.getString("ConditionAmount","");
        String conditionPrice=sp_condition.getString("ConditionPrice","");
        String discountType=sp_condition.getString("DiscountType","");
        String discountProductNo=sp_condition.getString("DiscountProductNo","");
        String discountPriceType=sp_condition.getString("DiscountPriceType","");
        String discountPrice=sp_condition.getString("DiscountPrice","");
        String discountRate=sp_condition.getString("DiscountRate","");
        System.out.println(discountPriceType+", "+discountPrice+", "+discountRate);

        if(conditionType.equals(""+1)){
            rdb1.setChecked(true);
            group1.setVisibility(View.VISIBLE);
            edt_condi_num.setText(conditionAmount);
            group2.setVisibility(View.GONE);
        }else if(conditionType.equals(""+2)){
            rdb2.setChecked(true);
            group1.setVisibility(View.GONE);
            edt_condi_price.setText(conditionPrice);
            group2.setVisibility(View.VISIBLE);
        }else if(conditionType.equals("")){
            rdb3.setChecked(true);
            group1.setVisibility(View.GONE);
            group2.setVisibility(View.GONE);
        }
        if(discountType.equals(""+1)){
            group3.setVisibility(View.GONE);
        }else if(discountType.equals(""+2)){
            rdb5.setChecked(true);
            group3.setVisibility(View.VISIBLE);
            edt_condi_no.setText(discountProductNo);
        }else if(discountType.equals("")){
            group3.setVisibility(View.GONE);
        }

        if(discountPriceType.equals(""+1)){
            rdb6.setChecked(true);
            group4.setVisibility(View.VISIBLE);
            edt_discount_price.setText(discountPrice);
        }else if(discountPriceType.equals(""+2)){
            rdb7.setChecked(true);
            group5.setVisibility(View.VISIBLE);
            edt_discount_rate.setText(discountRate);
        }

    }

    private void setDataPage() {
        if(checkEmpty()==true){
            Snackbar.make(myView,"กรุณากรอกข้อมูลให้ครบก่อน",Snackbar.LENGTH_SHORT).show();
        }else {
            String conditionType="";
            String conditionAmount="";
            String conditionPrice="";
            String discountType="";
            String discountProductNo="";
            String discountPrice="";
            String discountRate="";
            String discountPriceType="";
            if(rdb1.isChecked()==true){
                conditionType=""+1;
                conditionAmount=edt_condi_num.getText().toString();
            }else if(rdb2.isChecked()==true){
                conditionType=""+2;
                conditionPrice=edt_condi_price.getText().toString();
            }else if(rdb3.isChecked()==true){
                conditionType=null;
            }

            if(rdb4.isChecked()==true){
                discountType=""+1;
            }else if(rdb5.isChecked()==true){
                discountType=""+2;
                discountProductNo=edt_condi_no.getText().toString();
            }

            if(rdb6.isChecked()==true){
                discountPriceType=""+1;
                discountPrice=edt_discount_price.getText().toString();
            }else if(rdb7.isChecked()==true){
                discountPriceType=""+2;
                discountRate=edt_discount_rate.getText().toString();
            }
            SharedPreferences.Editor editor = sp_condition.edit();
            editor.putString("ConditionType",conditionType);
            editor.putString("ConditionAmount",conditionAmount);
            editor.putString("ConditionPrice",conditionPrice);
            editor.putString("DiscountType",discountType);
            editor.putString("DiscountProductNo",discountProductNo);
            editor.putString("DiscountPrice",discountPrice);
            editor.putString("DiscountRate",discountRate);
            editor.putString("DiscountPriceType",discountPriceType);
            editor.apply();

            //got to level1
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if (false) {
                transaction.addToBackStack(null);
            }
            transaction.replace(R.id.container, new FragmentAddPromotionConditionLevel2()).commit();
        }
    }
    //check empty text
    private boolean checkEmpty(){
        System.out.println(rdb5.isChecked());
        if(rdb1.isChecked()==true&&edt_condi_num.getText().toString().isEmpty()){
            return true;
        }else if(rdb2.isChecked()==true&&edt_condi_price.getText().toString().isEmpty()){
            return true;
        }else if(rdb5.isChecked()==true&&edt_condi_no.getText().toString().isEmpty()){
            return true;
        }else if(rdb6.isChecked()==true&&edt_discount_price.getText().toString().isEmpty()){
            return true;
        }else if(rdb7.isChecked()==true&&edt_discount_rate.getText().toString().isEmpty()){
            return true;
        }else{
            return false;
        }
    }

}

