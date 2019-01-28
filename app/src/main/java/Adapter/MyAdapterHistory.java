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
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import CustomItem.CustomItemHistory;
import CustomItem.CustomItemSection;
import ViewHolder.ViewHolderListHistory;
import ViewHolder.ViewHolderSection;
import FethData.DataUserLogin;
import anucha.techlogn.promotionapp.fragment.FragmentHistoryPromotionDetail;
import anucha.techlogn.promotionapp.fragment.FragmentManagePromotionDetail;
import anucha.techlogn.promotionapp.R;

public class MyAdapterHistory extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList mItems;
    private final int SECTION_ITEM = 0;
    private final int CHILD_ITEM = 1;
    private boolean mIsFirstChild =  true;
    private DataUserLogin users;

    public MyAdapterHistory(Context context, ArrayList items) {
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
            View v = inflater.inflate(R.layout.fragment_list_history, parent, false);
            vHolder = new ViewHolderListHistory(v);
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
            final CustomItemHistory item = (CustomItemHistory) mItems.get(position);
            ViewHolderListHistory secHolder = (ViewHolderListHistory) vHolder;
            String name;
            if (item.mPromotionName.length() > 20) {
                name = item.mPromotionName.substring(0, 20) + "...";
            } else {
                name = item.mPromotionName;
            }
            if(item.mTimeStart.equals("-")){
                secHolder.textTimeStart.setVisibility(View.GONE);
                secHolder.textTimeEnd.setVisibility(View.GONE);
            }else{
                secHolder.textTimeStart.setVisibility(View.VISIBLE);
                secHolder.textTimeEnd.setVisibility(View.VISIBLE);
            }
            secHolder.textPromotion.setText(name);
            secHolder.textDateStart.setText("เริ่มใช้ : " + item.mDateStart);
            secHolder.textDateEnd.setText("สิ้นสุด : " + item.mDateEnd);
            secHolder.textTimeStart.setText("เวลาเริ่มใช้ : " + item.mTimeStart);
            secHolder.textTimeEnd.setText("เวลาสิ้นสุด : " + item.mTimeEnd);
            secHolder.textCreateBy.setText("สร้างโดย : " + item.mCreateBy);

            users=new DataUserLogin(mContext);
            if(Integer.parseInt(users.branchID)==1){
                if (Integer.parseInt(item.mBranchID) == 1) {
                    secHolder.imgManage.setEnabled(true);
                    secHolder.imgManage.setBackground(mContext.getResources().getDrawable(R.drawable.ic_settings_applications_black_24dp));

                    secHolder.imgManage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(mContext,""+item.mPromotionID,Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putInt("PromotionID", item.mPromotionID);
                            FragmentManagePromotionDetail fragmentManagePromotionDetail = new FragmentManagePromotionDetail();
                            fragmentManagePromotionDetail.setArguments(bundle);
                            FragmentTransaction transaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                            if (false) {
                                transaction.addToBackStack(null);
                            }
                            transaction.replace(R.id.container, fragmentManagePromotionDetail).commit();
                        }
                    });
                } else {
                    secHolder.imgManage.setBackground(mContext.getResources().getDrawable(R.drawable.ic_settings_applications_black_24dp2));
                    secHolder.imgManage.setEnabled(false);
                    secHolder.imgManage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "ไม่สามารถจัดการส่วนของโรงงานได้", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }else {
                if (Integer.parseInt(item.mBranchID) == 1) {
                    secHolder.imgManage.setBackground(mContext.getResources().getDrawable(R.drawable.ic_settings_applications_black_24dp2));
                    secHolder.imgManage.setEnabled(false);
                    secHolder.imgManage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "ไม่สามารถจัดการส่วนของโรงงานได้", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    secHolder.imgManage.setEnabled(true);
                    secHolder.imgManage.setBackground(mContext.getResources().getDrawable(R.drawable.ic_settings_applications_black_24dp));

                    secHolder.imgManage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(mContext,""+item.mPromotionID,Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putInt("PromotionID", item.mPromotionID);
                            FragmentHistoryPromotionDetail fragmentHistoryPromotionDetail = new FragmentHistoryPromotionDetail();
                            fragmentHistoryPromotionDetail.setArguments(bundle);
                            FragmentTransaction transaction = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                            if (false) {
                                transaction.addToBackStack(null);
                            }
                            transaction.replace(R.id.container, fragmentHistoryPromotionDetail).commit();
                        }
                    });

                }
            }

            boolean isLastOfSection = position < mItems.size() - 1 && getItemViewType(position + 1) == SECTION_ITEM;  //รายการสุดท้ายของกลุ่ม
            boolean isLastOfAll = position == mItems.size() - 1;   //รายการสุดท้ายของทั้งหมด
            boolean isLastChild = isLastOfSection || isLastOfAll;

            if (mIsFirstChild && isLastChild) {   //กรณีที่มีเพียงรายการเดียวในกลุ่ม ให้โค้งทั้ง 4 มุม
                secHolder.textPromotion.getRootView().setBackgroundResource(R.drawable.one_item_state);
                mIsFirstChild = false;
            } else if (mIsFirstChild || position == 0) {   //position == 0 คือเผื่อกรณีที่ให้รายการแรกสุดเป็น Child Item เลย โดยไม่ได้เริ่มจากการสร้าง Section ขึ้นมาก่อน
                secHolder.textPromotion.getRootView().setBackgroundResource(R.drawable.top_item_state);
                mIsFirstChild = false;
            } else if (isLastChild) {
                secHolder.textPromotion.getRootView().setBackgroundResource(R.drawable.bottom_item_state);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mItems.get(position) instanceof CustomItemSection) {
            return SECTION_ITEM;
        } else if(mItems.get(position) instanceof CustomItemHistory) {
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

