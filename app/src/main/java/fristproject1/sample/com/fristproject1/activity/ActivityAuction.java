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
import java.util.Timer;
import java.util.TimerTask;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.networkpacket.CurrentPrice;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityAuction extends Activity {
    TextView currPriceTextView;

    Timer timer = new Timer(true);
    TimerTask timerTask;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new IoniconsModule());

        setContentView(R.layout.activity_auction);

        currPriceTextView = (TextView) findViewById(R.id.current_price);

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

                final CurrentPrice currPrice = LoganSquare.parse(str, CurrentPrice.class);

                Log.d("tan", "carPrices: " + currPrice.toString());
                App.Uihandler.post(new Runnable() {

                    @Override
                    public void run() {
                        Log.d("tan", "text price:" + currPriceTextView.getText().toString() + " curr price:" + currPrice.price);
                        if (currPriceTextView.getText().toString().equals("")) {
                            currPriceTextView.setText(String.valueOf(currPrice.price));
                        } else if (Integer.parseInt(currPriceTextView.getText().toString()) < currPrice.price) {
                            currPriceTextView.setText(String.valueOf(currPrice.price));
                        }
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
