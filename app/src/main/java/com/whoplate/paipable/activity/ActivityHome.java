package com.whoplate.paipable.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.joanzapata.iconify.widget.IconTextView;
import com.whoplate.paipable.Const;
import com.whoplate.paipable.R;
import com.whoplate.paipable.fragment.HomeTabFragment;
import com.whoplate.paipable.fragment.MineTabFragment;
import com.whoplate.paipable.session.XSession;
import com.whoplate.paipable.stack.XStack;

public class ActivityHome extends AppCompatActivity {
    private int HOMEFRAGMENT = 0;
    private int MINEFRAGMENT = 1;

    private DrawerLayout homeDrawerLayout;
    private LinearLayout tabHome;
    private LinearLayout tabMine;
    private IconTextView tabHomeIcon;
    private IconTextView tabMineIcon;
    private ListView homeDrawer;

    private View.OnClickListener tagClickListener;

    //manage the fragment
    private FragmentManager FM;
    private int fragmentType = HOMEFRAGMENT;

    final private String[] strs = new String[]{"{md-headset-mic}  联系客服"
            , "{entypo-new-message}  意见反馈"
            , "{md-info-outline}  关于我们"
            , "{fa-gear}  设置"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        XStack.Push(this);

        setContentView(R.layout.activity_home);

        homeDrawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        tabHome = (LinearLayout) findViewById(R.id.tab_home);
        tabMine = (LinearLayout) findViewById(R.id.tab_mine);
        tabHomeIcon = (IconTextView) findViewById(R.id.tab_home_icon);
        tabMineIcon = (IconTextView) findViewById(R.id.tab_mine_icon);
        homeDrawer = (ListView) findViewById(R.id.home_drawer);

        tagClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viewId = v.getId();

                FragmentTransaction transaction = FM.beginTransaction();

                switch (viewId) {
                    case R.id.tab_home:
                        if (fragmentType == HOMEFRAGMENT) {
                            return;
                        }

                        tabHomeIcon.setText("{typcn-home}");
                        tabMineIcon.setText("{typcn-user-outline}");


                        HomeTabFragment homeTabFragment = new HomeTabFragment();
                        transaction.replace(R.id.home_frame_layout, homeTabFragment, "HomeTabFragment");
                        transaction.commit();

                        fragmentType = HOMEFRAGMENT;
                        break;
                    case R.id.tab_mine:
                        if (fragmentType == MINEFRAGMENT) {
                            return;
                        }

                        tabHomeIcon.setText("{typcn-home-outline}");
                        tabMineIcon.setText("{typcn-user}");

                        MineTabFragment mineTabFragment = new MineTabFragment();
                        transaction.replace(R.id.home_frame_layout, mineTabFragment, "HomeTabFragment");
                        transaction.commit();

                        fragmentType = MINEFRAGMENT;
                        break;
                }
            }
        };

        tabHome.setOnClickListener(tagClickListener);
        tabMine.setOnClickListener(tagClickListener);

        //set home drawer listview
        homeDrawer.setAdapter(new ArrayAdapter(this, R.layout.home_drawer_row, strs));
        homeDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Const.CUSTOMER_SERVICE_PHONE));
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(ActivityHome.this, ActivityFeedback.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        //manage the fragment
        FM = getSupportFragmentManager();
        FragmentTransaction transaction = FM.beginTransaction();
        HomeTabFragment homeTabFragment = new HomeTabFragment();
        transaction.replace(R.id.home_frame_layout, homeTabFragment, "HomeTabFragment");
        transaction.commit();

    }

    public void OpenDrawer() {
        homeDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    protected void onStart() {
        super.onStart();

        XStack.Push(this);

        //check the session
        if (!XSession.IsValid()) {
            Log.d("tan", "onClick: go to sign in");
            startActivity(new Intent(this, ActivityLogIn.class));
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        XStack.Push(this);
        Log.d("tan", "onResume: xstack size:" + XStack.Size());
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
}