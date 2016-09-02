package fristproject1.sample.com.fristproject1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.base.XActivity;

public class ActivityInitUserInfo extends XActivity{
    @Override
    public int GetContentView() {
        return R.layout.activity_init_user_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("设置用户信息");
        rigthBtn.setText("下一步");
        rigthBtn.setVisibility(View.VISIBLE);
        
        rigthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO:

                startActivity(new Intent(ActivityInitUserInfo.this,ActivityPurchaseIntent.class));
                finish();
            }
        });

    }
}
