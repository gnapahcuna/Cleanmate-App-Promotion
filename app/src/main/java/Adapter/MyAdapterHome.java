package Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import CustomItem.CustomItemHistory;
import CustomItem.CustomItemHome;
import CustomItem.CustomItemSection;
import ViewHolder.ViewHolderHome;
import ViewHolder.ViewHolderListHistory;
import ViewHolder.ViewHolderSection;
import anucha.techlogn.promotionapp.R;

public class MyAdapterHome extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList mItems;
    private final int SECTION_ITEM = 0;
    private final int CHILD_ITEM = 1;
    private boolean mIsFirstChild =  true;


    public MyAdapterHome(Context context, ArrayList items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*LayoutInflater inflater = LayoutInflater.from(mContext);
        final RecyclerView.ViewHolder vHolder;
        View v = inflater.inflate(R.layout.frag_list_item, parent, false);
        vHolder = new ViewHolderHome(v);
        return vHolder;*/
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final RecyclerView.ViewHolder vHolder;
        if(viewType == SECTION_ITEM) {
            View v = inflater.inflate(R.layout.section_layout, parent, false);
            vHolder = new ViewHolderSection(v);
            return vHolder;
        } else if(viewType == CHILD_ITEM) {
            View v = inflater.inflate(R.layout.frag_list_item, parent, false);
            vHolder = new ViewHolderHome(v);
            return vHolder;
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {
        int type = getItemViewType(position);
        if (type == SECTION_ITEM) {
            CustomItemSection item = (CustomItemSection) mItems.get(position);
            ViewHolderSection secHolder = (ViewHolderSection) vHolder;
            secHolder.textView.setText(item.sectionText);
            mIsFirstChild = true;
        } else if (type == CHILD_ITEM) {
            final CustomItemHome item = (CustomItemHome) mItems.get(position);
            ViewHolderHome secHolder = (ViewHolderHome) vHolder;
            String name;
            if (item.nPromotionName.length() > 20) {
                name = item.nPromotionName.substring(0, 20) + "...";
            } else {
                name = item.nPromotionName;
            }
            secHolder.textPromotion.setText(name);
            if (item.mActive.equals("Non-Active")) {
                secHolder.textActive.setTextColor(mContext.getColor(R.color.color_non_active));
                secHolder.imgIsActive.setVisibility(View.VISIBLE);
                secHolder.imgIsActive.setBackground(mContext.getResources().getDrawable(R.drawable.icon_non_active));
                secHolder.textDateEnd.setTextColor(mContext.getColor(R.color.color_non_active));
                secHolder.textDateStart.setTextColor(mContext.getColor(R.color.color_second_text));
                //secHolder.textEnd.setTextColor(mContext.getColor(R.color.color_selector));
            }else if(item.mActive.equals("Planning")) {
                secHolder.textActive.setTextColor(mContext.getColor(R.color.color_planning));
                secHolder.imgIsActive.setVisibility(View.VISIBLE);
                secHolder.imgIsActive.setBackground(mContext.getResources().getDrawable(R.drawable.icon_planning));

                secHolder.textDateEnd.setTextColor(mContext.getColor(R.color.color_second_text));
                secHolder.textDateStart.setTextColor(mContext.getColor(R.color.color_planning));
            }else {
                secHolder.textActive.setTextColor(mContext.getColor(R.color.color_button));
                secHolder.imgIsActive.setBackground(mContext.getResources().getDrawable(R.drawable.icon_active));
                secHolder.imgIsActive.setVisibility(View.VISIBLE);
                //secHolder.textEnd.setTextColor(mContext.getColor(R.color.color_second_text));

                secHolder.textDateEnd.setTextColor(mContext.getColor(R.color.color_second_text));
                secHolder.textDateStart.setTextColor(mContext.getColor(R.color.color_second_text));
            }
            secHolder.textNum.setText("⬤ ใช้ไปแล้ว " + getFormatedAmount(Integer.parseInt(item.mNum)) + " ครั้ง");
            secHolder.textTotal.setText("⬤ ยอดรวมส่วนลด " + getFormatedAmount((int) Double.parseDouble(item.mTotal)) + " ฿");

            if(item.mTimeStart.equals("-")){
                secHolder.textTimeStart.setVisibility(View.GONE);
                secHolder.textTimeEnd.setVisibility(View.GONE);
            }else{
                secHolder.textTimeStart.setVisibility(View.VISIBLE);
                secHolder.textTimeEnd.setVisibility(View.VISIBLE);
            }

            secHolder.textActive.setText(item.mActive);
            secHolder.textDateStart.setText("วันที่เริ่มใช้ : " + item.mDateStart);
            secHolder.textDateEnd.setText("วันที่สิ้นสุด : " + item.mDateEnd);
            secHolder.textTimeStart.setText("เวลาเริ่มใช้ : " + item.mTimeStart);
            secHolder.textTimeEnd.setText("เวลาสิ้นสุด : " + item.mTimeEnd);
            secHolder.textCreateBy.setText("สร้างโดย : " + item.mCreateBy);

            /*secHolder.list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg="";
                    if (item.mActive.equals("Non-Active")) {
                        msg="โปรโมชั่นนี้สิ้นสุดระยะเวลาการใช้งานแล้ว";
                        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
                    }else if (item.mActive.equals("Non-Active")) {
                        msg="โปรโมชั่นนี้ถูกสร้างไว้ล่วงหน้าแต่ยังไม่ถึงกำหนดการใช้งาน";
                        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
                    }

                }
            });
*/
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
        } else if(mItems.get(position) instanceof CustomItemHome) {
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

