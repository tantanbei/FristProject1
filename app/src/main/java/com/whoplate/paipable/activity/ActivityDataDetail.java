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
import com.whoplate.paipable.http.Http;
import com.whoplate.paipable.networkpacket.AuctionDetail;
import com.whoplate.paipable.networkpacket.AuctionDetails;
import com.whoplate.paipable.networkpacket.Paper;
import com.whoplate.paipable.networkpacket.PaperFilter;
import com.whoplate.paipable.networkpacket.base.AuctionDetailDates;
import com.whoplate.paipable.networkpacket.base.Papers;
import com.whoplate.paipable.thread.XThread;
import com.whoplate.paipable.time.XTime;
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

    private MyRecycleViewAdapter adapter = null;

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
                    Response response = Http.Get(Const.SERVER_IP + "/auction/detail?date=" + requestDate);
                    AuctionDetails details = LoganSquare.parse(response.body().byteStream(), AuctionDetails.class);

                    generateDataAdapter(details, requestDate);
                    showChart();

                    PaperFilter filter = new PaperFilter();
                    Log.d("tan", "run: "+Const.DataKeywords);
                    filter.KeywordIds.add(Const.DataKeywords.indexOf(requestDate));

                    Log.d("tan", "run: " + filter.KeywordIds);
                    Response responseMessage = Http.Post(Const.SERVER_IP + "/paper/get/filter" ,LoganSquare.serialize(filter));
                    final Papers papers = LoganSquare.parse(responseMessage.body().byteStream(), Papers.class);

                    App.Uihandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (papers == null) {
                                showEmpty();
                                return;
                            } else {
                                hideEmpty();

                                adapter = new MyRecycleViewAdapter(ActivityDataDetail.this, papers.Data);
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
        empty.setVisibility(View.VISIBLE);
        messages.setVisibility(View.GONE);
    }

    private void hideEmpty() {
        messages.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
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

    public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.myViewHolder> {
        private ArrayList<Paper> data;
        private WeakReference<Activity> a;

        public MyRecycleViewAdapter(Activity a, ArrayList<Paper> data) {
            this.data = data;
            this.a = new WeakReference<Activity>(a);
        }

        @Override
        public MyRecycleViewAdapter.myViewHolder onCreateViewHolder(ViewGroup vg, int viewType) {
            View view = LayoutInflater.from(vg.getContext()).inflate(R.layout.row_paper, vg, false);
            return new MyRecycleViewAdapter.myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyRecycleViewAdapter.myViewHolder holder, final int position) {
            Log.d("tan", "onBindViewHolder: " + data.get(position).Title);
            holder.title.setText(data.get(position).Title);
            holder.date.setText(XTime.TimeStampToDate(data.get(position).DateSubmit * 1000L));
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityDataDetail.this, ActivityWebView.class);
                    intent.putExtra("paperid", data.get(position).PaperId);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class myViewHolder extends RecyclerView.ViewHolder {
            public View root;
            public TextView title;
            public TextView date;

            public myViewHolder(View itemView) {
                super(itemView);

                root = itemView;
                title = (TextView) itemView.findViewById(R.id.paper_title);
                date = (TextView) itemView.findViewById(R.id.date);
            }
        }
    }
}
