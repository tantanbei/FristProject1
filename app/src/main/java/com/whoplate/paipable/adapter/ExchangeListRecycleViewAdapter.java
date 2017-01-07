package com.whoplate.paipable.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.viewHolder.ExchangeListViewHolder;

import java.lang.ref.Reference;

public class ExchangeListRecycleViewAdapter extends RecyclerView.Adapter<ExchangeListViewHolder> {
    private Reference<XActivity> a;

    public ExchangeListRecycleViewAdapter() {

    }

    @Override
    public ExchangeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ExchangeListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
