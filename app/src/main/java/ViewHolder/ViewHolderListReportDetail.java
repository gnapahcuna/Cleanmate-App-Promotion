package ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import anucha.techlogn.promotionapp.R;

/**
 * Created by anucha on 2/16/2018.
 */
public class ViewHolderListReportDetail extends RecyclerView.ViewHolder {

    public TextView textName;
    public TextView textOrder;
    public TextView textDiscount;

    public ViewHolderListReportDetail(View convertView) {
        super(convertView);
        textName = convertView.findViewById(R.id.textNameCust);
        textOrder = convertView.findViewById(R.id.textOrder);
        textDiscount = convertView.findViewById(R.id.textDiscount);
    }
}
