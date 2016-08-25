package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;

import java.util.Timer;
import java.util.TimerTask;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;
import fristproject1.sample.com.fristproject1.http.Http;
import fristproject1.sample.com.fristproject1.networkpacket.StrategyPacket;
import fristproject1.sample.com.fristproject1.thread.XThread;
import okhttp3.Response;

public class ActivityAuctionStrategy extends XActivity {

    TextView second30;
    TextView second40;
    TextView second45;
    TextView second50;
    TextView forecastPrirce;

    Timer timer = new Timer(true);
    TimerTask timerTask;

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
        forecastPrirce = (TextView) findViewById(R.id.forecast_transaction_price);

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

        timer.schedule(timerTask, 1000, 300);
    }

    private void getStrategyPrice() {
        final String url = Const.SERVER_IP + Const.URL_AUCTION_STRATEGY;

        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Http.Get(ActivityAuctionStrategy.this, url);

                    final StrategyPacket packet = LoganSquare.parse(response.body().byteStream(), StrategyPacket.class);
                    switch (packet.Style) {
                        case 1:
                            App.Uihandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityAuctionStrategy.this, R.string.auction_unstart, Toast.LENGTH_LONG).show();
                                }
                            });
                            timerTask.cancel();
                            return;
                        case 2:
                            App.Uihandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ActivityAuctionStrategy.this, R.string.auction_over, Toast.LENGTH_LONG).show();
                                }
                            });
                            timerTask.cancel();
                            return;
                        case 0:
                            App.Uihandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    forecastPrirce.setText(String.valueOf(packet.ForecastPrice));
                                    second30.setText(String.valueOf(packet.Second30));
                                    second40.setText(String.valueOf(packet.Second40));
                                    second45.setText(String.valueOf(packet.Second45));
                                    second50.setText(String.valueOf(packet.Second50));
                                }
                            });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
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
}
