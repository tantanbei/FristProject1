package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.widget.IconTextView;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.fragment.HomeTabFragment;

public class ActivityHome extends AppCompatActivity {

    private FrameLayout homeFramelayout;

    private LinearLayout tabHome;
    private LinearLayout tabMine;
    private IconTextView tabHomeIcon;
    private IconTextView tabMineIcon;

    private View.OnClickListener tagClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        homeFramelayout = (FrameLayout) findViewById(R.id.home_frame_layout);
        tabHome = (LinearLayout) findViewById(R.id.tab_home);
        tabMine = (LinearLayout) findViewById(R.id.tab_mine);
        tabHomeIcon = (IconTextView) findViewById(R.id.tab_home_icon);
        tabMineIcon = (IconTextView) findViewById(R.id.tab_mine_icon);

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

        FragmentManager FM = getSupportFragmentManager();
        FragmentTransaction transaction = FM.beginTransaction();
        HomeTabFragment homeTabFragment = new HomeTabFragment();
        transaction.add(R.id.home_frame_layout, homeTabFragment, "HomeTabFragment");
        transaction.commit();

    }


}
