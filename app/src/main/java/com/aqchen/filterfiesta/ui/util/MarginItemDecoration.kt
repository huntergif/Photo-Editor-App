package com.aqchen.filterfiesta.ui.util

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

sealed class MarginItemDecorationDirection {
    object Horizontal: MarginItemDecorationDirection()
    object Vertical: MarginItemDecorationDirection()
}

class MarginItemDecoration(private val direction: MarginItemDecorationDirection) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.getChildAdapterPosition(view) == 0) {
            if (direction == MarginItemDecorationDirection.Horizontal) {
                outRect.left = 8.dp
            } else if (direction == MarginItemDecorationDirection.Vertical) {
                outRect.top = 8.dp
            }
        }
        if (direction == MarginItemDecorationDirection.Horizontal) {
            outRect.right = 8.dp
        } else if (direction == MarginItemDecorationDirection.Vertical) {
            outRect.bottom = 8.dp
        }
    }
}

