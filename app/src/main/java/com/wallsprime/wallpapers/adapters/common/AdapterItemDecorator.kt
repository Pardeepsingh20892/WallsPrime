package com.wallsprime.wallpapers.adapters.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView





class AdapterItemDecorator( var spanCount:Int, var spacing:Int ): RecyclerView.ItemDecoration()
{
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position: Int = parent.getChildAdapterPosition(view)


        if (spanCount == 2) {

            if (position % 2 == 0)   {
                outRect.right = 0
                outRect.left = spacing

            }
            else {
                outRect.right = spacing
                outRect.left = 0

            }

            if (position < spanCount) {
                outRect.top = spacing
            }

        }

        else {
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
            outRect.left = spacing
            outRect.right = spacing

        }
    }
}