package ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import anucha.techlogn.promotionapp.R;

/**
 * Created by anucha on 2/16/2018.
 */

public class ViewHolderAddPromo extends RecyclerView.ViewHolder {

    public TextView textPromotion;
    public TextView textExam;
    public ImageView imgAddPromo;
    public RelativeLayout back;
    public CardView card;
    //public HorizontalScrollView Score;

    public ViewHolderAddPromo(View convertView) {
        super(convertView);
        textPromotion = convertView.findViewById(R.id.textNamePromo);
        textExam = convertView.findViewById(R.id.textExam);
        imgAddPromo = convertView.findViewById(R.id.img_add_promo);
        //Score = convertView.findViewById(R.id.score);
    }
}
