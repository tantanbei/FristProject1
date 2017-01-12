package com.whoplate.paipable.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.OkPacket;
import com.whoplate.paipable.networkpacket.Product;
import com.whoplate.paipable.networkpacket.SignInBack;
import com.whoplate.paipable.string.XString;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.util.XDebug;
import com.whoplate.paipable.viewHolder.ExchangeListViewHolder;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.Response;

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
        return new ExchangeListViewHolder(view, a.get());
    }

    @Override
    public void onBindViewHolder(final ExchangeListViewHolder holder, final int position) {
        holder.title.setText(data.get(position).Title);
        holder.point.setText("消耗积分：" + data.get(position).Point);
        holder.productId = data.get(position).ProductId;

        holder.value = data.get(position).Value;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
