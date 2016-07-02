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
import fristproject1.sample.com.fristproject1.networkpacket.AuctionHistoryResult;
import fristproject1.sample.com.fristproject1.networkpacket.AuctionHistoryResults;
import fristproject1.sample.com.fristproject1.networkpacket.RequestAuctionHistoryPacket;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityHistoryData extends Activity {

    final int SHOWLIMIATIONS = 1;
    final int SHOWPEOPLENUM = 2;
    final int SHOWMINIMUMPRICE = 3;
    final int SHOWAVERAGEPRICE = 4;
    final int SHOWCAUTIONPRICE = 5;
    int showType = 3;

    LineChart historyChart;
    TextView title;
    IconTextView goBack;
    OkHttpClient client;
    Request request;

    AuctionHistoryResults allHistoryCache;
    boolean forceToRefreshData;

    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<Entry> limiations = new ArrayList<Entry>();
    ArrayList<Entry> peopleNums = new ArrayList<Entry>();
    ArrayList<Entry> minimumPrices = new ArrayList<Entry>();
    ArrayList<Entry> averagePrices = new ArrayList<Entry>();
    ArrayList<Entry> cautionPrices = new ArrayList<Entry>();

    LineDataSet limiationsDataSet;
    LineDataSet peopleNumsDataSet;
    LineDataSet minimumPricesDataSet;
    LineDataSet averagePricesDataSet;
    LineDataSet cautionPricesDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_history);

        historyChart = (LineChart) findViewById(R.id.history_chart);
        title = (TextView) findViewById(R.id.title);
        goBack = (IconTextView) findViewById(R.id.goBack);

        title.setText(R.string.history_data);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        historyChart.setDragEnabled(true);
        historyChart.setScaleEnabled(true);
        historyChart.setTouchEnabled(true);
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
        Log.d("tan", "refreshData: " + requestAuctionHistoryPacket.ToJsonString());
        client = new OkHttpClient();
        request = new Request.Builder()
                .url(Const.SERVER_IP + "/auction/history?request=" + requestAuctionHistoryPacket.ToJsonString())
                .method("GET", null)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            final String str = response.body().string();
            Log.d("tan", "str: " + str);
            allHistoryCache = LoganSquare.parse(str, AuctionHistoryResults.class);
            Log.d("tan", "refreshData: " + allHistoryCache.toString());

            generateDataAdapter();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showChart(final int type) {
        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                LineData data = new LineData();
                switch (type) {
                    case SHOWLIMIATIONS:
                        data = new LineData(dates, limiationsDataSet);
                        break;
                    case SHOWPEOPLENUM:
                        data = new LineData(dates, peopleNumsDataSet);
                        break;
                    case SHOWMINIMUMPRICE:
                        data = new LineData(dates, minimumPricesDataSet);
                        break;
                    case SHOWAVERAGEPRICE:
                        data = new LineData(dates, averagePricesDataSet);
                        break;
                    case SHOWCAUTIONPRICE:
                        data = new LineData(dates, cautionPricesDataSet);
                        break;
                }

                if (historyChart != null) {
                    historyChart.setData(data);
                    historyChart.invalidate();
                } else {
                    Log.d("tan", "historyChart is null ");
                }
            }
        });
    }

    private void generateDataAdapter() {
        final int size = allHistoryCache.auctionHistoryResults.length;

        for (int i = 0; i < size; i++) {
            AuctionHistoryResult oneRow = allHistoryCache.auctionHistoryResults[i];

            Entry limiation = new Entry(oneRow.limiation, i);
            Entry peopleNum = new Entry(oneRow.peopleNumber, i);
            Entry minimimPrice = new Entry(oneRow.minumumPrice, i);
            Entry averagePrice = new Entry(oneRow.averagePrice, i);
            Entry cautionPrice = new Entry(oneRow.cautionPrice, i);

            limiations.add(limiation);
            peopleNums.add(peopleNum);
            minimumPrices.add(minimimPrice);
            averagePrices.add(averagePrice);
            cautionPrices.add(cautionPrice);
            dates.add(oneRow.date);

        }

        limiationsDataSet = new LineDataSet(limiations, getString(R.string.limiation));
        peopleNumsDataSet = new LineDataSet(peopleNums, getString(R.string.people_num));
        minimumPricesDataSet = new LineDataSet(minimumPrices, getString(R.string.min_price));
        averagePricesDataSet = new LineDataSet(averagePrices, getString(R.string.average_price));
        cautionPricesDataSet = new LineDataSet(cautionPrices, getString(R.string.caution_price));

        showChart(SHOWAVERAGEPRICE);
    }
}
