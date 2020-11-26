package com.kostovtd.boardy.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kostovtd.boardy.R
import com.kostovtd.boardy.presenters.SignUpPresent
import kotlinx.android.synthetic.main.activity_sign_up.*

/**
 * Created by tosheto on 23.11.20.
 */
class SignUpActivity : BaseActivity(), SignUpView {

    private val presenter = SignUpPresent()


    override fun getLayout(): View = layoutInflater.inflate(R.layout.activity_sign_up, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.attachView(this)

        signUp.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()
            val confirmPassword = inputConfirmPassword.text.toString()
            val userName = inputUsername.text.toString()
            val firstName = inputFirstName.text.toString()
            val lastName = inputLastName.text.toString()

            presenter.signUpWithEmailAndPassword(
                email, password, confirmPassword,
                userName, firstName, lastName
            )
        }
    }

    companion object {

        fun newIntent(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }

    }
}