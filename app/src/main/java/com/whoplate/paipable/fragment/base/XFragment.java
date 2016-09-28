package com.whoplate.paipable.fragment.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public class XFragment extends Fragment {

    public Activity Parent;

    @Override
    public void onAttach(Context context) {

        try {
            super.onAttach(context);

            Parent = (Activity) context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
