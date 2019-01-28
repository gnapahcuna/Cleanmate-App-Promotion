package ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import anucha.techlogn.promotionapp.R;

/**
 * Created by anucha on 2/16/2018.
 */
public class ViewHolderSearchCust extends RecyclerView.ViewHolder {

    public TextView textName;
    public TextView textPhone;
    public ImageView imgDel;

    public ViewHolderSearchCust(View convertView) {
        super(convertView);
        textName = convertView.findViewById(R.id.textNameCust);
        textPhone = convertView.findViewById(R.id.textPhone);
        imgDel = convertView.findViewById(R.id.img_delete);
    }
}
