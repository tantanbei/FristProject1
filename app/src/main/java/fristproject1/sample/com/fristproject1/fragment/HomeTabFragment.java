package fristproject1.sample.com.fristproject1.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.ActivityAuction;
import fristproject1.sample.com.fristproject1.activity.ActivityAuctionIdle;
import fristproject1.sample.com.fristproject1.activity.ActivityHistoryData;
import fristproject1.sample.com.fristproject1.activity.ActivityHome;
import fristproject1.sample.com.fristproject1.fragment.base.XFragment;
import fristproject1.sample.com.fristproject1.http.Http;
import fristproject1.sample.com.fristproject1.networkpacket.AuctionStatus;
import fristproject1.sample.com.fristproject1.thread.XThread;
import fristproject1.sample.com.fristproject1.time.XTime;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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

    private OkHttpClient client = new OkHttpClient();

    private ArrayList<View> ImageArrayList = new ArrayList<View>();
    private int[] srcIds = {R.mipmap.home_first_pager, R.mipmap.home_second_pager, R.mipmap.home_third_pager};

    private Activity currParent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_tab_fragment, container, false);

        homeMenu = (IconTextView) view.findViewById(R.id.home_menu);
        homeCustomerService = (IconTextView) view.findViewById(R.id.home_customer_service);
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

        for (int i = 0; i < srcIds.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(srcIds[i]);
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
                startActivity(new Intent(Parent, ActivityAuctionIdle.class));
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivityAuction.class));
            }
        };

        auctionReady.setOnClickListener(listener);
        auctionRunning.setOnClickListener(listener);
        auctionOver.setOnClickListener(listener);

        historyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivityHistoryData.class));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshData(0);
    }

    private void refreshData(long delay) {

        if (refreshRunnable == null) {
            refreshRunnable = new Runnable() {
                @Override
                public void run() {

                    try {

                        final Response response = Http.Get(Parent, Const.SERVER_IP + Const.URL_AUCTION_STATUS);
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
                        e.printStackTrace();
                    }
                }
            };
        }

        XThread.RunBackground(refreshRunnable, delay);
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

    @Override
    public void onStop() {
        super.onStop();

        if (refreshRunnable != null) {
            XThread.Cancel(refreshRunnable);
        }
    }
}
