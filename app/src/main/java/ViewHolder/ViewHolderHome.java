package ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import anucha.techlogn.promotionapp.R;

/**
 * Created by anucha on 2/16/2018.
 */

public class ViewHolderHome extends RecyclerView.ViewHolder {

    public TextView textPromotion;
    public TextView textActive;
    public TextView textDateStart;
    public TextView textDateEnd;
    public TextView textTimeStart;
    public TextView textTimeEnd;
    public TextView textCreateBy;
    public TextView textNum;
    public TextView textTotal;
    public ImageView imgIsActive;
    public RelativeLayout list;

    public ViewHolderHome(View convertView) {
        super(convertView);
        textPromotion = convertView.findViewById(R.id.textNamePromo);
        textActive = convertView.findViewById(R.id.textStatus);
        textDateStart = convertView.findViewById(R.id.textDateStart);
        textDateEnd = convertView.findViewById(R.id.textDateEnd);
        textTimeStart = convertView.findViewById(R.id.textTimeStart);
        textTimeEnd = convertView.findViewById(R.id.textTimeEnd);
        textCreateBy = convertView.findViewById(R.id.textCreateBy);
        textNum = convertView.findViewById(R.id.textNumpromo);
        textTotal = convertView.findViewById(R.id.textTotalPromo);
        imgIsActive=convertView.findViewById(R.id.imageViewStatus);
        list=convertView.findViewById(R.id.list);

    }
}
