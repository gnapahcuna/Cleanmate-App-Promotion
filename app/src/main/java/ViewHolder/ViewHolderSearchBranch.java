package ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import anucha.techlogn.promotionapp.R;

/**
 * Created by anucha on 2/16/2018.
 */
public class ViewHolderSearchBranch extends RecyclerView.ViewHolder {

    public TextView textBranchName;
    public TextView textBranchID;
    public ImageView img_del;

    public ViewHolderSearchBranch(View convertView) {
        super(convertView);
        textBranchName = convertView.findViewById(R.id.textBranchName);
        textBranchID = convertView.findViewById(R.id.textBranchID);
        img_del = convertView.findViewById(R.id.img_delete);
    }
}
