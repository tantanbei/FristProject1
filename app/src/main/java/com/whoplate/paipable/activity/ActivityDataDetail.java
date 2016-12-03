package com.whoplate.paipable.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.whoplate.paipable.App;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.base.XActivity;
import com.whoplate.paipable.adapter.PaperListRecycleViewAdapter;
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.itemDecoration.LinearLayoutColorDivider;
import com.whoplate.paipable.networkpacket.AuctionDetail;
import com.whoplate.paipable.networkpacket.AuctionDetails;
import com.whoplate.paipable.networkpacket.Paper;
import com.whoplate.paipable.networkpacket.PaperFilter;
import com.whoplate.paipable.networkpacket.base.AuctionDetailDates;
import com.whoplate.paipable.networkpacket.base.Papers;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.time.XTime;
import com.whoplate.paipable.ui.XView;
import com.whoplate.paipable.util.XDebug;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import okhttp3.Response;

public class ActivityDataDetail extends XActivity {

    private Spinner datesSpinner;
    private LineChart detailChart;
    private RecyclerView messages;
    private TextView empty;

    ArrayList<String> dates = new ArrayList<>();

    private PaperListRecycleViewAdapter adapter = null;

    LineDataSet pricesDataSet;
    ArrayList<String> distances = new ArrayList<String>();

    @Override
    public int GetContentView() {
        return R.layout.activity_deitail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datesSpinner = (Spinner) findViewById(R.id.dates);
        detailChart = (LineChart) findViewById(R.id.detail_chart);
        messages = (RecyclerView) findViewById(R.id.messages);
        empty = (TextView) findViewById(R.id.empty);

        title.setText(R.string.data_detail);

        detailChart.setDragEnabled(true);
        detailChart.setScaleEnabled(true);
        detailChart.setTouchEnabled(true);
        detailChart.animateX(1000);

        datesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String requestDate = parent.getItemAtPosition(position).toString();
                showChartRequest(requestDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        datesSpinner.setSelection(0);

        messages.setLayoutManager(new LinearLayoutManager(this));
        messages.addItemDecoration(new LinearLayoutColorDivider());
    }

    @Override
    protected void onStart() {
        super.onStart();

        refreshData();
    }

    private void refreshData() {

        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {

                try {
                    Response response = Http.Get(Const.URL_APN + Const.URL_AUCTION_DETAIL_DATES);
                    AuctionDetailDates datesResult = LoganSquare.parse(response.body().byteStream(), AuctionDetailDates.class);
                    dates = datesResult.Dates;

                    final mySpinnerAdapter mySpinnerAdapter = new mySpinnerAdapter(dates);

                    App.Uihandler.post(new Runnable() {
                        @Override
                        public void run() {
                            datesSpinner.setAdapter(mySpinnerAdapter);
                            datesSpinner.setSelection(0);
                        }
                    });

                } catch (IOException e) {
                    XDebug.Handle(e);
                }
            }
        });
    }

    private void showChartRequest(final String requestDate) {
        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Http.Get(Const.URL_APN + Const.URL_AUCTION_DETAIL + "?date=" + requestDate);
                    AuctionDetails details = LoganSquare.parse(response.body().byteStream(), AuctionDetails.class);

                    generateDataAdapter(details, requestDate);
                    showChart();

                    PaperFilter filter = new PaperFilter();
                    Log.d("tan", "run: " + Const.DataKeywords);
                    filter.KeywordIds.add(Const.DataKeywords.indexOf(requestDate));

                    Log.d("tan", "run: " + filter.KeywordIds);
                    Response responseMessage = Http.Post(Const.URL_APN + "/paper/get/filter", filter);
                    final Papers papers = LoganSquare.parse(responseMessage.body().byteStream(), Papers.class);

                    App.Uihandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (papers == null) {
                                showEmpty();
                                return;
                            } else {
                                hideEmpty();

                                adapter = new PaperListRecycleViewAdapter(ActivityDataDetail.this, papers.Data);
                                messages.setAdapter(adapter);
                            }
                        }
                    });

                } catch (IOException e) {
                    XDebug.Handle(e);
                }
            }
        });
    }

    private void generateDataAdapter(AuctionDetails details, String requestDate) {

        final int size = details.Details.length;
        distances.clear();

        ArrayList<Entry> prices = new ArrayList<Entry>();

        for (int i = 0; i < size; i++) {
            AuctionDetail oneRow = details.Details[i];

            Entry price = new Entry(oneRow.Price, i);

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

    private void showEmpty() {
        XView.Show(empty);
        XView.Hide(messages);
    }

    private void hideEmpty() {
        XView.Show(messages);
        XView.Hide(empty);
    }

    class mySpinnerAdapter extends BaseAdapter {
        private ArrayList<String> dates;

        public mySpinnerAdapter(ArrayList<String> dates) {
            this.dates = dates;
        }

        @Override
        public int getCount() {
            return dates.size();
        }

        @Override
        public Object getItem(int position) {
            return dates.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(ActivityDataDetail.this);
            tv.setText(dates.get(position));
            tv.setTextSize(18f);
            tv.setPadding(20, 10, 20, 10);
            return tv;
        }
    }
}
