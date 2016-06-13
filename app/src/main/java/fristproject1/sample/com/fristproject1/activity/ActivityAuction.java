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

    private boolean threadIsEnable = true;
    Runnable sendGetCurrPriceRunnable;
    Thread thread;

    OkHttpClient client = new OkHttpClient();

    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new IoniconsModule());

        setContentView(R.layout.activity_auction);

        currPriceTextView = (TextView) findViewById(R.id.current_price);

        if (sendGetCurrPriceRunnable == null) {
            sendGetCurrPriceRunnable = new Runnable() {
                @Override
                public void run() {
                    while (threadIsEnable) {
                        getCurrentPrice(Const.SERVER_IP + "/auction/price");
                        try {
                            Thread.sleep(300L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }

        thread = new Thread(sendGetCurrPriceRunnable);
        thread.start();
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
                handler.post(new Runnable() {

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

        threadIsEnable = false;
    }
}
