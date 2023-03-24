package com.aqchen.filterfiesta.ui.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.view.updatePaddingRelative
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * https://stackoverflow.com/questions/64853649/how-can-i-properly-center-the-first-and-last-items-in-a-horizontal-recyclerview
 * CenterLinearLayoutManager extends LinearLayoutManager to ensure that the snapped item is centered in the parent width.
 * It intercepts the LinearLayoutManager's layout methods and adds padding for the first and last element.
 * Requires the use of `recyclerView.clipToPadding = false` and works best when the first and last element
 * are not in the viewport at the same time.
 */
open class CenterLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context) : super(context)
    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private lateinit var recyclerView: RecyclerView

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        // always measure first item, its size determines starting offset
        // this must be done before super.onLayoutChildren
        if (childCount == 0 && state.itemCount > 0) {
            val firstChild = recycler.getViewForPosition(0)
            measureChildWithMargins(firstChild, 0, 0)
            recycler.recycleView(firstChild)
        }
        super.onLayoutChildren(recycler, state)
    }

    override fun measureChildWithMargins(child: View, widthUsed: Int, heightUsed: Int) {
        val lp = (child.layoutParams as RecyclerView.LayoutParams).absoluteAdapterPosition
        super.measureChildWithMargins(child, widthUsed, heightUsed)
        if (lp != 0 && lp != itemCount - 1) return
        // after determining first and/or last items size use it to alter host padding
        when (orientation) {
            RecyclerView.HORIZONTAL -> {
                val hPadding = ((width - child.measuredWidth) / 2).coerceAtLeast(0)
                if (lp == 0) recyclerView.updatePaddingRelative(start = hPadding, end = hPadding) // here we set the same padding for both sides
                if (lp == itemCount - 1) {
                    if (!reverseLayout) recyclerView.updatePaddingRelative(end = hPadding)
                    if (reverseLayout) recyclerView.updatePaddingRelative(start = hPadding)
                }
            }
            RecyclerView.VERTICAL -> {}
        }
    }

    // capture host recyclerview
    override fun onAttachedToWindow(view: RecyclerView) {
        recyclerView = view
        super.onAttachedToWindow(view)
    }
}
