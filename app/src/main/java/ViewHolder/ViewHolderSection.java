package ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import anucha.techlogn.promotionapp.R;

public class ViewHolderSection extends RecyclerView.ViewHolder {

      public TextView textView;

      public ViewHolderSection(View v) {
            super(v);
            textView = (TextView)v.findViewById(R.id.textView_section);
      }
}

