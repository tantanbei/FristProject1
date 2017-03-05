package com.whoplate.paipable.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.UploadVideo;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.ui.XView;
import com.whoplate.paipable.util.XFile;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.Response;

public class ActivityEditVideo extends XActivity {
    static public final int REQUEST_RECORDER = 1;

    JCVideoPlayerStandard videoPlayer;
    TextView emptyVideo;
    EditText titleEditor;
    EditText summaryEditor;

    String videoPath;
    Bitmap thumbBmp;

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
                final ArrayList<String> keys = new ArrayList<String>();
                final ArrayList<byte[]> values = new ArrayList<byte[]>();
                keys.add("title");
                values.add(titleEditor.getText().toString().getBytes());

                keys.add("summary");
                values.add(summaryEditor.getText().toString().getBytes());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                thumbBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                keys.add("thumbnail");
                values.add(stream.toByteArray());

                keys.add("video");
                values.add(XFile.GetBytesFromPath(videoPath));

                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {
                        Response response = Http.Post(Const.URL_API + Const.URL_UPLOAD_VIDEO, keys, values);
                        //// TODO: 05/03/2017  
                    }
                });
            }
        });

        emptyVideo = (TextView) findViewById(R.id.empty_video);
        videoPlayer = (JCVideoPlayerStandard) findViewById(R.id.video_player);
        titleEditor = (EditText) findViewById(R.id.video_title);
        summaryEditor = (EditText) findViewById(R.id.video_summary);

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

        if (!XFile.Exists(videoPath)) {
            XToast.Show("录制视频失败");
            return;
        }

        XView.Show(videoPlayer);
        XView.Hide(emptyVideo);
        thumbBmp = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
        videoPlayer.setUp(videoPath, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
        videoPlayer.thumbImageView.setBackground(new BitmapDrawable(null, thumbBmp));
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        videoPlayer = null;
        emptyVideo = null;
        videoPath = null;
        thumbBmp = null;
    }
}
