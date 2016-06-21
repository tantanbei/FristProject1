package fristproject1.sample.com.fristproject1.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;

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
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityAuction extends Activity {
    TextView currPriceTextView;
    TextView serverTimeTextView;
    TextView forecastTransactionPriceTextView;

    Timer timer = new Timer(true);
    TimerTask timerTask;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new IoniconsModule());

        setContentView(R.layout.activity_auction);

        currPriceTextView = (TextView) findViewById(R.id.current_price);
        serverTimeTextView = (TextView) findViewById(R.id.server_time);
        forecastTransactionPriceTextView = (TextView) findViewById(R.id.forecast_transaction_price);


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

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("tan", e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String str = response.body().string();

                final CurrentPacket currentPacket = LoganSquare.parse(str, CurrentPacket.class);
                String tmpCurrentTransactionPrice = "";
                final String currentTransactionPrice;
                final String serverTime;
                Log.d("tan", "text price:" + currPriceTextView.getText().toString());

                if (currPriceTextView.getText().toString().equals("")) {
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
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        timer.cancel();
    }
}
