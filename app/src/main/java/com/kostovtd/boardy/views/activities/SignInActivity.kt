package com.kostovtd.boardy.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kostovtd.boardy.R
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

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        signInPresenter.attachView(this)

        signIn.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPassword.text.toString()

            signInPresenter.signInWithEmailAndPassword(email, password)
        }

        signUp.setOnClickListener {
            goToSignUpActivity()
        }
    }


    override fun onPause() {
        super.onPause()
        signInPresenter.cancelAllRequests()
    }


    override fun goToMainActivity() {
        MainActivity.newIntent(this)
    }


    override fun goToSignUpActivity() {
        SignUpActivity.newIntent(this)
    }

    companion object {

        fun newIntent(context: Context) {
            val intent = Intent(context, SignInActivity::class.java)
            context.startActivity(intent)
        }

    }


//    val request = SplitInstallRequest.newBuilder()
//        .addModule("HeroRealms")
//        .build()
//    val manager = SplitInstallManagerFactory.create(this)
//
//    manager.startInstall(request)
//
//    manager.registerListener {
//        when (it.status()) {
//            SplitInstallSessionStatus.DOWNLOADING -> Snackbar.make(
//                baseRootContainer,
//                "Downloading",
//                Snackbar.LENGTH_LONG
//            ).show()
//            SplitInstallSessionStatus.INSTALLED -> {
//                val intent = Intent()
//                intent.setClassName(
//                    BuildConfig.APPLICATION_ID,
//                    "com.kostovtd.herorealms.views.activities.HeroRealmsActivity"
//                )
//                startActivity(intent)
//            }
//            else -> {
//            }
//        }
//    }
}