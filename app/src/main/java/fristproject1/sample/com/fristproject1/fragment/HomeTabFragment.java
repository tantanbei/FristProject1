package fristproject1.sample.com.fristproject1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.ActivityAuction;

public class HomeTabFragment extends Fragment {

    private ViewPager homeViewPager;
    private TextView auction;
    private ArrayList<View> ImageArrayList = new ArrayList<View>();
    private int[] srcIds = {R.mipmap.home_first_pager, R.mipmap.home_second_pager, R.mipmap.home_third_pager};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_tab_fragment, container, false);

        homeViewPager = (ViewPager) view.findViewById(R.id.home_view_pager);
        auction = (TextView) view.findViewById(R.id.auction);

        for (int i = 0; i < srcIds.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(srcIds[i]);
            ImageArrayList.add(imageView);
        }

        homeViewPager.setAdapter(new HomeViewPagerAdapter());

        auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityAuction.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public class HomeViewPagerAdapter extends PagerAdapter {

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
