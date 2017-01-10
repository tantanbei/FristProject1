package com.whoplate.paipable.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whoplate.paipable.R;

public class ExchangeListViewHolder extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView point;
    public TextView submit;

    public String value;
    public int productId;

    public ExchangeListViewHolder(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.title);
        point = (TextView) itemView.findViewById(R.id.point);
        submit = (TextView) itemView.findViewById(R.id.submit);
    }
}
