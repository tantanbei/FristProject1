package com.whoplate.paipable.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.networkpacket.ProductWithImage;
import com.whoplate.paipable.viewHolder.ExchangeListViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ExchangeListRecycleViewAdapter extends RecyclerView.Adapter<ExchangeListViewHolder> {
    private WeakReference<XActivity> a;
    private ArrayList<ProductWithImage> data;

    public ExchangeListRecycleViewAdapter(XActivity a, ArrayList<ProductWithImage> data) {
        this.a = new WeakReference<XActivity>(a);
        this.data = data;
    }

    @Override
    public ExchangeListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(a.get()).inflate(R.layout.row_exchange, parent, false);
        return new ExchangeListViewHolder(view, a.get());
    }

    @Override
    public void onBindViewHolder(final ExchangeListViewHolder holder, final int position) {
        ProductWithImage productWithImage = data.get(position);

        holder.title.setText(productWithImage.Title);
        holder.point.setText("消耗积分：" + productWithImage.Point);
        holder.productId = productWithImage.ProductId;

        holder.value = data.get(position).Value;

        Glide.with(a.get()).load(Const.SERVER_IP + Const.URL_APN + Const.URL_GET_IMAGE + "?imagetype=2&imageid=" + productWithImage.ImageId).centerCrop().into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
