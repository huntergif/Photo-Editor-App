package com.aqchen.filterfiesta.util;

import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
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
