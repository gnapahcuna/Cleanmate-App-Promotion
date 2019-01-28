package anucha.techlogn.promotionapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anucha on 3/7/2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper sqLiteDB;
    private static final String DB_NAME = "dbase_promo";
    private static final int DB_VERSION = 2;
    private static Context mContext;
    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public static synchronized SQLiteHelper getInstance(Context context) {
        //Toast.makeText(context,""+sqLiteDB,Toast.LENGTH_SHORT).show();
        mContext=context;
        if(sqLiteDB == null) {
            sqLiteDB = new SQLiteHelper(context.getApplicationContext());
        }
        return sqLiteDB;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql= "CREATE TABLE tb_product (ProductID INTEGER PRIMARY KEY,"+
                        "ProductNameTH,"+
                        "ProductNameEN,"+
                        "ProductPrice,"+
                        "ImageFile,"+
                        "ColorCode,"+
                        "ServiceType,"+
                        "CategoryID)";
        db.execSQL(sql);


        sql="CREATE TABLE tb_service (ServiceType INTEGER PRIMARY KEY,"+
                "ServiceNameTH,"+
                "ServiceNameEN," +
                "ImageFile)";
        db.execSQL(sql);


        sql="CREATE TABLE tb_category (CategoryID PRIMARY KEY,"+
                "CategoryNameTH,"+
                "CategoryNameEN,"+
                "ColorCode)";
        db.execSQL(sql);

        sql="CREATE TABLE tb_branch (BranchID INTEGER PRIMARY KEY,"+
                "BranchCode,"+
                "BranchNameTH,"+
                "BranchNameEN,"+
                "BranchGroupID)";
        db.execSQL(sql);

        sql="CREATE TABLE tb_branchgroup (BranchGroupID INTEGER PRIMARY KEY,"+
                "BranchGroupName)";
        db.execSQL(sql);

        sql="CREATE TABLE tb_promotion (PromotionID INTEGER PRIMARY KEY,"+
                "PromotionName,"+
                "ServiceSelect,"+
                "CategorySelect,"+
                "ProductSelect,"+
                "IsActive)";
        db.execSQL(sql);

        sql="CREATE TABLE tb_promotionDetail (PromotionDetailID INTEGER PRIMARY KEY,"+
                "ServiceType,"+
                "CategoryID,"+
                "ProductID,"+
                "ConditionType," +
                "ConditionAmount," +
                "ConditionPrice," +
                "PromotionID)";
        db.execSQL(sql);

        sql="CREATE TABLE tb_promotionDiscount (DiscountID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "DiscountType,"+
                "DiscountProductNo,"+
                "DiscountPriceType,"+
                "DiscountRate," +
                "DiscountPrice," +
                "PromotionDetailID)";
        db.execSQL(sql);
        /*sql="CREATE TABLE tb_customer (CustomerID INTEGER PRIMARY KEY,"+
                "CustomerType,"+
                "MemberTypeID,"+
                "TitleName,"+
                "FirstName,"+
                "LastName," +
                "TelephoneNo)";
        db.execSQL(sql);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Toast.makeText(mContext,""+oldVersion+" ,"+newVersion,Toast.LENGTH_SHORT).show();
        if (newVersion > oldVersion) {
            /*String sql="CREATE TABLE tb_customer (CustomerID INTEGER PRIMARY KEY,"+
                    "CustomerType,"+
                    "MemberTypeID,"+
                    "TitleName,"+
                    "FirstName,"+
                    "LastName," +
                    "TelephoneNo)";
            db.execSQL(sql);*/
        }
    }
}
