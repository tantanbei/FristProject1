package com.whoplate.paipable.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.networkpacket.VideoInfo;
import com.whoplate.paipable.time.XTime;
import com.whoplate.paipable.viewHolder.VideoListViewHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListViewHolder> {
    private ArrayList<VideoInfo> data;
    private WeakReference<XActivity> a;

    public VideoListAdapter(XActivity a, ArrayList<VideoInfo> data) {
        this.data = data;
        this.a = new WeakReference<XActivity>(a);
    }

    @Override
    public VideoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(a.get()).inflate(R.layout.row_video, parent, false);
        return new VideoListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoListViewHolder holder, int position) {
        VideoInfo info = data.get(position);

        holder.player.setUp(Const.SERVER_IP + Const.URL_APN + "/video?videoid=" + info.VideoId
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, info.Title);
        Glide.with(a.get()).load(Const.SERVER_IP + Const.URL_APN + "/image/get?imageid=" + info.ImageId).into(holder.player.thumbImageView);

        holder.userName.setText(info.UserName);
        holder.dataSubmit.setText(XTime.TimeStampToDate(info.DataSubmit * 1000L));

    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }
}
