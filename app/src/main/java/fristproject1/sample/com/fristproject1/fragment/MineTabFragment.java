package fristproject1.sample.com.fristproject1.fragment;

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

import fristproject1.sample.com.fristproject1.R;
import fristproject1.sample.com.fristproject1.activity.ActivitySignIn;
import fristproject1.sample.com.fristproject1.activity.ActivityUserInfo;
import fristproject1.sample.com.fristproject1.db.Pref;
import fristproject1.sample.com.fristproject1.fragment.base.XFragment;
import fristproject1.sample.com.fristproject1.string.XString;

public class MineTabFragment extends XFragment {

    View view;

    LinearLayout userInfo;
    TextView username;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.mine_tab_fragment, container, false);

        IconTextView back = (IconTextView) view.findViewById(R.id.goBack);
        TextView title = (TextView) view.findViewById(R.id.title);

        back.setVisibility(View.GONE);
        title.setText(R.string.user_info);

        userInfo = (LinearLayout) view.findViewById(R.id.user_info);

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Parent, ActivityUserInfo.class));
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
        username.setText(Pref.Get(Pref.USERNAME, XString.GetString(Parent, R.string.app_name)));
    }
}
