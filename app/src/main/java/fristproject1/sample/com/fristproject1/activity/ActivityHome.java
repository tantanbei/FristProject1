package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.widget.IconTextView;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.String.XString;
import fristproject1.sample.com.fristproject1.fragment.HomeTabFragment;

public class ActivityHome extends AppCompatActivity {

    private FrameLayout homeFramelayout;
    private DrawerLayout homeDrawerlayout;
    private LinearLayout tabHome;
    private LinearLayout tabMine;
    private IconTextView tabHomeIcon;
    private IconTextView tabMineIcon;
    private ListView homeDrawer;

    private View.OnClickListener tagClickListener;

    final private String[] strs = new String[]{"{md-headset-mic}  联系客服"
            , "{entypo-new-message}  意见反馈"
            , "{md-info-outline}  关于我们"
            , "{fa-gear}  设置"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        homeFramelayout = (FrameLayout) findViewById(R.id.home_frame_layout);
        homeDrawerlayout = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        tabHome = (LinearLayout) findViewById(R.id.tab_home);
        tabMine = (LinearLayout) findViewById(R.id.tab_mine);
        tabHomeIcon = (IconTextView) findViewById(R.id.tab_home_icon);
        tabMineIcon = (IconTextView) findViewById(R.id.tab_mine_icon);
        homeDrawer = (ListView) findViewById(R.id.home_drawer);

        tagClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viewId = v.getId();

                switch (viewId) {
                    case R.id.tab_home:
                        tabHomeIcon.setText("{typcn-home}");
                        tabMineIcon.setText("{typcn-user-outline}");
                        break;
                    case R.id.tab_mine:
                        tabHomeIcon.setText("{typcn-home-outline}");
                        tabMineIcon.setText("{typcn-user}");
                        break;
                }
            }
        };

        tabHome.setOnClickListener(tagClickListener);
        tabMine.setOnClickListener(tagClickListener);

        //set home drawer listview
        homeDrawer.setAdapter(new ArrayAdapter(this, R.layout.home_drawer_row, strs));

        //manage the fragment
        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction transaction = FM.beginTransaction();
        HomeTabFragment homeTabFragment = new HomeTabFragment();
        transaction.replace(R.id.home_frame_layout, homeTabFragment, "HomeTabFragment");
        transaction.commit();

    }
}
