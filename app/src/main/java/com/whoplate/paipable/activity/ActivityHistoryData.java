package com.whoplate.paipable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.AuctionHistoryResult;
import com.whoplate.paipable.networkpacket.AuctionHistoryResults;
import com.whoplate.paipable.networkpacket.RequestAuctionHistoryPacket;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityHistoryData extends XActivity {

    final int SHOW_LIMITATIONS = 1;
    final int SHOW_PEOPLE_NUM = 2;
    final int SHOW_MINIMUM_PRICE = 3;
    final int SHOW_AVERAGE_PRICE = 4;
    final int SHOW_CAUTION_PRICE = 5;
    int showType = 3;

    Spinner switchType;
    LineChart historyChart;
    TextView date;
    TextView limitation;
    TextView peopleNum;
    TextView minPrice;
    TextView averagePrice;
    TextView cautionPrice;
    Button priceDetail;

    AuctionHistoryResults allHistoryCache;
    boolean forceToRefreshData;

    ArrayList<String> dates = new ArrayList<String>();
    ArrayList<Entry> limitations = new ArrayList<Entry>();
    ArrayList<Entry> peopleNums = new ArrayList<Entry>();
    ArrayList<Entry> minimumPrices = new ArrayList<Entry>();
    ArrayList<Entry> averagePrices = new ArrayList<Entry>();
    ArrayList<Entry> cautionPrices = new ArrayList<Entry>();

    LineDataSet limitationsDataSet;
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
        limitation = (TextView) findViewById(R.id.limiation);
        peopleNum = (TextView) findViewById(R.id.people_num);
        minPrice = (TextView) findViewById(R.id.min_price);
        averagePrice = (TextView) findViewById(R.id.average_price);
        cautionPrice = (TextView) findViewById(R.id.caution_price);
        priceDetail = (Button) findViewById(R.id.detail);

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

        priceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityHistoryData.this, ActivityDataDetail.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        });
    }

    private void showDetailByIndex(final int index) {
        if (dates == null || dates.size() == 1) {
            return;
        }

        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                date.setText(dates.get(index));
                limitation.setText(String.valueOf((int) limitations.get(index).getVal()));
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

        try {

            Response response = Http.Get(Const.URL_APN + "/auction/history?request=" + requestAuctionHistoryPacket.ToJsonString());
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
            XDebug.Handle(e);
        }
    }

    private void showChart(final int type) {
        App.Uihandler.post(new Runnable() {
            @Override
            public void run() {
                LineData data = new LineData();
                switch (type) {
                    case SHOW_LIMITATIONS:
                        data = new LineData(dates, limitationsDataSet);
                        break;
                    case SHOW_PEOPLE_NUM:
                        data = new LineData(dates, peopleNumsDataSet);
                        break;
                    case SHOW_MINIMUM_PRICE:
                        data = new LineData(dates, minimumPricesDataSet);
                        break;
                    case SHOW_AVERAGE_PRICE:
                        data = new LineData(dates, averagePricesDataSet);
                        break;
                    case SHOW_CAUTION_PRICE:
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

            limitations.add(limiation);
            peopleNums.add(peopleNum);
            minimumPrices.add(minimimPrice);
            averagePrices.add(averagePrice);
            cautionPrices.add(cautionPrice);
            dates.add(oneRow.date);

        }

        limitationsDataSet = new LineDataSet(limitations, getString(R.string.limiation));
        peopleNumsDataSet = new LineDataSet(peopleNums, getString(R.string.people_num));
        minimumPricesDataSet = new LineDataSet(minimumPrices, getString(R.string.min_price));
        averagePricesDataSet = new LineDataSet(averagePrices, getString(R.string.average_price));
        cautionPricesDataSet = new LineDataSet(cautionPrices, getString(R.string.caution_price));
    }
}
