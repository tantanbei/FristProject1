package com.whoplate.paipable.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.AuctionDetail;
import com.whoplate.paipable.networkpacket.AuctionDetails;
import com.whoplate.paipable.networkpacket.base.AuctionDetailDates;
import com.whoplate.paipable.thread.XThread;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityDataDetail extends XActivity {

    Spinner datesSpinner;
    LineChart detailChart;

    ArrayList<String> dates = new ArrayList<>();

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
                    Response response = Http.Get(Const.SERVER_IP + Const.URL_AUCTION_DETAIL_DATES);
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
                    e.printStackTrace();
                }
            }
        });
    }

    private void showChartRequest(final String requestDate) {
        XThread.RunBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = Http.Get(Const.SERVER_IP + "/auction/detail?date=" + requestDate);
                    AuctionDetails details = LoganSquare.parse(response.body().byteStream(), AuctionDetails.class);

                    generateDataAdapter(details, requestDate);
                    showChart();

                } catch (IOException e) {
                    e.printStackTrace();
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
