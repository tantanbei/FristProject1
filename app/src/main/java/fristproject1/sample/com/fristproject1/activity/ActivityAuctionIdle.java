package fristproject1.sample.com.fristproject1.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import fristproject1.sample.com.fristproject1.R;

public class ActivityAuctionIdle extends Activity {

    private ArrayList<View> ImageArrayList = new ArrayList<View>();
    private int[] srcIds = {R.mipmap.auction_idle_first, R.mipmap.auction_idle_second};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_auction_idle);

        ViewPager viewPager = (ViewPager) findViewById(R.id.acution_idle_view_page);

        for (int i = 0; i < srcIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(srcIds[i]);
            ImageArrayList.add(imageView);
        }

        viewPager.setAdapter(new AuctionIdleViewPagerAdapter());
    }

    public class AuctionIdleViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ImageArrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(ImageArrayList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(ImageArrayList.get(position % ImageArrayList.size()), 0);
            return ImageArrayList.get(position % ImageArrayList.size());
        }
    }
}
