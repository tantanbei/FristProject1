package com.whoplate.paipable.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whoplate.paipable.R;

public class PaperListViewHolder extends RecyclerView.ViewHolder {
    public View root;
    public TextView title;
    public TextView date;

    public PaperListViewHolder(View itemView) {
        super(itemView);

        root = itemView;
        title = (TextView) itemView.findViewById(R.id.paper_title);
        date = (TextView) itemView.findViewById(R.id.date);
    }
}