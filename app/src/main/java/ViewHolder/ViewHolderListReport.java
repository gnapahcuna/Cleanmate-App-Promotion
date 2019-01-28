package ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import anucha.techlogn.promotionapp.R;

/**
 * Created by anucha on 2/16/2018.
 */

public class ViewHolderListReport extends RecyclerView.ViewHolder {

    public TextView textPromotion;
    public TextView textDateStart;
    public TextView textDateEnd;
    public TextView textTimeStart;
    public TextView textTimeEnd;
    public TextView textCreateBy;
    public TextView textNum;
    public TextView textTotal;
    public RelativeLayout list;
    //public ImageView imgManage;

    public ViewHolderListReport(View convertView) {
        super(convertView);
        textPromotion = convertView.findViewById(R.id.textNamePromo);
        textDateStart = convertView.findViewById(R.id.textDateStart);
        textDateEnd = convertView.findViewById(R.id.textDateEnd);
        textCreateBy = convertView.findViewById(R.id.textCreateBy);
        textNum=convertView.findViewById(R.id.textNumpromo);
        textTotal=convertView.findViewById(R.id.textTotalPromo);
        list=convertView.findViewById(R.id.list);
        textTimeStart = convertView.findViewById(R.id.textTimeStart);
        textTimeEnd = convertView.findViewById(R.id.textTimeEnd);
        //imgManage=convertView.findViewById(R.id.img_manage);
    }
}
