package com.kostovtd.boardy.utils

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.BoundedMatcher
import com.kostovtd.boardy.util.spToPixels
import org.hamcrest.Description
import org.hamcrest.Matcher
import kotlin.math.ceil

/**
 * Created by tosheto on 23.11.20.
 */
class TextViewMatchers {
    companion object {

        fun withTextColorResource(colorResourceId: Int): Matcher<View> {
            return object : BoundedMatcher<View, TextView>(TextView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("TextView with textColorResourceId: $colorResourceId")
                }

                override fun matchesSafely(item: TextView?): Boolean {
                    return item?.let {
                        Color.parseColor(item.context.resources.getString(colorResourceId)) == item.currentTextColor
                    } ?: run {
                        false
                    }
                }
            }
        }

        fun withTextSize(textSize: Float): Matcher<View> {
            return object : BoundedMatcher<View, TextView>(TextView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("TextView with textSize: $textSize")
                }

                override fun matchesSafely(item: TextView?): Boolean {
                    return item?.let {
                        ceil(spToPixels(textSize, item.context)) == item.textSize
                    } ?: run {
                        false
                    }
                }
            }
        }

        fun withTextStyle(textStyleId: Int): Matcher<View> {
            return object : BoundedMatcher<View, TextView>(TextView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("TextView with textStyleId: $textStyleId")
                }

                override fun matchesSafely(item: TextView?): Boolean {
                    return item?.let {
                        textStyleId == it.typeface.style
                    } ?: run {
                        false
                    }
                }
            }
        }

    }
}