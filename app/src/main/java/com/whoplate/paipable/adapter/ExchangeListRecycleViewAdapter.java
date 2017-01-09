package com.whoplate.paipable.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.networkpacket.Product;
import com.whoplate.paipable.viewHolder.ExchangeListViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ExchangeListRecycleViewAdapter extends RecyclerView.Adapter<ExchangeListViewHolder> {
    private WeakReference<XActivity> a;
    private ArrayList<Product> data;

    public ExchangeListRecycleViewAdapter(XActivity a, ArrayList<Product> data) {
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
        holder.title.setText(data.get(position).Title);
        holder.point.setText("消耗积分：" + data.get(position).Point);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
