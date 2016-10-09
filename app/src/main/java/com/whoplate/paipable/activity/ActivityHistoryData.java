package com.whoplate.paipable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.networkpacket.AuctionHistoryResult;
import com.whoplate.paipable.networkpacket.AuctionHistoryResults;
import com.whoplate.paipable.networkpacket.RequestAuctionHistoryPacket;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityHistoryData extends XActivity {

    final int SHOWLIMIATIONS = 1;
    final int SHOWPEOPLENUM = 2;
    final int SHOWMINIMUMPRICE = 3;
    final int SHOWAVERAGEPRICE = 4;
    final int SHOWCAUTIONPRICE = 5;
    int showType = 3;

    Spinner switchType;
    LineChart historyChart;
    TextView date;
    TextView limiation;
    TextView peopleNum;
    TextView minPrice;
    TextView averagePrice;
    TextView cautionPrice;

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
    public int GetContentView() {
        return R.layout.auction_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        historyChart = (LineChart) findViewById(R.id.history_chart);
        switchType = (Spinner) findViewById(R.id.switch_type);
        date = (TextView) findViewById(R.id.date);
        limiation = (TextView) findViewById(R.id.limiation);
        peopleNum = (TextView) findViewById(R.id.people_num);
        minPrice = (TextView) findViewById(R.id.min_price);
        averagePrice = (TextView) findViewById(R.id.average_price);
        cautionPrice = (TextView) findViewById(R.id.caution_price);

        title.setText(R.string.history_data);

        historyChart.setDragEnabled(true);
        historyChart.setScaleEnabled(true);
        historyChart.setTouchEnabled(true);
        historyChart.animateX(1000);

        historyChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
//                Log.d("tan", "onValueSelected: Entry:" + e.toString() + " dataSetIndex:" + dataSetIndex + " highlight:" + h);
                showDetailByIndex(e.getXIndex());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        switchType.setSelection(showType - 1);
        switchType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //need date is ready
                if (dates != null && dates.size() != 0) {
                    showChart(position + 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    private void showDetailByIndex(final int index) {
        if (dates == null || dates.size() == 1) {
            return;
        }

        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                date.setText(dates.get(index));
                limiation.setText(String.valueOf((int) limiations.get(index).getVal()));
                peopleNum.setText(String.valueOf((int) peopleNums.get(index).getVal()));
                minPrice.setText(String.valueOf((int) minimumPrices.get(index).getVal()));
                averagePrice.setText(String.valueOf((int) averagePrices.get(index).getVal()));
                cautionPrice.setText(String.valueOf((int) cautionPrices.get(index).getVal()));
            }
        });

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
            if (str == null || str.length() == 0) {
                return;
            }
            allHistoryCache = LoganSquare.parse(str, AuctionHistoryResults.class);
            if (allHistoryCache == null || allHistoryCache.auctionHistoryResults == null) {
                return;
            }

            generateDataAdapter();
            Log.d("tan", "refreshData: show chart");
            showChart(showType);
            showDetailByIndex(dates.size() - 1);

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

            Entry limiation = new Entry(oneRow.limitation, i);
            Entry peopleNum = new Entry(oneRow.peopleNumber, i);
            Entry minimimPrice = new Entry(oneRow.minimumPrice, i);
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
    }
}
