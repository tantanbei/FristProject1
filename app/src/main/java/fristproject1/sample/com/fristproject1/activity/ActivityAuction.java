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
        serverTimeTextView = (TextView) findViewById(R.id.server_time);

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

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        currPriceTextView.setText(currentTransactionPrice);
                        serverTimeTextView.setText(serverTime);
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
