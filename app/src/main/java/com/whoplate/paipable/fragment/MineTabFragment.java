package com.whoplate.paipable.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;
import com.whoplate.paipable.R;
import com.whoplate.paipable.activity.ActivitySignIn;
import com.whoplate.paipable.activity.ActivityUserInfo;
import com.whoplate.paipable.db.Pref;
import com.whoplate.paipable.fragment.base.XFragment;
import com.whoplate.paipable.string.XString;
import com.whoplate.paipable.ui.XView;

public class MineTabFragment extends XFragment {

    View view;

    LinearLayout userInfo;
    TextView username;

    TextView point;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.mine_tab_fragment, container, false);

        IconTextView back = (IconTextView) view.findViewById(R.id.goBack);
        TextView title = (TextView) view.findViewById(R.id.title);

        XView.Hide(back);
        title.setText(R.string.user_info);

        userInfo = (LinearLayout) view.findViewById(R.id.user_info);
        point = (TextView) view.findViewById(R.id.point);

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivityUserInfo.class));
            }
        });

        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivitySignIn.class));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (view == null) {
            return;
        }

        username = (TextView) view.findViewById(R.id.username);
        username.setText(Pref.Get(Pref.USERNAME, XString.GetString(R.string.app_name)));
    }
}
