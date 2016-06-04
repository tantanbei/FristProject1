package fristproject1.sample.com.fristproject1.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.widget.IconTextView;

import fristproject1.sample.com.fristproject1.R;

public class ActivityHome extends AppCompatActivity {

    private FrameLayout homeFramelayout;

    private LinearLayout tabHome;
    private LinearLayout tabFavousable;
    private LinearLayout tabDiscover;
    private LinearLayout tabMine;
    private IconTextView tabHomeIcon;
    private IconTextView tabFavousableIcon;
    private IconTextView tabDiscoverIcon;
    private IconTextView tabMineIcon;

    private View.OnClickListener tagClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new TypiconsModule());
        setContentView(R.layout.activity_home);

        homeFramelayout = (FrameLayout) findViewById(R.id.home_frame_layout);
        tabHome = (LinearLayout) findViewById(R.id.tab_home);
        tabFavousable = (LinearLayout) findViewById(R.id.tab_favourable);
        tabDiscover = (LinearLayout) findViewById(R.id.tab_discover);
        tabMine = (LinearLayout) findViewById(R.id.tab_mine);
        tabHomeIcon = (IconTextView) findViewById(R.id.tab_home_icon);
        tabFavousableIcon = (IconTextView) findViewById(R.id.tab_favourable_icon);
        tabDiscoverIcon = (IconTextView) findViewById(R.id.tab_discover_icon);
        tabMineIcon = (IconTextView) findViewById(R.id.tab_mine_icon);

        tagClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viewId = v.getId();

                switch (viewId){
                    case R.id.tab_home:
                        tabHomeIcon.setText("{typcn-home}");
                        tabFavousableIcon.setText("{typcn-tag}");
                        tabDiscoverIcon.setText("{typcn-world-outline}");
                        tabMineIcon.setText("{typcn-user-outline}");
                        break;
                    case R.id.tab_favourable:
                        tabHomeIcon.setText("{typcn-home-outline}");
                        tabFavousableIcon.setText("{typcn-tags}");
                        tabDiscoverIcon.setText("{typcn-world-outline}");
                        tabMineIcon.setText("{typcn-user-outline}");
                        break;
                    case R.id.tab_discover:
                        tabHomeIcon.setText("{typcn-home-outline}");
                        tabFavousableIcon.setText("{typcn-tag}");
                        tabDiscoverIcon.setText("{typcn-world}");
                        tabMineIcon.setText("{typcn-user-outline}");
                        break;
                    case R.id.tab_mine:
                        tabHomeIcon.setText("{typcn-home-outline}");
                        tabFavousableIcon.setText("{typcn-tag}");
                        tabDiscoverIcon.setText("{typcn-world-outline}");
                        tabMineIcon.setText("{typcn-user}");
                        break;
                }
            }
        };

        tabHome.setOnClickListener(tagClickListener);
        tabFavousable.setOnClickListener(tagClickListener);
        tabDiscover.setOnClickListener(tagClickListener);
        tabMine.setOnClickListener(tagClickListener);
    }
}
