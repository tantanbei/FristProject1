package com.whoplate.paipable.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whoplate.paipable.R;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class VideoListViewHolder extends RecyclerView.ViewHolder {
    public View root;
    public JCVideoPlayerStandard player;
    public TextView userName;
    public TextView dataSubmit;

    public VideoListViewHolder(View itemView) {
        super(itemView);

        root = itemView;
        player = (JCVideoPlayerStandard) itemView.findViewById(R.id.video_player);
        userName = (TextView) itemView.findViewById(R.id.username);
        dataSubmit = (TextView) itemView.findViewById(R.id.data_submit);
    }
}
