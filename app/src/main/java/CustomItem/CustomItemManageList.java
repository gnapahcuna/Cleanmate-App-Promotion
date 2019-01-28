package CustomItem;

public class CustomItemManageList {
      public int mPromotionID;
      public String mPromotionName;
      public String mActive;
      public String mDateStart;
      public String mDateEnd;
      public String mTimeStart;
      public String mTimeEnd;
      public String mCreateBy;
      public String mBranchID;

      public CustomItemManageList(int id,String promotion_name, String active, String dstart, String dend, String tstart, String tend, String createBy,String branchID) {
            mPromotionID=id;
            mPromotionName=promotion_name ;
            mActive=active;
            mDateStart=dstart ;
            mDateEnd=dend;
            mTimeStart=tstart ;
            mTimeEnd=tend;
            mCreateBy=createBy;
            mBranchID=branchID;
      }
}

