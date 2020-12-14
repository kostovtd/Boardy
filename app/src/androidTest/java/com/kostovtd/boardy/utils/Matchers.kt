package com.kostovtd.boardy.utils

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.test.espresso.matcher.BoundedMatcher
import com.kostovtd.boardy.util.bitmapFromDrawable
import com.kostovtd.boardy.util.spToPixels
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
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


class DrawableMatcher(@field:DrawableRes @param:DrawableRes private val expectedId: Int) : TypeSafeMatcher<View?>(View::class.java) {
    private var resourceName: String? = null
    override fun matchesSafely(target: View?): Boolean { //Type check we need to do in TypeSafeMatcher
        if (target !is ImageView) {
            return false
        }
        //We fetch the image view from the focused view
        val imageView: ImageView = target
        if (expectedId < 0) {
            return imageView.drawable == null
        }
        //We get the drawable from the resources that we are going to compare with image view source
        val resources: Resources = target.getContext().resources
        val expectedDrawable: Drawable = resources.getDrawable(expectedId)
        resourceName = resources.getResourceEntryName(expectedId)
        //comparing the bitmaps should give results of the matcher if they are equal
        return if (imageView.drawable is VectorDrawable) {
            val bitmap = bitmapFromDrawable(imageView.drawable)
            val otherBitmap = bitmapFromDrawable(expectedDrawable)
            bitmap?.sameAs(otherBitmap) ?: false
        } else {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val otherBitmap = (expectedDrawable as BitmapDrawable).bitmap
            bitmap.sameAs(otherBitmap)
        }
    }

    override fun describeTo(description: Description) {
        description.appendText("with drawable from resource id: ")
        description.appendValue(expectedId)
        if (resourceName != null) {
            description.appendText("[")
            description.appendText(resourceName)
            description.appendText("]")
        }
    }
}