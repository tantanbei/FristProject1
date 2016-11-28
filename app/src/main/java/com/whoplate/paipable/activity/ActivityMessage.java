package com.whoplate.paipable.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.adapter.PaperListRecycleViewAdapter;
import com.whoplate.paipable.fragment.HomeTabFragment;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.Paper;
import com.whoplate.paipable.networkpacket.base.Papers;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.time.XTime;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.Response;

public class ActivityMessage extends XActivity {
    private RecyclerView message;
    private PaperListRecycleViewAdapter adapter = null;

    @Override
    public int GetContentView() {
        return R.layout.activity_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.announcement);

        message = (RecyclerView) findViewById(R.id.all_messages);
        message.setLayoutManager(new LinearLayoutManager(this));

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
                    Response response = Http.Get(Const.URL_APN + Const.URL_PAPER);
                    final Papers papers = LoganSquare.parse(response.body().byteStream(), Papers.class);

                    Log.d("tan", "papers: " + papers.ToJsonString());

                    App.Uihandler.post(new Runnable() {
                        @Override
                        public void run() {
                            generateMessage(papers.Data);
                        }
                    });

                } catch (IOException e) {
                    XDebug.Handle(e);
                }
            }
        });
    }

    private void generateMessage(final ArrayList<Paper> papers) {
        adapter = new PaperListRecycleViewAdapter(this, papers);
        message.setAdapter(adapter);
    }
}
