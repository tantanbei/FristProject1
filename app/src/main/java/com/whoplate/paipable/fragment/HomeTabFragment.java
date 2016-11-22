package com.whoplate.paipable.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.joanzapata.iconify.widget.IconTextView;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.ActivityMessage;
import com.whoplate.paipable.activity.ActivityAuctionStrategy;
import com.whoplate.paipable.activity.ActivityHistoryData;
import com.whoplate.paipable.activity.ActivityHome;
import com.whoplate.paipable.activity.ActivitySignIn;
import com.whoplate.paipable.activity.ActivityWebView;
import com.whoplate.paipable.fragment.base.XFragment;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.AuctionStatus;
import com.whoplate.paipable.networkpacket.Paper;
import com.whoplate.paipable.networkpacket.base.Papers;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.time.XTime;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.Response;

public class HomeTabFragment extends XFragment {

    Runnable refreshRunnable;

    private IconTextView homeMenu;
    private IconTextView homeCustomerService;
    private ViewPager homeViewPager;
    private LinearLayout auctionIdle;
    private LinearLayout auctionReady;
    private LinearLayout auctionRunning;
    private LinearLayout auctionOver;
    private TextView idleDate;
    private TextView readyDate;
    private TextView readyForecast;
    private TextView runningForecast;
    private TextView overPrice;
    private TextView historyData;
    private TextView signInEveryDay;
    private RecyclerView message;
    private ScrollView scrollView;
    private MyRecycleViewAdapter adapter = null;

