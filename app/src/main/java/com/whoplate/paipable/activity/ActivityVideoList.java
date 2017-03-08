package com.whoplate.paipable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.adapter.VideoListAdapter;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.itemDecoration.LinearLayoutColorDivider;
import com.whoplate.paipable.networkpacket.VideoInfo;
import com.whoplate.paipable.networkpacket.VideoInfos;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.ui.XView;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;
import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import okhttp3.Response;

public class ActivityVideoList extends XActivity {
    private RecyclerView videoList;
    private VideoListAdapter adapter = null;

    @Override
    public int GetContentView() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        videoList = null;
        adapter = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.break_rule_video);
        XView.Show(rigthBtn);
        rigthBtn.setText("录制");

        videoList = (RecyclerView) findViewById(R.id.all_messages);
        videoList.setLayoutManager(new LinearLayoutManager(this));
        videoList.addItemDecoration(new LinearLayoutColorDivider());

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityVideoList.this, ActivityEditVideo.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (adapter == null) {
            refreshData();
        }
    }

    private void refreshData() {
        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Http.Get(Const.URL_APN + Const.URL_GET_VIDEO_LIST);
                    final VideoInfos videoInfos = LoganSquare.parse(response.body().byteStream(), VideoInfos.class);

                    App.Uihandler.post(new Runnable() {
                        @Override
                        public void run() {
                            generateVideo(videoInfos.Data);
                        }
                    });

                } catch (IOException e) {
                    XDebug.Handle(e);
                }
            }
        });
    }

    private void generateVideo(final ArrayList<VideoInfo> videoInfos) {
        adapter = new VideoListAdapter(this, videoInfos);
        videoList.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


}
