package CustomItem;

public class CustomItemHistory {
      public int mPromotionID;
      public String mPromotionName;
      public String mDateStart;
      public String mDateEnd;
      public String mTimeStart;
      public String mTimeEnd;
      public String mCreateBy;
      public String mBranchID;

      public CustomItemHistory(int id, String promotion_name, String dstart, String dend, String tstart, String tend, String createBy, String branchID) {
            mPromotionID=id;
            mPromotionName=promotion_name ;
            mDateStart=dstart ;
            mDateEnd=dend;
            mTimeStart=tstart ;
            mTimeEnd=tend;
            mCreateBy=createBy;
            mBranchID=branchID;
      }
}

