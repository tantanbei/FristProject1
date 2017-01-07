package com.whoplate.paipable.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.networkpacket.PointExchangeProduce;
import com.whoplate.paipable.viewHolder.ExchangeListViewHolder;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ExchangeListRecycleViewAdapter extends RecyclerView.Adapter<ExchangeListViewHolder> {
    private WeakReference<XActivity> a;
    private ArrayList<PointExchangeProduce> data;

    public ExchangeListRecycleViewAdapter(XActivity a, ArrayList<PointExchangeProduce> data) {
        this.a = new WeakReference<XActivity>(a);
        this.data = data;
    }

    @Override
    public ExchangeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(a.get()).inflate(R.layout.row_exchange, parent, false);
        return new ExchangeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExchangeListViewHolder holder, int position) {
        //// TODO: 07/01/2017  
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
