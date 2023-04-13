package com.aqchen.filterfiesta.util

import android.text.SpannableString
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import org.hamcrest.Matchers

// https://stackoverflow.com/questions/38314077/how-to-click-a-clickablespan-using-espresso
fun clickClickableSpan(textId: Int): ViewAction {
    return object: ViewAction {

        override fun getConstraints(): Matcher<View> {
            return Matchers.instanceOf(TextView::class.java)
        }

        override fun getDescription(): String {
            return "clicking on clickable span with text id: $textId"
        }

        override fun perform(uiController: UiController?, view: View?) {
            if (view !is TextView || view.text !is SpannableString || view.text.isEmpty()) {
                throw NoMatchingViewException.Builder()
                    .includeViewHierarchy(true)
                    .withRootView(view)
                    .build()
            }

            val text = InstrumentationRegistry.getInstrumentation().targetContext.resources.getString(textId)
            val spannableString = view.text as SpannableString
            val spans: Array<ClickableSpan> = spannableString.getSpans(0, spannableString.length, ClickableSpan::class.java)
            spans.forEach {
                val start = spannableString.getSpanStart(it)
                val end = spannableString.getSpanEnd(it)
                val sequence = spannableString.subSequence(start, end);
                if (text == sequence.toString()) {
                    it.onClick(view)
                    return
                }
            }
            throw NoMatchingViewException.Builder()
                .includeViewHierarchy(true)
                .withRootView(view)
                .build()
        }
    }
}
