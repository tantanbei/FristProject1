package com.whoplate.paipable.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.networkpacket.CurrentPacket;
import com.whoplate.paipable.toast.XToast;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityAuction extends XActivity {
    TextView currPriceTextView;
    TextView serverTimeTextView;
    TextView forecastTransactionPriceTextView;
    TextView limitation;
    TextView peopleNum;

    Timer timer = new Timer(true);
    TimerTask timerTask;

    OkHttpClient client = new OkHttpClient();

    @Override
    public int GetContentView() {
        return R.layout.activity_auction;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currPriceTextView = (TextView) findViewById(R.id.current_price);
        serverTimeTextView = (TextView) findViewById(R.id.server_time);
        forecastTransactionPriceTextView = (TextView) findViewById(R.id.forecast_transaction_price);
        limitation = (TextView) findViewById(R.id.limitation);
        peopleNum = (TextView) findViewById(R.id.people_number);

        title.setText(R.string.auction);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                getCurrentPrice(Const.URL_API + "/auction/price");
            }
        };

        timer.schedule(timerTask, 1000, 300);
    }

    private void getCurrentPrice(String url) {

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        Call call = client.newCall(request);

        try {

            Response response = call.execute();
            final byte[] bs = response.body().bytes();

            switch (bs[0]) {
                case '1':
                    XToast.Show(R.string.auction_unstart);
                    timerTask.cancel();
                    return;
                case '2':
                    XToast.Show(R.string.auction_over);
                    timerTask.cancel();
                    return;
            }

            final CurrentPacket currentPacket = LoganSquare.parse(new String(bs, 1, bs.length - 1, "UTF-8"), CurrentPacket.class);
            String tmpCurrentTransactionPrice = "";
            final String currentTransactionPrice;
            final String serverTime;

            if (currPriceTextView.getText().toString().equals("")) {
                if (currentPacket == null) {
                    return;
                }
                tmpCurrentTransactionPrice = String.valueOf(currentPacket.currentTransactionPrice);
            } else if (Integer.parseInt(currPriceTextView.getText().toString()) < currentPacket.currentTransactionPrice) {
                tmpCurrentTransactionPrice = String.valueOf(currentPacket.currentTransactionPrice);
            }
            currentTransactionPrice = tmpCurrentTransactionPrice;

            long time = currentPacket.serverTime;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = new Date(time);
            serverTime = format.format(d1);

            final int forecastPrice = currentPacket.forecastTransactionPrice;
            final int people = currentPacket.peopleNumber;
            final int limitationNum = currentPacket.limitation;

            App.Uihandler.post(new Runnable() {

                @Override
                public void run() {
                    if (!currentTransactionPrice.equals("")) {
                        currPriceTextView.setText(currentTransactionPrice);
                    }
                    serverTimeTextView.setText(serverTime);
                    if (Integer.parseInt(forecastTransactionPriceTextView.getText().toString()) < currentPacket.forecastTransactionPrice) {
                        forecastTransactionPriceTextView.setText(String.valueOf(forecastPrice));
                    }
                    if (Integer.parseInt(limitation.getText().toString()) < currentPacket.limitation) {
                        limitation.setText(String.valueOf(limitationNum));
                    }
                    if (Integer.parseInt(peopleNum.getText().toString()) < currentPacket.peopleNumber) {
                        peopleNum.setText(String.valueOf(people));
                    }
                }
            });
        } catch (IOException e) {
            XDebug.Handle(e);

            XToast.Show(R.string.request_fails);

            timer.cancel();
        } finally {
            call.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        timer.cancel();
    }
}
