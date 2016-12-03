package com.whoplate.paipable.ui;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.whoplate.paipable.App;
import com.whoplate.paipable.R;
import com.whoplate.paipable.util.XDebug;

public class XView {

    public static void Hide(@Nullable final MenuItem item) {
        if (item != null && item.isVisible()) {
            item.setVisible(false);
        }
    }

    public static void Show(@Nullable final MenuItem item) {
        if (item != null && !item.isVisible()) {
            item.setVisible(true);
        }
    }

    //check if view is visible
    public static boolean IsVisible(@Nullable final View v) {
        if (v == null) {
            return false;
        }

        if (v.getVisibility() == View.VISIBLE) {
            return true;
        }

        return false;
    }

    //toggle view from visible <-> gone
    //show progress bar, hide all other content...
    public static void Toggle(@Nullable final View v) {
        if (v == null) {
            Log.e("xing", "null view");
            return;
        }

        if (IsVisible(v)) {
            v.setVisibility(View.GONE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }

    //toggle view from visible <-> gone
    //show progress bar, hide all other content...
    public static void ToggleInvisible(@Nullable final View v) {
        if (v == null) {
            Log.e("xing", "null view");
            return;
        }

        if (v.getVisibility() == View.INVISIBLE) {
            v.setVisibility(View.INVISIBLE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
    }

    //make gone
    public static void Hide(@Nullable final View v) {
        if (v == null) {
            Log.e("xing", "null view");
            return;
        }

        if (v.getVisibility() != View.GONE) {
            v.setVisibility(View.GONE);
        }
    }

    public static void SetAlpha(@Nullable final View v, final float alpha) {
        if (v != null) {
            v.setAlpha(alpha);
        }
    }

    //make invisible
    public static void HideInvisible(@Nullable final View v) {
        if (v == null) {
            Log.e("xing", "null view");
            return;
        }

        if (v.getVisibility() != View.INVISIBLE) {
            v.setVisibility(View.INVISIBLE);
        }
    }

    //make visible..
    public static void Show(@Nullable final View v) {
        if (v == null) {
            Log.e("xing", "null view");
            return;
        }

        if (!IsVisible(v)) {
            v.setVisibility(View.VISIBLE);
        }
    }


//    //animate show TODO broken
//    public static void SlideUpShow(@Nullable final View v) {
//        if (v == null) {
//            Log.e("xing", "null view");
//            return;
//        }
//
//        if (IsVisible(v)) {
//            return;
//        }
//
//        Animation slideUp = AnimationUtils.loadAnimation(App.INSTANCE.getApplicationContext(), R.anim.slide_up_show);
//        slideUp.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // Called when the Animation starts
//                v.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // Called when the Animation ended
//                // Since we are fading a View out we set the visibility
//                // to GONE once the Animation is finished
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // This is called each time the Animation repeats
//            }
//        });
//
//        try {
//            v.startAnimation(slideUp);
//        } catch (Exception e) {
//            XDebug.Handle(e);
//        }
//    }
//
//    //animate show TODO broken
//    public static void SlideDownShow(@Nullable final View v) {
//        if (v == null) {
//            Log.e("xing", "null view");
//            return;
//        }
//
//        if (IsVisible(v)) {
//            return;
//        }
//
//        Animation slideUp = AnimationUtils.loadAnimation(App.INSTANCE.getApplicationContext(), R.anim.slide_down_show);
//        slideUp.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // Called when the Animation starts
//                XView.Show(v);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // This is called each time the Animation repeats
//            }
//        });
//
//        try {
//            v.startAnimation(slideUp);
//        } catch (Exception e) {
//            XDebug.Handle(e);
//        }
//    }
//
//    //animate hide
//    public static void SlideUpHide(@Nullable final View v) {
//        if (v == null) {
//            Log.e("xing", "null view");
//            return;
//        }
//
//        //don't do extra work...it is already GONE
//        if (!XView.IsVisible(v)) {
//            return;
//        }
//
//        Animation slideUpHide = AnimationUtils.loadAnimation(App.INSTANCE, R.anim.slide_up_hide);
//
//        slideUpHide.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // Called when the Animation starts
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // Called when the Animation ended
//                // Since we are fading a View out we set the visibility
//                // to GONE once the Animation is finished
//                XView.Hide(v);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // This is called each time the Animation repeats
//            }
//        });
//
//        try {
//            v.startAnimation(slideUpHide);
//        } catch (Exception e) {
//            XDebug.Handle(e);
//        }
//    }
//
//
//    //animate hide
//    public static void SlideDownHide(@Nullable final View v) {
//        if (v == null) {
//            return;
//        }
//
//        //don't do extra work...it is already GONE
//        if (!XView.IsVisible(v)) {
//            return;
//        }
//
//        Animation slideDownHide = AnimationUtils.loadAnimation(App.INSTANCE, R.anim.slide_down_hide);
//
//        slideDownHide.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // Called when the Animation starts
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // Called when the Animation ended
//                // Since we are fading a View out we set the visibility
//                // to GONE once the Animation is finished
//                XView.Hide(v);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // This is called each time the Animation repeats
//            }
//        });
//
//        try {
//            v.startAnimation(slideDownHide);
//        } catch (Exception e) {
//            XDebug.Handle(e);
//        }
//
//    }
//
//    public static void HideWithFade(@Nullable final View v) {
//        if (v == null) {
//            return;
//        }
//
//        //don't do extra work...it is already GONE
//        if (!XView.IsVisible(v)) {
//            return;
//        }
//
//        final Animation hideWithFade = AnimationUtils.loadAnimation(v.getContext(), R.anim.fade_hide);
//
//        hideWithFade.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                // Called when the Animation starts
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                // Called when the Animation ended
//                // Since we are fading a View out we set the visibility
//                // to GONE once the Animation is finished
//                if (v != null) {
//                    XView.Hide(v);
//                }
//
//                hideWithFade.setAnimationListener(null);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//                // This is called each time the Animation repeats
//            }
//        });
//
//        try {
//            v.startAnimation(hideWithFade);
//        } catch (Exception e) {
//            XDebug.Handle(e);
//        }
//    }
}
