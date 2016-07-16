package fristproject1.sample.com.fristproject1.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.networkpacket.CurrentPacket;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityAuction extends Activity {
    TextView currPriceTextView;
    TextView serverTimeTextView;
    TextView forecastTransactionPriceTextView;
    IconTextView goBack;
    TextView title;

    Timer timer = new Timer(true);
    TimerTask timerTask;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auction);

        currPriceTextView = (TextView) findViewById(R.id.current_price);
        serverTimeTextView = (TextView) findViewById(R.id.server_time);
        forecastTransactionPriceTextView = (TextView) findViewById(R.id.forecast_transaction_price);
        goBack = (IconTextView) findViewById(R.id.goBack);
        title = (TextView) findViewById(R.id.title);

        title.setText(R.string.auction);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        timerTask = new TimerTask() {
            @Override
            public void run() {
                getCurrentPrice(Const.SERVER_IP + "/auction/price");
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
                    App.Uihandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityAuction.this, R.string.auction_unstart, Toast.LENGTH_LONG).show();
                        }
                    });
                    timerTask.cancel();
                    return;
                case '2':
                    App.Uihandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ActivityAuction.this, R.string.auction_over, Toast.LENGTH_LONG).show();
                        }
                    });
                    timerTask.cancel();
                    return;
            }

            final CurrentPacket currentPacket = LoganSquare.parse(new String(bs, 1, bs.length - 1, "UTF-8"), CurrentPacket.class);
            String tmpCurrentTransactionPrice = "";
            final String currentTransactionPrice;
            final String serverTime;
            Log.d("tan", "text price:" + currPriceTextView.getText().toString());

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

            App.Uihandler.post(new Runnable() {

                @Override
                public void run() {
                    if (!currentTransactionPrice.equals("")) {
                        currPriceTextView.setText(currentTransactionPrice);
                    }
                    serverTimeTextView.setText(serverTime);
                    forecastTransactionPriceTextView.setText(String.valueOf(forecastPrice));

                }
            });
        } catch (IOException e) {
            e.printStackTrace();

            App.Uihandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ActivityAuction.this, R.string.request_fails, Toast.LENGTH_LONG).show();
                }
            });

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
