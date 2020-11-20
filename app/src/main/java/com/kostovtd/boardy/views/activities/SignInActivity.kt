package com.kostovtd.boardy.views.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.kostovtd.boardy.R
import com.kostovtd.boardy.data.ResourceStatus
import com.kostovtd.boardy.presenters.SignInPresenter
import kotlinx.android.synthetic.main.activity_sign_in.*

/**
 * Created by tosheto on 14.11.20.
 */
class SignInActivity : BaseActivity(), SignInView {


    private val signInPresenter = SignInPresenter()


    override fun getLayout(): View = layoutInflater.inflate(R.layout.activity_sign_in, null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_sign_in)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        signInPresenter.attachView(this)

        signIn.setOnClickListener {
//            val email = inputEmail.text.toString()
//            val password = inputPassword.text.toString()
//
//            signInPresenter.signInWithEmailAndPassword(email, password)
            showLoading()
        }
    }
}