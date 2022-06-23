package com.kostovtd.boardy

import com.kostovtd.boardy.presenters.SignUpPresent
import com.kostovtd.boardy.ui.activities.SignUpView
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

/**
 * Created by tosheto on 23.12.20.
 */
class SignUpUnitTests {

    private var presenter: SignUpPresent = spyk()
    private var view: SignUpView = mockk()


    @Before
    fun setUp() {
        presenter.attachView(view)
    }


    @Test
    fun testIsEmailEmptyWithValidInput() {
        assert(presenter.isEmailEmpty(""))
        assert(presenter.isEmailEmpty("  "))
    }


    @Test
    fun testIsEmailEmptyWithInvalidInput() {
        assertFalse(presenter.isEmailEmpty("15"))
        assertFalse(presenter.isEmailEmpty("ab"))
        assertFalse(presenter.isEmailEmpty("!@#"))
        assertFalse(presenter.isEmailEmpty("1a@"))
        assertFalse(presenter.isEmailEmpty(" 15"))
        assertFalse(presenter.isEmailEmpty("15 "))
        assertFalse(presenter.isEmailEmpty(" 15 "))
        assertFalse(presenter.isEmailEmpty(" ab"))
        assertFalse(presenter.isEmailEmpty("ab "))
        assertFalse(presenter.isEmailEmpty(" ab "))
        assertFalse(presenter.isEmailEmpty(" !@"))
        assertFalse(presenter.isEmailEmpty("!@ "))
        assertFalse(presenter.isEmailEmpty(" !@ "))
    }


    @Test
    fun testIsEmailFormatValidWithValidInput() {
        assert(presenter.isEmailFormatValid("test@test.com"))
    }


    @Test
    fun testIsEmailFormatValidWithInvalidInput() {
        assertFalse(presenter.isEmailFormatValid("testtest.com"))
        assertFalse(presenter.isEmailFormatValid("@test.com"))
        assertFalse(presenter.isEmailFormatValid("tests@test"))
        assertFalse(presenter.isEmailFormatValid("%tests@test.com%"))
        assertFalse(presenter.isEmailFormatValid("*tests@test.com*"))
    }


    @Test
    fun testIsPasswordEmptyWithValidInput() {
        assert(presenter.isPasswordEmpty(""))
        assert(presenter.isPasswordEmpty("  "))
    }


    @Test
    fun testIsPasswordEmptyWithInvalidInput() {
        assertFalse(presenter.isPasswordEmpty("15"))
        assertFalse(presenter.isPasswordEmpty("ab"))
        assertFalse(presenter.isPasswordEmpty("!@#"))
        assertFalse(presenter.isPasswordEmpty("1a@"))
        assertFalse(presenter.isPasswordEmpty(" 15"))
        assertFalse(presenter.isPasswordEmpty("15 "))
        assertFalse(presenter.isPasswordEmpty(" 15 "))
        assertFalse(presenter.isPasswordEmpty(" ab"))
        assertFalse(presenter.isPasswordEmpty("ab "))
        assertFalse(presenter.isPasswordEmpty(" ab "))
        assertFalse(presenter.isPasswordEmpty(" !@"))
        assertFalse(presenter.isPasswordEmpty("!@ "))
        assertFalse(presenter.isPasswordEmpty(" !@ "))
    }


    @Test
    fun testIsPasswordFormatValidWithValidInput() {
        assert(presenter.isPasswordFormatValid("12345678"))
        assert(presenter.isPasswordFormatValid("qweasdzxc"))
        assert(presenter.isPasswordFormatValid("!@#$%^&*()"))
    }


