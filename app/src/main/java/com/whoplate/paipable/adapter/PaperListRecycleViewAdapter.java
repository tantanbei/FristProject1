package com.whoplate.paipable.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.ActivityWebView;
import com.whoplate.paipable.networkpacket.Paper;
import com.whoplate.paipable.time.XTime;
import com.whoplate.paipable.ui.XView;
import com.whoplate.paipable.viewHolder.PaperListViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class PaperListRecycleViewAdapter extends RecyclerView.Adapter<PaperListViewHolder> {

    private ArrayList<Paper> data;
    private WeakReference<Activity> a;

    public PaperListRecycleViewAdapter(Activity a, ArrayList<Paper> data) {
        this.data = data;
        this.a = new WeakReference<Activity>(a);
    }

    @Override
    public PaperListViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
        View view = LayoutInflater.from(vg.getContext()).inflate(R.layout.row_paper, vg, false);
        return new PaperListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PaperListViewHolder holder, final int position) {
        Log.d("tan", "onBindViewHolder: " + data.get(position).Title);
        holder.title.setText(data.get(position).Title);
        holder.date.setText(XTime.TimeStampToDate(data.get(position).DateSubmit * 1000L));

        int imageId = data.get(position).ImageId;
        if (imageId != 0) {
            Glide.with(a.get()).load(Const.SERVER_IP + Const.URL_APN + Const.URL_GET_IMAGE + "?imagetype=0&imageid=" + imageId).centerCrop().into(holder.image);
        } else {
            XView.Hide(holder.image);
        }

        final int reprintId = data.get(position).ReprintId;
        final String reprintText;
        if (reprintId >= Const.Reprints.size() || reprintId == 0) {
            reprintText = "转载";
        } else {
            reprintText = Const.Reprints.get(reprintId);
        }
        holder.reprint.setText(reprintText);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = a.get();
                if (activity == null || activity.isDestroyed()) {
                    return;
                }

                Intent intent = new Intent(activity, ActivityWebView.class);
                intent.putExtra("title", data.get(position).Title);
                intent.putExtra("paperid", data.get(position).PaperId);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
