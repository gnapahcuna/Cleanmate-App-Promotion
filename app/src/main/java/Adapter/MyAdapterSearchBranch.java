package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import CustomItem.CustomItemSearchBranch;
import ViewHolder.ViewHolderSearchBranch;
import anucha.techlogn.promotionapp.R;

public class MyAdapterSearchBranch extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList mItems;
    public ArrayList mDataID;

    public MyAdapterSearchBranch(Context context, ArrayList items) {
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
        View v = inflater.inflate(R.layout.list_search_branch, parent, false);
        vHolder = new ViewHolderSearchBranch(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {
        final CustomItemSearchBranch item = (CustomItemSearchBranch) mItems.get(position);
        ViewHolderSearchBranch secHolder = (ViewHolderSearchBranch) vHolder;
        secHolder.textBranchName.setText(""+item.mBranchName);
        secHolder.textBranchID.setText("Code : "+item.mBranchID+"\t\t Branch Group : "+item.mBranchGroup);
        secHolder.img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
                mDataID.remove(item.mBranchID);
            }
        });
        mDataID.add(item.mBranchID);

        if(position == 0) {   //position == 0 คือเผื่อกรณีที่ให้รายการแรกสุดเป็น Child Item เลย โดยไม่ได้เริ่มจากการสร้าง Section ขึ้นมาก่อน
            secHolder.textBranchName.getRootView().setBackgroundResource(R.drawable.top_item_state);
        } else if(position == mItems.size()-1) {
            secHolder.textBranchName.getRootView().setBackgroundResource(R.drawable.bottom_item_state);
        }
    }


    public void removeAt(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());
    }
}

