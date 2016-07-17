package fristproject1.sample.com.fristproject1.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;

import fristproject1.sample.com.fristproject1.Const;
import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.ActivityAuction;
import fristproject1.sample.com.fristproject1.activity.ActivityHistoryData;
import fristproject1.sample.com.fristproject1.activity.ActivityHome;

public class HomeTabFragment extends Fragment {

    Activity Parent;

    private IconTextView homeMenu;
    private IconTextView homeCustomerService;
    private ViewPager homeViewPager;
    private LinearLayout auction;
    private TextView historyData;
    private LinearLayout dataDetail;

    private ArrayList<View> ImageArrayList = new ArrayList<View>();
    private int[] srcIds = {R.mipmap.home_first_pager, R.mipmap.home_second_pager, R.mipmap.home_third_pager};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_tab_fragment, container, false);

        homeMenu = (IconTextView) view.findViewById(R.id.home_menu);
        homeCustomerService = (IconTextView) view.findViewById(R.id.home_customer_service);
        homeViewPager = (ViewPager) view.findViewById(R.id.home_view_pager);
        auction = (LinearLayout) view.findViewById(R.id.auction_idle);
        historyData = (TextView) view.findViewById(R.id.history_data);

        for (int i = 0; i < srcIds.length; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(srcIds[i]);
            ImageArrayList.add(imageView);
        }

        homeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityHome) Parent).OpenDrawer();
            }
        });

        homeCustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Const.CUSTOMER_SERVICE_PHONE));
                startActivity(intent);
            }
        });

        homeViewPager.setAdapter(new HomeViewPagerAdapter());

        auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityAuction.class));
            }
        });

        historyData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityHistoryData.class));
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

    @Override
    public void onAttach(Context context) {

        try {
            super.onAttach(context);

            Parent = (ActivityHome) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
