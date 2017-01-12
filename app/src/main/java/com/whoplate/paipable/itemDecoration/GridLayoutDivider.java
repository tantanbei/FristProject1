package com.whoplate.paipable.itemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.whoplate.paipable.ui.XUI;

public class GridLayoutDivider extends RecyclerView.ItemDecoration{

    private final int mSize;
    private final int dividerSize;

    public GridLayoutDivider() {
        mSize = XUI.DpToPx(1);
        dividerSize = 5 * mSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(dividerSize, dividerSize, dividerSize, dividerSize);
    }
}
