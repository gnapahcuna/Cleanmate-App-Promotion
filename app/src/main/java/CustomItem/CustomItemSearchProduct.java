package CustomItem;

public class CustomItemSearchProduct {
      public String mProductID;
      public String mProductNameTH;
      public String mProductNameEN;
      public String mProductPrice;
      public String mServiceType;
      public String mCategoryID;

      public CustomItemSearchProduct(String id, String proTH, String proEN,String price, String ser,String cate) {
            mProductID=id ;
            mProductNameTH=proTH;
            mProductNameEN=proEN ;
            mProductPrice=price;
            mServiceType=ser ;
            mCategoryID=cate;
      }
}

