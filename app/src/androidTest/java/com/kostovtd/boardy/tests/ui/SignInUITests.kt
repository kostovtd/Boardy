package com.kostovtd.boardy.tests.ui

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.espresso.action.ViewActions.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.kostovtd.boardy.views.activities.SignInActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.kostovtd.boardy.R
import com.kostovtd.boardy.utils.*

/**
 * Created by tosheto on 22.11.20.
 */

@RunWith(AndroidJUnit4::class)
class SignInUITests: BaseUITest() {

    @Rule
    @JvmField
    val signInRule = ActivityTestRule(SignInActivity::class.java, true, false)

    private val instrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
    private lateinit var context: Context


    @Before
    fun before() {
        context = instrumentation.targetContext

        val intent = Intent()
        signInRule.startActivity(intent)
    }


    @After
    fun after() {

    }


    @Test
    fun testHasProperUI() {
        expectWithDescendantText(R.id.toolbar, R.string.sign_in)

        expect(R.id.inputEmail)

        expect(R.id.inputPassword)

        expect(R.id.signIn)
            .withTextResource(R.string.sign_in)

        expect(R.id.signUp)
            .withTextResource(R.string.sign_up)
    }


    @Test
    fun testErrorMessageIsVisibleWithEmptyInputForSignIn() {
        expect(R.id.signIn)
            .perform(click())

        expectSnackbarWithTextResource(R.string.empty_email)

        expect(R.id.inputEmail)
            .perform(clearText(), typeText("test@gmail.com"))

        expect(R.id.signIn)
            .perform(click())

        expectSnackbarWithTextResource(R.string.empty_password)
    }
}