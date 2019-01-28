package CustomItem;

public class CustomItemProduct {
      public String mProductID;
      public String mProductNameTH;
      public String mProductNameEN;
      public String mProductPrice;
      public String mServiceType;
      public String mCategoryID;

      public CustomItemProduct(String id, String nameTH, String nameEN, String price,String ser,String cate) {
            mProductID=id ;
            mProductNameTH=nameTH;
            mProductNameEN=nameEN;
            mProductPrice=price;
            mServiceType=ser;
            mCategoryID=cate;
      }
}

