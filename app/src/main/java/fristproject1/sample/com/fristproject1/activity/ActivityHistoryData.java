package fristproject1.sample.com.fristproject1.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.github.mikephil.charting.charts.LineChart;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.IOException;

import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.networkpacket.AuctionHistoryResults;
import fristproject1.sample.com.fristproject1.networkpacket.RequestAuctionHistoryPacket;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityHistoryData extends Activity {
    LineChart historyChart;
    TextView title;
    IconTextView goBack;
    OkHttpClient client;
    Request request;

    AuctionHistoryResults allHistoryCache;
    boolean forceToRefreshData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_history);

        historyChart = (LineChart) findViewById(R.id.history_data);
        title = (TextView) findViewById(R.id.title);
        goBack = (IconTextView) findViewById(R.id.goBack);

        title.setText(R.string.history_data);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        });

        thread.start();
    }

    private void refreshData() {

        if (allHistoryCache != null && !forceToRefreshData) {
            return;
        }

        RequestAuctionHistoryPacket requestAuctionHistoryPacket = new RequestAuctionHistoryPacket(true, true, true, true, true, true);
        Log.d("tan", "refreshData: "+requestAuctionHistoryPacket.ToJsonString());
        client = new OkHttpClient();
        request = new Request.Builder()
                .url(Const.SERVER_IP + "/auction/history?request=" + requestAuctionHistoryPacket.ToJsonString())
                .method("GET", null)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            final String str = response.body().string();
            allHistoryCache = LoganSquare.parse(str, AuctionHistoryResults.class);

            Log.d("tan", "onStart: " + str);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