    @Test
    fun testIsPasswordFormatValidWithInvalidInput() {
        assertFalse(presenter.isPasswordFormatValid("1"))
        assertFalse(presenter.isPasswordFormatValid("12"))
        assertFalse(presenter.isPasswordFormatValid("123"))
        assertFalse(presenter.isPasswordFormatValid("1234"))
        assertFalse(presenter.isPasswordFormatValid("12345"))
        assertFalse(presenter.isPasswordFormatValid("123456"))
        assertFalse(presenter.isPasswordFormatValid("1234567"))
        assertFalse(presenter.isPasswordFormatValid("q"))
        assertFalse(presenter.isPasswordFormatValid("qw"))
        assertFalse(presenter.isPasswordFormatValid("qwe"))
        assertFalse(presenter.isPasswordFormatValid("qwea"))
        assertFalse(presenter.isPasswordFormatValid("qweas"))
        assertFalse(presenter.isPasswordFormatValid("qweasd"))
        assertFalse(presenter.isPasswordFormatValid("qweasdz"))
        assertFalse(presenter.isPasswordFormatValid("!"))
        assertFalse(presenter.isPasswordFormatValid("!@"))
        assertFalse(presenter.isPasswordFormatValid("!@#"))
        assertFalse(presenter.isPasswordFormatValid("!@#$"))
        assertFalse(presenter.isPasswordFormatValid("!@#$%"))
        assertFalse(presenter.isPasswordFormatValid("!@#$%^"))
        assertFalse(presenter.isPasswordFormatValid("!@#$%^&"))
    }


    @Test
    fun testIsConfirmPasswordEmptyWithValidInput() {
        assert(presenter.isConfirmPasswordEmpty(""))
        assert(presenter.isConfirmPasswordEmpty(" "))
    }


    @Test
    fun testIsConfirmPasswordEmptyWithInvalidInput() {
        assertFalse(presenter.isConfirmPasswordEmpty("15"))
        assertFalse(presenter.isConfirmPasswordEmpty("ab"))
        assertFalse(presenter.isConfirmPasswordEmpty("!@#"))
        assertFalse(presenter.isConfirmPasswordEmpty("1a@"))
        assertFalse(presenter.isConfirmPasswordEmpty(" 15"))
        assertFalse(presenter.isConfirmPasswordEmpty("15 "))
        assertFalse(presenter.isConfirmPasswordEmpty(" 15 "))
        assertFalse(presenter.isConfirmPasswordEmpty(" ab"))
        assertFalse(presenter.isConfirmPasswordEmpty("ab "))
        assertFalse(presenter.isConfirmPasswordEmpty(" ab "))
        assertFalse(presenter.isConfirmPasswordEmpty(" !@"))
        assertFalse(presenter.isConfirmPasswordEmpty("!@ "))
        assertFalse(presenter.isConfirmPasswordEmpty(" !@ "))
    }


    @Test
    fun testIsConfirmPasswordSameAsPasswordWithValidInput() {
        assert(presenter.isConfirmPasswordSameAsPassword("12345678", "12345678"))
        assert(presenter.isConfirmPasswordSameAsPassword("qweasdzx", "qweasdzx"))
        assert(presenter.isConfirmPasswordSameAsPassword("!@#$%^&*", "!@#$%^&*"))
        assert(presenter.isConfirmPasswordSameAsPassword("1q2w3e4r5t!", "1q2w3e4r5t!"))
    }


    @Test
    fun testIsConfirmPasswordSameAsPasswordWithInvalidInput() {
        assertFalse(presenter.isConfirmPasswordSameAsPassword("12345678", "f12345678"))
        assertFalse(presenter.isConfirmPasswordSameAsPassword("1qweasdzx", "qweasdzx"))
        assertFalse(presenter.isConfirmPasswordSameAsPassword(")!@#$%^&*", "!@#$%^&*"))
        assertFalse(presenter.isConfirmPasswordSameAsPassword("f1q2w3e4r5t!", "1q2w3e4r5t!"))
        assertFalse(presenter.isConfirmPasswordSameAsPassword("f1q2w3e4r5t! ", "1q2w3e4r5t!"))
    }
}