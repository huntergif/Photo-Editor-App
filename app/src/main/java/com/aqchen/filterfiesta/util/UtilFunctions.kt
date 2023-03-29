package com.aqchen.filterfiesta.util;

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.widget.TextView


// Adds the given clickable span to the given text view.
// The given `text` must contain the given `clickableText`
fun setTextViewWithClickableSpan(textView: TextView, text: String, clickableText: String, clickableSpan: ClickableSpan) {
        val startIndex = text.indexOf(clickableText)
        val endIndex = startIndex + clickableText.length
        val spannableString = SpannableString(text)
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance()
}

fun getScreenWidth(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = activity.windowManager.currentWindowMetrics
                val insets: Insets = windowMetrics.windowInsets
                        .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.width() - insets.left - insets.right
        } else {
                // Support SDK 29
                val displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
        }
}
