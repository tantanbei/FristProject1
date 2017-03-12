package com.whoplate.paipable.ui;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.MaterialIcons;
import com.joanzapata.iconify.fonts.TypiconsIcons;
import com.whoplate.paipable.App;
import com.whoplate.paipable.R;


public class XIconDrawable {
    //Note: analyser will complain that static drawable will leak context but we are using application context so the leak is safe...
    static public IconDrawable HOME_FOCUSED;
    static public IconDrawable HOME_UNFOCUSED;
    static public IconDrawable MINE_FOCUSED;
    static public IconDrawable MINE_UNFOCUSED;
    static public IconDrawable SWITCH_CAMERA;
    static public IconDrawable RECORD_START;
    static public IconDrawable RECORD_STOP;

    static {
        Load();
    }

    static public void Load() {
        HOME_FOCUSED = new IconDrawable(App.INSTANCE, TypiconsIcons.typcn_home)
                .colorRes(R.color.colorPrimary)
                .sizePx(XUI.DpToPx(20f));

        HOME_UNFOCUSED = new IconDrawable(App.INSTANCE, TypiconsIcons.typcn_home_outline)
                .colorRes(R.color.black)
                .sizePx(XUI.DpToPx(20f));

        MINE_FOCUSED = new IconDrawable(App.INSTANCE, TypiconsIcons.typcn_user)
                .colorRes(R.color.colorPrimary)
                .sizePx(XUI.DpToPx(20f));

        MINE_UNFOCUSED = new IconDrawable(App.INSTANCE, TypiconsIcons.typcn_user_outline)
                .colorRes(R.color.black)
                .sizePx(XUI.DpToPx(20f));
        SWITCH_CAMERA = new IconDrawable(App.INSTANCE, MaterialIcons.md_party_mode)
                .colorRes(R.color.white)
                .sizePx(XUI.DpToPx(20f));
        RECORD_START = new IconDrawable(App.INSTANCE, FontAwesomeIcons.fa_play_circle_o)
                .colorRes(R.color.white)
                .sizePx(XUI.DpToPx(50f));
        RECORD_STOP = new IconDrawable(App.INSTANCE, FontAwesomeIcons.fa_stop_circle_o)
                .colorRes(R.color.white)
                .sizePx(XUI.DpToPx(50f));

    }

}
