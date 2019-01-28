package FethData;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import anucha.techlogn.promotionapp.SQLiteHelper;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

import static maes.tech.intentanim.CustomIntent.customType;
public class DataPromotion {
    public ArrayList<String>proName;
    private SQLiteHelper mSQLite;
    private SQLiteDatabase mDb;
    public DataPromotion(Context context){
        mSQLite = SQLiteHelper.getInstance(context);
        //String  s="ซักน้ำเฉพาะชุดนอน,กางเกง,กระโป";
        //Toast.makeText(context,""+s.length(),Toast.LENGTH_SHORT).show();
    }
    public void initProname(){
        proName =new ArrayList<>();
        proName.add("ซักแห้งทุกประเภทลด 15%");
        proName.add("ซักแห้งเฉพาะเสื้อผ้าชายลด 15 %");
        proName.add("ซักแห้งเฉพาะสูท,กางเกง,กระโปรง หญิงลด 15%");
        proName.add("ซักน้ำครบ 3 ชิ้น ลด 30 บาท");
        proName.add("ซักแห้งพรมผ้าม่านครบ 500บาท ลด 20%");
        proName.add("ซักแห้งกระเป๋าชิ้นที่ 3 ลด 50%");
        proName.add("ซักน้ำเฉพาะชุดนอน,กางเกง,กระโปรง หญิงลด 20บาท");
        proName.add("ซักน้ำพรมผ้าม่านครบ 500บาท ลด 50 บาท");

        ArrayList<Integer> arrSer= new ArrayList<>();
        ArrayList<Integer> arrCate= new ArrayList<>();
        ArrayList<Integer> arrPro= new ArrayList<>();

        arrSer.add(1);
        arrSer.add(1);
        arrSer.add(1);
        arrSer.add(2);
        arrSer.add(1);
        arrSer.add(1);
        arrSer.add(2);
        arrSer.add(2);

        arrCate.add(1);
        arrCate.add(2);
        arrCate.add(3);
        arrCate.add(1);
        arrCate.add(7);
        arrCate.add(5);
        arrCate.add(3);
        arrCate.add(5);

        arrPro.add(1);
        arrPro.add(1);
        arrPro.add(1);
        arrPro.add(1);
        arrPro.add(1);
        arrPro.add(1);
        arrPro.add(0);
        arrPro.add(1);

        for(int i=0;i<proName.size();i++){
            addDataPromo(i+1,proName.get(i),""+1,""+arrSer.get(i),""+arrCate.get(i),""+arrPro.get(i));
        }
    }
    public void initProDetail(){
        String[] data1=new String[]{"1","","","","",""};
        String[] data2=new String[]{"1","2","","","",""};
        String[] data31=new String[]{"1","2","11","","",""};
        String[] data32=new String[]{"1","2","3","","",""};
        String[] data33=new String[]{"1","2","28","","",""};
        String[] data4=new String[]{"2","","","1","3",""};
        String[] data5=new String[]{"1","7","","2","","500"};
        String[] data6=new String[]{"1","5","","1","3",""};

        String[] data71=new String[]{"2","2","102","","",""};
        String[] data72=new String[]{"2","2","93","","",""};
        String[] data73=new String[]{"2","2","98","","",""};

        String[] data8=new String[]{"2","5","","2","","500"};

        //line1
        addDataDetail(1,1,data1[0],data1[1],data1[2],data1[3],data1[4],data1[5]);
        //line2
        addDataDetail(2,2,data2[0],data2[1],data2[2],data2[3],data2[4],data2[5]);
        //line3
        addDataDetail(3,3,data31[0],data31[1],data31[2],data31[3],data31[4],data31[5]);
        addDataDetail(4,3,data32[0],data32[1],data32[2],data32[3],data32[4],data32[5]);
        addDataDetail(5,3,data33[0],data33[1],data33[2],data33[3],data33[4],data33[5]);
        //line4
        addDataDetail(6,4,data4[0],data4[1],data4[2],data4[3],data4[4],data4[5]);
        //line5
        addDataDetail(7,5,data5[0],data5[1],data5[2],data5[3],data5[4],data5[5]);
        //line6
        addDataDetail(8,6,data6[0],data6[1],data6[2],data6[3],data6[4],data6[5]);
        //line7
        addDataDetail(9,7,data71[0],data71[1],data71[2],data71[3],data71[4],data71[5]);
        addDataDetail(10,7,data72[0],data72[1],data72[2],data72[3],data72[4],data72[5]);
        addDataDetail(11,7,data73[0],data73[1],data73[2],data73[3],data73[4],data73[5]);
        //line8
        addDataDetail(12,8,data8[0],data8[1],data8[2],data8[3],data8[4],data8[5]);

    }
    public void initProDiscount(){
        String[] data1=new String[]{"1","","2","15",""};
        String[] data2=new String[]{"1","","2","15",""};
        String[] data3=new String[]{"1","","2","15",""};
        String[] data4=new String[]{"1","","1","","30"};
        String[] data5=new String[]{"1","","2","20",""};
        String[] data6=new String[]{"2","3","2","50",""};
        String[] data7=new String[]{"1","","1","","20"};
        String[] data8=new String[]{"1","","1","","20"};


        //line1
        addDataDiscount(1,data1[0],data1[1],data1[2],data1[3],data1[4]);
        //line2
        addDataDiscount(2,data2[0],data2[1],data2[2],data2[3],data2[4]);
        //line3
        addDataDiscount(3,data3[0],data3[1],data3[2],data3[3],data3[4]);
        addDataDiscount(4,data3[0],data3[1],data3[2],data3[3],data3[4]);
        addDataDiscount(5,data3[0],data3[1],data3[2],data3[3],data3[4]);
        //line4
        addDataDiscount(6,data4[0],data4[1],data4[2],data4[3],data4[4]);
        //line5
        addDataDiscount(7,data5[0],data5[1],data5[2],data5[3],data5[4]);
        //line6
        addDataDiscount(8,data6[0],data6[1],data6[2],data6[3],data6[4]);
        //line7
        addDataDiscount(9,data7[0],data7[1],data7[2],data7[3],data7[4]);
        addDataDiscount(10,data7[0],data7[1],data7[2],data7[3],data7[4]);
        addDataDiscount(11,data7[0],data7[1],data7[2],data7[3],data7[4]);
        //line8
        addDataDiscount(12,data8[0],data8[1],data8[2],data8[3],data8[4]);

    }
    public void addDataPromo(int proID,String proName,String isActice,String selcSer,String selcCate,String selcPro){
        mDb = mSQLite.getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("PromotionID",""+proID);
        cv.put("PromotionName",proName);
        cv.put("IsActive",isActice);
        cv.put("ServiceSelect",selcSer);
        cv.put("CategorySelect",selcCate);
        cv.put("ProductSelect",selcPro);
        mDb.insert("tb_promotion",null,cv);
    }
    public void addDataDetail(int pk,int fk,String ser,String cate,String pro,String condiType,String condiAmount,String condiPrice){
        mDb = mSQLite.getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("PromotionDetailID",""+pk);
        cv.put("ServiceType",""+ser);
        cv.put("CategoryID",cate);
        cv.put("ProductID",pro);
        cv.put("ConditionType",""+condiType);
        cv.put("ConditionAmount",condiAmount);
        cv.put("ConditionPrice",condiPrice);
        cv.put("PromotionID",""+fk);
        mDb.insert("tb_promotionDetail",null,cv);
    }
    public void addDataDiscount(int fk,String disType,String disProNo,String disPriceType,String disRate,String distPrice){
        mDb = mSQLite.getReadableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("DiscountType",disType);
        cv.put("DiscountProductNo",""+disProNo);
        cv.put("DiscountPriceType",disPriceType);
        cv.put("DiscountRate",disRate);
        cv.put("DiscountPrice",""+distPrice);
        cv.put("PromotionDetailID",""+fk);
        mDb.insert("tb_promotionDiscount",null,cv);
    }
}
