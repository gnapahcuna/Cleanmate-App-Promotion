package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import CustomItem.CustomItemSearchCust;
import ViewHolder.ViewHolderSearchCust;
import anucha.techlogn.promotionapp.R;

public class MyAdapterSearchCust extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList mItems;
    public ArrayList mDataID;

    public MyAdapterSearchCust(Context context, ArrayList items) {
        mContext = context;
        mItems = items;
        mDataID=new ArrayList();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final RecyclerView.ViewHolder vHolder;
        View v = inflater.inflate(R.layout.list_search_cust, parent, false);
        vHolder = new ViewHolderSearchCust(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {
        final CustomItemSearchCust item = (CustomItemSearchCust) mItems.get(position);
        ViewHolderSearchCust secHolder = (ViewHolderSearchCust) vHolder;
        secHolder.textName.setText(item.mName);
        secHolder.textPhone.setText("ID : "+item.mID+"\t\t Tel : "+item.mPhone);
        secHolder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext,""+item.mName,Toast.LENGTH_SHORT).show();
                removeAt(position);
                mDataID.remove(item.mID);
            }
        });
        mDataID.add(item.mID);

        if(position == 0) {   //position == 0 คือเผื่อกรณีที่ให้รายการแรกสุดเป็น Child Item เลย โดยไม่ได้เริ่มจากการสร้าง Section ขึ้นมาก่อน
            secHolder.textName.getRootView().setBackgroundResource(R.drawable.top_item_state);
        } else if(position == mItems.size()-1) {
            secHolder.textName.getRootView().setBackgroundResource(R.drawable.bottom_item_state);
        }
    }


    public void removeAt(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());
    }
}

