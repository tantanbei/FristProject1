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
import com.whoplate.paipable.adapter.PaperListRecycleViewAdapter;
import com.whoplate.paipable.fragment.base.XFragment;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.itemDecoration.LinearLayoutColorDivider;
import com.whoplate.paipable.networkpacket.AuctionStatus;
import com.whoplate.paipable.networkpacket.Paper;
import com.whoplate.paipable.networkpacket.base.Papers;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.time.XTime;
import com.whoplate.paipable.ui.XView;
import com.whoplate.paipable.util.Goto;
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
    private TextView more;
    private RecyclerView message;
    private ScrollView scrollView;
    private PaperListRecycleViewAdapter adapter = null;

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
        more = (TextView) view.findViewById(R.id.more);
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
                Goto.DialCustomer();
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

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivityMessage.class));
            }
        });

        message.setLayoutManager(new LinearLayoutManager(Parent));
        message.addItemDecoration(new LinearLayoutColorDivider());

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

                        final Response response = Http.Get(Const.URL_APN + Const.URL_AUCTION_STATUS);
                        final String str = response.body().string();
                        Log.d("tan", "refreshData: " + str);

                        final AuctionStatus status = LoganSquare.parse(str, AuctionStatus.class);

                        App.Uihandler.post(new Runnable() {
                            @Override
                            public void run() {
                                switch (status.Id) {
                                    case 0:
                                        XView.Show(auctionIdle);
                                        XView.Hide(auctionReady);
                                        XView.Hide(auctionRunning);
                                        XView.Hide(auctionOver);
                                        if (status.Data.length == 1) {
                                            long time = status.Data[0];
                                            int day = XTime.GetDayByTimeStamp(time);
                                            Log.d("tan", "day: " + day);
                                            idleDate.setText(day + "号");
                                        }
                                        break;
                                    case 1:
                                        XView.Hide(auctionIdle);
                                        XView.Show(auctionReady);
                                        XView.Hide(auctionRunning);
                                        XView.Hide(auctionOver);
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
                                        XView.Hide(auctionIdle);
                                        XView.Hide(auctionReady);
                                        XView.Show(auctionRunning);
                                        XView.Hide(auctionOver);
                                        if (status.Data.length == 1) {
                                            runningForecast.setText(String.valueOf(status.Data[0]));
                                        }
                                        break;
                                    case 3:
                                        XView.Hide(auctionIdle);
                                        XView.Hide(auctionReady);
                                        XView.Hide(auctionRunning);
                                        XView.Show(auctionOver);
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
        adapter = new PaperListRecycleViewAdapter(Parent, papers);
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
}
