package ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import anucha.techlogn.promotionapp.R;

/**
 * Created by anucha on 2/16/2018.
 */

public class ViewHolderBranchReport extends RecyclerView.ViewHolder {

    public TextView textBranch;
    public TextView textNum;
    public TextView textDiscount;
    public RelativeLayout list;
    //public ImageView imgManage;

    public ViewHolderBranchReport(View convertView) {
        super(convertView);
        textBranch = convertView.findViewById(R.id.textNameBranch);
        textNum = convertView.findViewById(R.id.textNum);
        textDiscount = convertView.findViewById(R.id.textDiscount);
        list=convertView.findViewById(R.id.list);
    }
}
