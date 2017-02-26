package com.whoplate.paipable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.ui.XView;
import com.whoplate.paipable.util.XFile;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class ActivityEditVideo extends XActivity {
    static public final int REQUEST_RECORDER = 1;

    JCVideoPlayerStandard videoPlayer;
    TextView emptyVideo;
    String videoPath;
    String thumbPath;

    @Override
    public int GetContentView() {
        return R.layout.activity_edit_video;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("编辑视频");
        rigthBtn.setText("上传");
        XView.Show(rigthBtn);
        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 25/02/2017
            }
        });

        emptyVideo = (TextView) findViewById(R.id.empty_video);
        videoPlayer = (JCVideoPlayerStandard) findViewById(R.id.video_player);

        emptyVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ActivityEditVideo.this, ActivityRecorder.class), REQUEST_RECORDER);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        videoPath = data.getStringExtra(ActivityRecorder.VIDEO_PATH);

        if (!XFile.Exists(videoPath)){
            XToast.Show("录制视频失败");
            return;
        }

        XView.Show(videoPlayer);
        XView.Hide(emptyVideo);
        videoPlayer.setUp(videoPath, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
    }
}
