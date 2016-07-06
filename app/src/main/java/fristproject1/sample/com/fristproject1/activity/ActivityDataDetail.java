package fristproject1.sample.com.fristproject1.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.joanzapata.iconify.widget.IconTextView;

import java.io.IOException;
import java.util.ArrayList;

import fristproject1.sample.com.fristproject1.App;
import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.networkpacket.AuctionDetail;
import fristproject1.sample.com.fristproject1.networkpacket.AuctionDetails;
import fristproject1.sample.com.fristproject1.networkpacket.AuctionHistoryResult;
import fristproject1.sample.com.fristproject1.networkpacket.AuctionHistoryResults;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityDataDetail extends Activity {

    IconTextView goBack;
    TextView title;
    LineChart detailChart;

    OkHttpClient client;
    Request request;

    String requestDate = "201509";

    LineDataSet pricesDataSet;
    ArrayList<String> distances = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deitail);

        goBack = (IconTextView) findViewById(R.id.goBack);
        title = (TextView) findViewById(R.id.title);
        detailChart = (LineChart) findViewById(R.id.detail_chart);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title.setText(R.string.data_detail);

        detailChart.setDragEnabled(true);
        detailChart.setScaleEnabled(true);
        detailChart.setTouchEnabled(true);
        detailChart.animateX(1000);
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

        client = new OkHttpClient();
        request = new Request.Builder()
                .url(Const.SERVER_IP + "/auction/detail?date=" + requestDate)
                .method("GET", null)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            final String str = response.body().string();
            Log.d("tan", "response str:" + str);
            AuctionDetails details = LoganSquare.parse(str, AuctionDetails.class);
            Log.d("tan", "json decode: " + details.ToJsonString());

            generateDataAdapter(details);
            Log.d("tan", "refreshData: show chart");
            showChart();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateDataAdapter(AuctionDetails details) {

        final int size = details.Details.length;

        ArrayList<Entry> prices = new ArrayList<Entry>();

        for (int i = size - 1; i >= 0; i--) {
            AuctionDetail oneRow = details.Details[i];

            Entry price = new Entry(oneRow.Price, size - i - 1);

            prices.add(price);
            distances.add(String.valueOf(size - oneRow.Distance));

        }

        pricesDataSet = new LineDataSet(prices, requestDate);
        pricesDataSet.setLineWidth(3);
        pricesDataSet.setDrawCircles(false);
        pricesDataSet.setDrawValues(false);
        pricesDataSet.setMode(LineDataSet.Mode.STEPPED);
    }

    private void showChart() {
        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                LineData data = new LineData(distances, pricesDataSet);

                if (detailChart != null) {
                    detailChart.setData(data);
                    detailChart.invalidate();
                }
            }
        });
    }
}
