package com.arifian.bakingapp.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.arifian.bakingapp.R;

/**
 * Created by faqih on 16/08/17.
 */

public class SpaceLinearItemDecorator extends RecyclerView.ItemDecoration {
    private int space;

    public SpaceLinearItemDecorator(Context context) {
        this.space = context.getResources().getDimensionPixelSize(R.dimen.material_card_gutter);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();

        if(itemPosition == 0){
            outRect.top = space;
        }
        outRect.bottom = space;
    }
}
