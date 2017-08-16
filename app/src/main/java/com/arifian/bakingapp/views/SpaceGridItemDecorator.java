package com.arifian.bakingapp.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arifian.bakingapp.R;

/**
 * Created by faqih on 16/08/17.
 */

public class SpaceGridItemDecorator extends RecyclerView.ItemDecoration {
    private int space;
    private int columnCount;

    private boolean mNeedLeftSpacing = false;

    public SpaceGridItemDecorator(Context context, int columnCount) {
        this.space = context.getResources().getDimensionPixelSize(R.dimen.material_card_gutter);
        this.columnCount = columnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int frameWidth = (int) ((parent.getWidth() - (float) space * (columnCount - 1)) / columnCount);
        int padding = parent.getWidth() / columnCount - frameWidth;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        int itemCount = parent.getAdapter().getItemCount() - columnCount;

        outRect.top = space;
        if (parent.getChildAdapterPosition(view) % columnCount != columnCount-1) {
            outRect.right = space;
        }
//        if (itemPosition > itemCount) {
//            outRect.bottom = space;
//        } else {
//            outRect.bottom = 0;
//        }

    }
}
