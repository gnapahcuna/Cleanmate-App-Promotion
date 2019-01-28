package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import CustomItem.CustomItemBranchName;
import anucha.techlogn.promotionapp.R;

public class MyAdapterSpinnerBranch extends ArrayAdapter<CustomItemBranchName> {
    private Context mContext;
    private ArrayList<CustomItemBranchName> listState;
    private MyAdapterSpinnerBranch myAdapter;
    private String getID;

    public MyAdapterSpinnerBranch(Context context, int resource, List<CustomItemBranchName> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<CustomItemBranchName>) objects;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final MyAdapterSpinnerBranch.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item_1, null);
            holder = new MyAdapterSpinnerBranch.ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (MyAdapterSpinnerBranch.ViewHolder) convertView.getTag();
        }
        try {
            holder.mTextView.setText(listState.get(position).mBranchName);
        }catch (Exception e){}
        // To check weather checked event fire from getview() or user input
        /*holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,listState.get(position).getTitle(),Toast.LENGTH_SHORT).show();
            }
        });*/

        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
