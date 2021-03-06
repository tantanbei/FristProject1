package com.whoplate.paipable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.StrategyPacket;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.util.XDebug;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Response;

public class ActivityAuctionStrategy extends XActivity implements View.OnClickListener {
    final String STRATEGY_TYPE = "strategy_type";
    final String FREQUENCY_STRATEGY = "0";
    final String AVERAGE_STRATEGY = "1";

    TextView frequency;
    TextView average;
    TextView strategyTitle;
    TextView second30;
    TextView second40;
    TextView second45;
    TextView second50;

    Timer timer;
    TimerTask timerTask;

    String strategyType = FREQUENCY_STRATEGY;

    @Override
    public int GetContentView() {
        return R.layout.activity_auction_strategy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        second30 = (TextView) findViewById(R.id.second_30);
        second40 = (TextView) findViewById(R.id.second_40);
        second45 = (TextView) findViewById(R.id.second_45);
        second50 = (TextView) findViewById(R.id.second_50);

        strategyTitle = (TextView) findViewById(R.id.algorithm_title);

        frequency = (TextView) findViewById(R.id.frequency_strategy);
        average = (TextView) findViewById(R.id.average_strategy);

        frequency.setOnClickListener(this);
        average.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                getStrategyPrice();
            }
        };

        timer = new Timer(true);
        timer.schedule(timerTask, 1000, 300);
    }

    private void getStrategyPrice() {
        final String url = Const.URL_APN + Const.URL_AUCTION_STRATEGY + "?type=" + strategyType;

        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Http.Get(url);

                    final StrategyPacket packet = LoganSquare.parse(response.body().byteStream(), StrategyPacket.class);
                    switch (packet.Style) {
                        case 1:
                            XToast.Show(R.string.auction_unstart);
                            timer.cancel();
                            return;
                        case 2:
                            XToast.Show(R.string.auction_over);
                            timer.cancel();
                            return;
                        case 0:
                            App.Uihandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    second30.setText(String.valueOf(packet.Second30));
                                    second40.setText(String.valueOf(packet.Second40));
                                    second45.setText(String.valueOf(packet.Second45));
                                    second50.setText(String.valueOf(packet.Second50));
                                }
                            });
                    }

                } catch (Exception e) {
                    XDebug.Handle(e);
                    timer.cancel();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        timer.cancel();
    }

    @Override
    public void onClick(View v) {
        Log.d("tan", "onClick: " + v.getId());
        switch (v.getId()) {
            case R.id.frequency_strategy:
                if (strategyType.equals(FREQUENCY_STRATEGY)) {
                    return;
                }

                frequency.setBackgroundResource(R.color.colorPrimary);
                frequency.setTextColor(getResources().getColor(R.color.white));
                average.setBackgroundResource(R.drawable.rectangle);
                average.setTextColor(getResources().getColor(R.color.black));
                strategyTitle.setText(R.string.frequency_strategy);
                strategyType = FREQUENCY_STRATEGY;
                getStrategyPrice();
                break;

            case R.id.average_strategy:
                if (strategyType.equals(AVERAGE_STRATEGY)) {
                    return;
                }

                average.setBackgroundResource(R.color.colorPrimary);
                average.setTextColor(getResources().getColor(R.color.white));
                frequency.setBackgroundResource(R.drawable.rectangle);
                frequency.setTextColor(getResources().getColor(R.color.black));
                strategyTitle.setText(R.string.average_strategy);
                strategyType = AVERAGE_STRATEGY;
                getStrategyPrice();
                break;
        }
    }
}
