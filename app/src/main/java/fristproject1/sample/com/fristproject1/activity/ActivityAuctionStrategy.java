package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;

public class ActivityAuctionStrategy extends XActivity {

    TextView second30;
    TextView second40;
    TextView second45;
    TextView second50;

    @Override
    public int GetContentView() {
        return R.layout.activity_auction_strategy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        second30 = (TextView) findViewById(R.id.second_30);
        second40 = (TextView) findViewById(R.id.second_40);
        second45 = (TextView) findViewById(R.id.second_45);
        second50 = (TextView) findViewById(R.id.second_50);

    }
}
