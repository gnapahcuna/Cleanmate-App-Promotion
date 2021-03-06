package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import anucha.techlogn.promotionapp.R;
import CustomItem.StateVOCust;

public class MyAdapterSpinnerCust extends ArrayAdapter<StateVOCust> {
    private Context mContext;
    public ArrayList<StateVOCust> listState;
    public ArrayList<Integer> listGetData;
    public ArrayList<Integer> listAllData;
    private MyAdapterSpinnerCust myAdapter;
    private boolean isFromView = false;
    private String check_str="";
    //private int mSelected;

    public MyAdapterSpinnerCust(Context context, int resource, List<StateVOCust> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<StateVOCust>) objects;
        this.myAdapter = this;
        listGetData=new ArrayList();
        listAllData=new ArrayList();
        //this.mSelected=selected;
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

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(listState.get(position).getCustomerName());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;


        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                    listGetData.add(position);
                }
                //check_str+=listState.get(position).getProductName()+",";
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
