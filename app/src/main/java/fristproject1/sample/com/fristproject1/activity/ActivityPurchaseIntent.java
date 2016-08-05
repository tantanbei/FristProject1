package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;

public class ActivityPurchaseIntent extends XActivity {
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

        LinearLayout ll = (LinearLayout) findViewById(R.id.add_car_layout);

        //add the first addCarView
        LinearLayout addView = (LinearLayout) getLayoutInflater().inflate(R.layout.add_car, null);
        ll.addView(addView);
    }
}
