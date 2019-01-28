package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import CustomItem.CustomItemSearchProduct;
import ViewHolder.ViewHolderSearchProduct;
import anucha.techlogn.promotionapp.R;

public class MyAdapterSearchProduct extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList mItems;
    public ArrayList mDataID;

    public MyAdapterSearchProduct(Context context, ArrayList items) {
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
        View v = inflater.inflate(R.layout.list_search_product, parent, false);
        vHolder = new ViewHolderSearchProduct(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, final int position) {
        final CustomItemSearchProduct item = (CustomItemSearchProduct) mItems.get(position);
        ViewHolderSearchProduct secHolder = (ViewHolderSearchProduct) vHolder;
        secHolder.textNameTH.setText(item.mProductNameTH);
        secHolder.textNameEN.setText("("+item.mProductNameEN+") ราคา : "+item.mProductPrice+"฿");
        secHolder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
                mDataID.remove(item.mProductID);
            }
        });
        mDataID.add(item.mProductID);

        if(position == 0) {   //position == 0 คือเผื่อกรณีที่ให้รายการแรกสุดเป็น Child Item เลย โดยไม่ได้เริ่มจากการสร้าง Section ขึ้นมาก่อน
            secHolder.textNameTH.getRootView().setBackgroundResource(R.drawable.top_item_state);
        } else if(position == mItems.size()-1) {
            secHolder.textNameTH.getRootView().setBackgroundResource(R.drawable.bottom_item_state);
        }
    }
    public void removeAt(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mItems.size());
    }
}

