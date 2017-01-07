package com.whoplate.paipable.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whoplate.paipable.R;

public class ExchangeListViewHolder extends RecyclerView.ViewHolder{
    public TextView exchange;

    public ExchangeListViewHolder(View itemView) {
        super(itemView);

        exchange = (TextView) itemView.findViewById(R.id.title);
    }
}