    private ArrayList<View> ImageArrayList = new ArrayList<View>();
    private int[] srcIds = {R.mipmap.home_first_pager, R.mipmap.home_second_pager};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_tab_fragment, container, false);

        homeMenu = (IconTextView) view.findViewById(R.id.home_menu);
        homeCustomerService = (IconTextView) view.findViewById(R.id.home_customer_service);
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        homeViewPager = (ViewPager) view.findViewById(R.id.home_view_pager);
        auctionReady = (LinearLayout) view.findViewById(R.id.auction_ready);
        auctionIdle = (LinearLayout) view.findViewById(R.id.auction_idle);
        auctionRunning = (LinearLayout) view.findViewById(R.id.auction_running);
        auctionOver = (LinearLayout) view.findViewById(R.id.auction_over);
        historyData = (TextView) view.findViewById(R.id.history_data);
        idleDate = (TextView) view.findViewById(R.id.idle_date);
        readyDate = (TextView) view.findViewById(R.id.ready_date);
        readyForecast = (TextView) view.findViewById(R.id.ready_forecast);
        runningForecast = (TextView) view.findViewById(R.id.running_forecast);
        overPrice = (TextView) view.findViewById(R.id.over_price);
        signInEveryDay = (TextView) view.findViewById(R.id.sign_in_everyday);
        message = (RecyclerView) view.findViewById(R.id.home_message);

        for (int srcId : srcIds) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(srcId);
            ImageArrayList.add(imageView);
        }

        homeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityHome) Parent).OpenDrawer();
            }
        });

        homeCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Const.CUSTOMER_SERVICE_PHONE));
                startActivity(intent);
            }
        });

        homeViewPager.setAdapter(new HomeViewPagerAdapter());

        auctionIdle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivityMessage.class));
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivityAuctionStrategy.class));
            }
        };

        auctionReady.setOnClickListener(listener);
        auctionRunning.setOnClickListener(listener);
        auctionOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivityMessage.class));
            }
        });

        historyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivityHistoryData.class));
            }
        });

        signInEveryDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivitySignIn.class));
            }
        });

        message.setLayoutManager(new LinearLayoutManager(Parent));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshData(0);

        if (adapter == null) {
            getMessage();
        }
    }

    private void refreshData(long delay) {

        if (refreshRunnable == null) {
            refreshRunnable = new Runnable() {
                @Override
                public void run() {

                    try {

                        final Response response = Http.Get(Const.SERVER_IP + Const.URL_AUCTION_STATUS);
                        final String str = response.body().string();
                        Log.d("tan", "refreshData: " + str);

                        final AuctionStatus status = LoganSquare.parse(str, AuctionStatus.class);

                        App.Uihandler.post(new Runnable() {
                            @Override
                            public void run() {
                                switch (status.Id) {
                                    case 0:
                                        auctionIdle.setVisibility(View.VISIBLE);
                                        auctionReady.setVisibility(View.GONE);
                                        auctionRunning.setVisibility(View.GONE);
                                        auctionOver.setVisibility(View.GONE);
                                        if (status.Data.length == 1) {
                                            long time = status.Data[0];
                                            int day = XTime.GetDayByTimeStamp(time);
                                            Log.d("tan", "day: " + day);
                                            idleDate.setText(day + "号");
                                        }
                                        break;
                                    case 1:
                                        auctionIdle.setVisibility(View.GONE);
                                        auctionReady.setVisibility(View.VISIBLE);
                                        auctionRunning.setVisibility(View.GONE);
                                        auctionOver.setVisibility(View.GONE);
                                        if (status.Data.length == 2) {
                                            long time = status.Data[0];
                                            int timeGap = (int) ((time - System.currentTimeMillis()));
                                            int day = timeGap / (1000 * 60 * 60 * 24);

                                            if (day != 0) {
                                                readyDate.setText(day + "天");
                                            } else {
                                                int hour = timeGap / (1000 * 60 * 60);

                                                if (hour != 0) {
                                                    readyDate.setText(hour + "小时");
                                                } else {
                                                    int min = timeGap / (1000 * 60);
                                                    if (min != 0) {
                                                        readyDate.setText(min + "分钟");
                                                    } else {
                                                        int sec = timeGap / (1000);
                                                        if (sec != 0) {
                                                            readyDate.setText(sec + "秒");
                                                            refreshData(600);
                                                        }
                                                    }
                                                }
                                            }

                                            readyForecast.setText(status.Data[1] + "-" + (status.Data[1] + 300));
                                        }
                                        break;
                                    case 2:
                                        auctionIdle.setVisibility(View.GONE);
                                        auctionReady.setVisibility(View.GONE);
                                        auctionRunning.setVisibility(View.VISIBLE);
                                        auctionOver.setVisibility(View.GONE);
                                        if (status.Data.length == 1) {
                                            runningForecast.setText(String.valueOf(status.Data[0]));
                                        }
                                        break;
                                    case 3:
                                        auctionIdle.setVisibility(View.GONE);
                                        auctionReady.setVisibility(View.GONE);
                                        auctionRunning.setVisibility(View.GONE);
                                        auctionOver.setVisibility(View.VISIBLE);
                                        if (status.Data.length == 1) {
                                            overPrice.setText(String.valueOf(status.Data[0]));
                                        }
                                        break;
                                }
                            }
                        });
                    } catch (IOException e) {
                        XDebug.Handle(e);
                    }
                }
            };
        }

        XThread.RunBackground(refreshRunnable, delay);
    }

    private void getMessage() {
        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Http.Get(Const.SERVER_IP + "/paper");
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
        adapter = new MyRecycleViewAdapter(Parent, papers);
        message.setAdapter(adapter);
        App.Uihandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        }, 100);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (refreshRunnable != null) {
            XThread.Cancel(refreshRunnable);
        }
    }

    public class HomeViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ImageArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(ImageArrayList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(ImageArrayList.get(position % ImageArrayList.size()), 0);
            return ImageArrayList.get(position % ImageArrayList.size());
        }
    }

    public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.myViewHolder> {
        private ArrayList<Paper> data;
        private WeakReference<Activity> a;

        public MyRecycleViewAdapter(Activity a, ArrayList<Paper> data) {
            this.data = data;
            this.a = new WeakReference<Activity>(a);
        }

        @Override
        public myViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
            View view = LayoutInflater.from(vg.getContext()).inflate(R.layout.row_paper, vg, false);
            return new myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(myViewHolder holder, final int position) {
            Log.d("tan", "onBindViewHolder: " + data.get(position).Title);
            holder.title.setText(data.get(position).Title);
            holder.date.setText(XTime.TimeStampToDate(data.get(position).DateSubmit * 1000L));
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Parent, ActivityWebView.class);
                    intent.putExtra("title", data.get(position).Title);
                    intent.putExtra("paperid", data.get(position).PaperId);
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
