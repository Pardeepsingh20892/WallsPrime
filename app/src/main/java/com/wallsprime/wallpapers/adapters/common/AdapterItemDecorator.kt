package com.wallsprime.wallpapers.adapters.common

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView





class AdapterItemDecorator( var spanCount:Int, var spacing:Int, var includeEdge:Boolean): RecyclerView.ItemDecoration()
{
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position: Int = parent.getChildAdapterPosition(view) // item position
       // val column: Int = position % spanCount // item column

        if (spanCount == 2) {
           // outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
           // outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position % 2 == 0)   {
                outRect.right = 0
                outRect.left = spacing

            }
            else{
                outRect.right = spacing
                outRect.left = 0

            }

            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
           // outRect.bottom = 0// item bottom
        }
        else {
            if (position < spanCount) { // top edge
                outRect.top = spacing
            }
            outRect.bottom = spacing
            outRect.left = spacing
            outRect.right = spacing

        }
    }
}