package ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import anucha.techlogn.promotionapp.R;

/**
 * Created by anucha on 2/16/2018.
 */
public class ViewHolderSearchProduct extends RecyclerView.ViewHolder {

    public TextView textNameTH;
    public TextView textNameEN;
    public ImageView imgDel;

    public ViewHolderSearchProduct(View convertView) {
        super(convertView);
        textNameTH = convertView.findViewById(R.id.textNameTH);
        textNameEN = convertView.findViewById(R.id.textNameEN);
        imgDel = convertView.findViewById(R.id.img_delete);
    }
}
