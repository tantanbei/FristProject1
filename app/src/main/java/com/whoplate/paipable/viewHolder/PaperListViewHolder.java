package com.whoplate.paipable.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.whoplate.paipable.R;

import org.w3c.dom.Text;

public class PaperListViewHolder extends RecyclerView.ViewHolder {
    public View root;
    public ImageView image;
    public TextView title;
    public TextView date;
    public TextView reprint;

    public PaperListViewHolder(View itemView) {
        super(itemView);

        root = itemView;
        image = (ImageView) itemView.findViewById(R.id.image);
        title = (TextView) itemView.findViewById(R.id.paper_title);
        date = (TextView) itemView.findViewById(R.id.date);
        reprint = (TextView) itemView.findViewById(R.id.reprint);
    }
}