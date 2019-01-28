package CustomItem;

public class CustomItemHome {
      public String nPromotionName;
      public String mActive;
      public String mDateStart;
      public String mDateEnd;
      public String mTimeStart;
      public String mTimeEnd;
      public String mCreateBy;
      public String mNum;
      public String mTotal;

      public CustomItemHome(String promotion_name, String active, String dstart, String dend, String tstart, String tend,String createBy,String num,String total) {
            nPromotionName=promotion_name ;
            mActive=active;
            mDateStart=dstart ;
            mDateEnd=dend;
            mTimeStart=tstart ;
            mTimeEnd=tend;
            mCreateBy=createBy;
            mNum=num;
            mTotal=total;
      }
}

