package com.kostovtd.boardy.utils

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.equalTo

/**
 * Created by tosheto on 22.11.20.
 */

fun expect(id: Int): ViewInteraction =
    onView(withId(id))
        .check(matches(isDisplayed()))

fun expectViewWithParent(id: Int): ViewInteraction =
    onView(withParent(withId(id)))

fun expectWithEffectiveVisibilityValue(id: Int, visibility: Visibility = Visibility.VISIBLE): ViewInteraction =
    onView(withId(id))
        .check(matches(withEffectiveVisibility(visibility)))

fun expectWithDescendantText(id: Int, text: String): ViewInteraction =
    onView(withId(id))
        .check(matches(hasDescendant(withText(text))))

fun expectWithDescendantText(id: Int, resourceId: Int): ViewInteraction =
    onView(withId(id))
        .check(matches(hasDescendant(withText(stringResource(resourceId)))))

fun expectSnackbarWithTextResource(resourceId: Int): ViewInteraction =
    onView(withId(com.google.android.material.R.id.snackbar_text))
        .check(matches(withText(stringResource(resourceId))))

fun expectSnackbarWithText(text: String): ViewInteraction =
    onView(withId(com.google.android.material.R.id.snackbar_text))
        .check(matches(withText(text)))

fun ViewInteraction.withText(text: String): ViewInteraction =
    this.check(matches(withText(equalTo(text))))

fun ViewInteraction.withTextResource(resourceId: Int): ViewInteraction =
    this.check(matches(withText(equalTo(stringResource(resourceId)))))

fun ViewInteraction.withTextColorResource(textColorResource: Int): ViewInteraction =
    this.check(matches(TextViewMatchers.withTextColorResource(textColorResource)))

fun ViewInteraction.withTextSize(textSize: Float): ViewInteraction =
    this.check(matches(TextViewMatchers.withTextSize(textSize)))

fun ViewInteraction.withTextStyle(textStyle: Int): ViewInteraction =
    this.check(matches(TextViewMatchers.withTextStyle(textStyle)))

fun stringResource(id: Int): String {
    val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
    return targetContext.resources.getString(id)
}