package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;
import fristproject1.sample.com.fristproject1.http.Http;
import fristproject1.sample.com.fristproject1.networkpacket.Purchase_intent_car;
import fristproject1.sample.com.fristproject1.string.XString;
import fristproject1.sample.com.fristproject1.thread.XThread;
import okhttp3.Response;

//car style
//        0:德系
//        1:日系
//        2:美系
//        3:国产
//        4:其他

public class ActivityPurchaseIntent extends XActivity {

    TextView textAddCar;
    LinearLayout addCarRoot;
    Spinner intentCarStyle;
    RadioGroup haveOrNotCar;
    Button submit;

    ArrayList<Integer> haveCarStyles = new ArrayList<Integer>();
    ArrayList<Integer> haveCarAges = new ArrayList<Integer>();

    @Override
    public int GetContentView() {
        return R.layout.activity_purchase_intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.purchase_info);

        rigthBtn.setVisibility(View.VISIBLE);
        rigthBtn.setText(R.string.skip);

        textAddCar = (TextView) findViewById(R.id.text_add_car);
        addCarRoot = (LinearLayout) findViewById(R.id.add_car_layout);
        intentCarStyle = (Spinner) findViewById(R.id.intent_car_style);
        haveOrNotCar = (RadioGroup) findViewById(R.id.have_or_not_car);
        submit = (Button) findViewById(R.id.submit);

        haveOrNotCar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("tan", "onCheckedChanged: " + checkedId);
                switch (checkedId) {
                    case R.id.no_car:
                        Log.d("tan", "no car");
                        selectNoCar();
                        break;
                    case R.id.have_car:
                        Log.d("tan", "have car");
                        selectHaveCar();
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Purchase_intent_car packet = new Purchase_intent_car();
                packet.HaveORNotCar = haveOrNotCar.getCheckedRadioButtonId() == R.id.no_car ? false : true;
                packet.HaveCarStyles = haveCarStyles;
                packet.HaveCarAges = haveCarAges;
                packet.IntentCarStyle = intentCarStyle.getSelectedItemPosition();

                XThread.RunBackground(new Runnable() {
                    @Override
                    public void run() {
                        Response response = Http.Post(ActivityPurchaseIntent.this, Const.SERVER_IP + Const.URL_PURCHASE_INTENT, packet, true);
                        //todo response...

                    }
                });

            }
        });

        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMine();
            }
        });
    }

    private void selectNoCar() {
        textAddCar.setVisibility(View.GONE);
        addCarRoot.setVisibility(View.GONE);
        addCarRoot.removeAllViews();
        haveCarAges.clear();
        haveCarStyles.clear();
    }

    private void selectHaveCar() {
        textAddCar.setVisibility(View.VISIBLE);
        addCarRoot.setVisibility(View.VISIBLE);

        //add the first addCarView
        final LinearLayout addView = (LinearLayout) getLayoutInflater().inflate(R.layout.add_car, null);
        final Button add = (Button) addView.findViewById(R.id.add);
        final Spinner carStyle = (Spinner) addView.findViewById(R.id.car_style);
        final Spinner carAge = (Spinner) addView.findViewById(R.id.car_age);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carStyle.getVisibility() == View.GONE) {
                    carStyle.setVisibility(View.VISIBLE);
                    carAge.setVisibility(View.VISIBLE);
                    add.setText(R.string.confirm);
                } else {
                    if (add.getText().equals(XString.GetString(ActivityPurchaseIntent.this, R.string.confirm))) {
                        haveCarStyles.add(0, carStyle.getSelectedItemPosition());
                        haveCarAges.add(0, carAge.getSelectedItemPosition());
                        Log.d("tan", "add: carStyle:" + carStyle.getSelectedItemPosition() + " carAge:" + carAge.getSelectedItemPosition());
                        carStyle.setEnabled(false);
                        carAge.setEnabled(false);
                        add.setText(R.string.delete);
                    } else {
                        carStyle.setVisibility(View.GONE);
                        carAge.setVisibility(View.GONE);
                        carStyle.setEnabled(true);
                        carAge.setEnabled(true);
                        carStyle.setSelection(0);
                        carAge.setSelection(0);
                        haveCarAges.remove(0);
                        haveCarStyles.remove(0);
                        add.setText(R.string.add);
                    }
                }
            }
        });
        addCarRoot.addView(addView);
    }

    private void gotoMine() {
        finish();
    }
}
