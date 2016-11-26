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
    private MyRecycleViewAdapter adapter = null;

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
        adapter = new MyRecycleViewAdapter(this, papers);
        message.setAdapter(adapter);
    }

    public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.myViewHolder> {
        private ArrayList<Paper> data;
        private WeakReference<Activity> a;

        public MyRecycleViewAdapter(Activity a, ArrayList<Paper> data) {
            this.data = data;
            this.a = new WeakReference<Activity>(a);
        }

        @Override
        public MyRecycleViewAdapter.myViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
            View view = LayoutInflater.from(vg.getContext()).inflate(R.layout.row_paper, vg, false);
            return new MyRecycleViewAdapter.myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyRecycleViewAdapter.myViewHolder holder, final int position) {
            Log.d("tan", "onBindViewHolder: " + data.get(position).Title);
            holder.title.setText(data.get(position).Title);
            holder.date.setText(XTime.TimeStampToDate(data.get(position).DateSubmit * 1000L));
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityMessage.this, ActivityWebView.class);
                    intent.putExtra("paperid", data.get(position).PaperId);
                    intent.putExtra("title", data.get(position).Title);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class myViewHolder extends RecyclerView.ViewHolder {
            public View root;
            public TextView title;
            public TextView date;

            public myViewHolder(View itemView) {
                super(itemView);

                root = itemView;
                title = (TextView) itemView.findViewById(R.id.paper_title);
                date = (TextView) itemView.findViewById(R.id.date);
            }
        }
    }
}
