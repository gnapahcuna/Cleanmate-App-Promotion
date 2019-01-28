package Adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import CustomItem.CustomItemBranchReport;
import CustomItem.CustomItemSection;
import ViewHolder.ViewHolderBranchReport;
import ViewHolder.ViewHolderSection;
import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.fragment.FragmentReportPromotionDetail;
import anucha.techlogn.promotionapp.R;

public class MyAdapterReportBranch extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList mItems;
    private final int SECTION_ITEM = 0;
    private final int CHILD_ITEM = 1;
    private boolean mIsFirstChild =  true;
    private DataUserLogin users;
    private View myView;

    public MyAdapterReportBranch(Context context, ArrayList items) {
        mContext = context;
        mItems = items;
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
        View v = inflater.inflate(R.layout.fragment_list_manage, parent, false);
        vHolder = new ViewHolderListManage(v);
        return vHolder;*/

        final RecyclerView.ViewHolder vHolder;
        if(viewType == SECTION_ITEM) {
            View v = inflater.inflate(R.layout.section_layout, parent, false);
            vHolder = new ViewHolderSection(v);
            return vHolder;
        } else if(viewType == CHILD_ITEM) {
            myView = inflater.inflate(R.layout.list_report_branch, parent, false);
            vHolder = new ViewHolderBranchReport(myView);
            return vHolder;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder vHolder, final int position) {
        int type = getItemViewType(position);
        if (type == SECTION_ITEM) {
            CustomItemSection item = (CustomItemSection) mItems.get(position);
            ViewHolderSection secHolder = (ViewHolderSection) vHolder;
            secHolder.textView.setText(item.sectionText);
            mIsFirstChild = true;
        } else if (type == CHILD_ITEM) {
            final CustomItemBranchReport item = (CustomItemBranchReport) mItems.get(position);
            final ViewHolderBranchReport secHolder = (ViewHolderBranchReport) vHolder;

            secHolder.textBranch.setText(item.mBranch);
            secHolder.textNum.setText("⬤ ใช้ไปแล้ว "+getFormatedAmount(Integer.parseInt(item.mNum))+" ครั้ง");
            secHolder.textDiscount.setText("⬤ ยอดรวมส่วนลด "+getFormatedAmount((int)Double.parseDouble(item.mTotal))+" ฿");
            boolean isLastOfSection = position < mItems.size() - 1 && getItemViewType(position + 1) == SECTION_ITEM;  //รายการสุดท้ายของกลุ่ม
            boolean isLastOfAll = position == mItems.size() - 1;   //รายการสุดท้ายของทั้งหมด
            boolean isLastChild = isLastOfSection || isLastOfAll;

            if (mIsFirstChild && isLastChild) {   //กรณีที่มีเพียงรายการเดียวในกลุ่ม ให้โค้งทั้ง 4 มุม
                secHolder.textBranch.getRootView().setBackgroundResource(R.drawable.one_item_state);
                mIsFirstChild = false;
            } else if (mIsFirstChild || position == 0) {   //position == 0 คือเผื่อกรณีที่ให้รายการแรกสุดเป็น Child Item เลย โดยไม่ได้เริ่มจากการสร้าง Section ขึ้นมาก่อน
                secHolder.textBranch.getRootView().setBackgroundResource(R.drawable.top_item_state);
                mIsFirstChild = false;
            } else if (isLastChild) {
                secHolder.textBranch.getRootView().setBackgroundResource(R.drawable.bottom_item_state);
            }
            secHolder.list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("PromotionID", item.mProID);
                    bundle.putString("PromotionName", item.mProName);
                    bundle.putString("branchID", item.mBranchID);
                    bundle.putString("branchName", item.mBranch);
                    FragmentReportPromotionDetail fragmentReportPromotionDetail = new FragmentReportPromotionDetail();
                    fragmentReportPromotionDetail.setArguments(bundle);
                    FragmentTransaction transaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                    if (false) {
                        transaction.addToBackStack(null);
                    }
                    transaction.replace(R.id.container, fragmentReportPromotionDetail).commit();

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mItems.get(position) instanceof CustomItemSection) {
            return SECTION_ITEM;
        } else if(mItems.get(position) instanceof CustomItemBranchReport) {
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
}

