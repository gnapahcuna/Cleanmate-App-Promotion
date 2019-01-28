package ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import anucha.techlogn.promotionapp.R;

/**
 * Created by anucha on 2/16/2018.
 */

public class ViewHolderListManage extends RecyclerView.ViewHolder {

    public TextView textPromotion;
    public TextView textActive;
    public TextView textDateStart;
    public TextView textDateEnd;
    public TextView textTimeStart;
    public TextView textTimeEnd;
    public TextView textCreateBy;
    public ImageView imgManage;

    public ViewHolderListManage(View convertView) {
        super(convertView);
        textPromotion = convertView.findViewById(R.id.textNamePromo);
        textActive = convertView.findViewById(R.id.textStatus);
        textDateStart = convertView.findViewById(R.id.textDateStart);
        textDateEnd = convertView.findViewById(R.id.textDateEnd);
        textCreateBy = convertView.findViewById(R.id.textCreateBy);
        imgManage=convertView.findViewById(R.id.img_manage);
        textTimeStart = convertView.findViewById(R.id.textTimeStart);
        textTimeEnd = convertView.findViewById(R.id.textTimeEnd);
    }
}
