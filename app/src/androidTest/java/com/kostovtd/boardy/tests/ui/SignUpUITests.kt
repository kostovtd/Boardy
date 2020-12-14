package com.kostovtd.boardy.tests.ui

import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.firebase.auth.FirebaseAuth
import com.kostovtd.boardy.R
import com.kostovtd.boardy.data.ErrorType
import com.kostovtd.boardy.data.Resource
import com.kostovtd.boardy.data.ResourceStatus
import com.kostovtd.boardy.data.models.User
import com.kostovtd.boardy.data.repositories.UserRepository
import com.kostovtd.boardy.utils.*
import com.kostovtd.boardy.views.activities.SignUpActivity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by tosheto on 8.12.20.
 */
class SignUpUITests : BaseUITest() {

    @Rule
    @JvmField
    val signUnRule = ActivityTestRule(SignUpActivity::class.java, true, false)

    private val instrumentation: Instrumentation = InstrumentationRegistry.getInstrumentation()
    private lateinit var context: Context

    private lateinit var userRepository: UserRepository


    @Before
    fun before() {
        context = instrumentation.targetContext

        userRepository = mockk()

        mockkStatic(FirebaseAuth::class)
        every { FirebaseAuth.getInstance().createUserWithEmailAndPassword(any(), any()) } returns mockk(relaxed = true)

        val intent = Intent()
        signUnRule.startActivity(intent)
    }


    @After
    fun after() {

    }


    @Test
    fun testHasProperUI() {
        expectWithDescendantText(R.id.toolbar, R.string.sign_up)

        expect(R.id.imageEmail)
            .withDrawableResource(R.drawable.ic_email)

        expect(R.id.imagePassword)
            .withDrawableResource(R.drawable.ic_password)

        expect(R.id.inputEmail)
            .withHintResource(R.string.email_mandatory)

        expect(R.id.inputPassword)
            .withHintResource(R.string.password_mandatory)

        expect(R.id.inputConfirmPassword)
            .withHintResource(R.string.confirm_password_mandatory)

        expect(R.id.signUp)
            .withTextResource(R.string.sign_up)
    }

    @Test
    fun testErrorMessageIsVisibleWithEmptyEmail() {
        expect(R.id.signUp)
            .perform(ViewActions.click())

        expectSnackbarWithTextResource(R.string.empty_email)
    }


    @Test
    fun testErrorMessageIsVisibleWithEmptyPassword() {
        expect(R.id.inputEmail)
            .perform(ViewActions.clearText(), ViewActions.typeText("test@gmail.com"))

        expect(R.id.signUp)
            .perform(ViewActions.click())

        expectSnackbarWithTextResource(R.string.empty_password)
    }


    @Test
    fun testErrorMessageIsVisibleWithEmptyConfirmPassword() {
        expect(R.id.inputEmail)
            .perform(ViewActions.clearText(), ViewActions.typeText("test@gmail.com"))

        expect(R.id.inputPassword)
            .perform(ViewActions.clearText(), ViewActions.typeText("1q2w3e4r"))

        expect(R.id.signUp)
            .perform(ViewActions.click())

        expectSnackbarWithTextResource(R.string.empty_confirm_password)
    }


    @Test
    fun testErrorMessageIsVisibleWithWrongConfirmPassword() {
        expect(R.id.inputEmail)
            .perform(ViewActions.clearText(), ViewActions.typeText("test@gmail.com"))

        expect(R.id.inputPassword)
            .perform(ViewActions.clearText(), ViewActions.typeText("1q2w3e4r"))

        expect(R.id.inputConfirmPassword)
            .perform(ViewActions.clearText(), ViewActions.typeText("aaaaaaaa"))

        expect(R.id.signUp)
            .perform(ViewActions.click())

        expectSnackbarWithTextResource(R.string.password_mismatch)
    }
}