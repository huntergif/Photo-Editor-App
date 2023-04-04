package com.aqchen.filterfiesta.ui.util

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

// https://stackoverflow.com/questions/44023420/recyclerview-inside-the-bottomsheet-is-not-working
class TouchEventInterceptorLayout(
    context: Context,
    attrs: AttributeSet
) : FrameLayout(context, attrs) {
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val handled = super.dispatchTouchEvent(ev)
        requestDisallowInterceptTouchEvent(true)
        return handled
    }
}
