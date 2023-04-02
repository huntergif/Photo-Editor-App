package com.aqchen.filterfiesta.util;

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Insets
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.view.WindowInsets
import android.widget.TextView
import org.opencv.core.CvType
import org.opencv.imgproc.Imgproc.COLOR_BGR2Luv


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

fun bitmapConfigToCvType(config: Bitmap.Config): Int {
    when (config) {
        Bitmap.Config.ALPHA_8 -> {
            return CvType.CV_8UC1
        }
        Bitmap.Config.RGB_565 -> {
            return CvType.CV_8UC3
        }
        Bitmap.Config.ARGB_4444 -> {
            return CvType.CV_8UC4
        }
        Bitmap.Config.ARGB_8888 -> {
            return CvType.CV_8UC4
        }
        Bitmap.Config.RGBA_F16 -> {
            return CvType.CV_16FC4
        }
        Bitmap.Config.HARDWARE -> {
            return CvType.CV_8UC4
        }
        else -> {
            return CvType.CV_8UC4
        }
    }
}
