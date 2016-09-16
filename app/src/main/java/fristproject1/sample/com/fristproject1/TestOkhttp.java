package fristproject1.sample.com.fristproject1;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bluelinelabs.logansquare.LoganSquare;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;
import java.util.ArrayList;

import fristproject1.sample.com.fristproject1.networkpacket.CarPriceOneTime;
import fristproject1.sample.com.fristproject1.networkpacket.CarPrices;
import fristproject1.sample.com.fristproject1.toast.XToast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestOkhttp extends Activity {
    private String TAG = "tan";

    Button btSend;
    EditText etCarId;
    LineChart chart;

    Call call;
    OkHttpClient client;
    Request request;

    private String carId;
    private CarPrices carPrices;
    private CarPriceOneTime carPriceOneTime;

    private ArrayList<Entry> realPrices = new ArrayList<Entry>();
    private ArrayList<String> realTimes = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testokhttp);

//        final TextView tvShow = (TextView) findViewById(R.id.tvShow);
        btSend = (Button) findViewById(R.id.btnSend);
        etCarId = (EditText) findViewById(R.id.etCarId);
        chart = (LineChart) findViewById(R.id.chartCar);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setTouchEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

//        tvShow.setMovementMethod(ScrollingMovementMethod.getInstance());

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carId = etCarId.getText().toString();
                if (carId == "" || carId == null) {
                    XToast.Show("need input the car id!!!");
                    return;
                }

                client = new OkHttpClient();
                request = new Request.Builder()
                        .url("http://192.168.0.106:8080/chexiang/produce/id?id=" + carId)
//                        .url("https://github.com/hongyangAndroid")
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

                        carPrices = LoganSquare.parse(str, CarPrices.class);

                        Log.d("tan", "carPrices: " + carPrices.toString());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {
                                refreshData();
                            }
                        });

                    }
                });
            }
        });
    }

    private void setDataFromNetwork() {
        if (carPrices == null) {
            Log.e(TAG, "setDataFromNetwork: CarPrices is null!");
            return;
        }

//        Entry entry = new Entry(0f, 0);
        int size = carPrices.carPrice.size();
        for (int n = 0; n < size; n++) {
            carPriceOneTime = carPrices.carPrice.get(n);

            //update the entry value
//            entry.setVal(Float.valueOf(carPriceOneTime.price));
//            entry.setXIndex(n);
            realPrices.add(new Entry(Float.valueOf(carPriceOneTime.price),n));
            realTimes.add(carPriceOneTime.time);
        }
    }

    private void refreshData() {

        chart.clear();
        realPrices.clear();
        realTimes.clear();

        setDataFromNetwork();

        LineDataSet lineDataSet = new LineDataSet(realPrices, carId);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(realTimes, dataSets);

        Log.d(TAG, "refreshData: " + realTimes.size() + " " + realPrices.size());

        chart.setData(data);

        chart.invalidate();
    }

}
