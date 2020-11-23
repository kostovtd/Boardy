package com.kostovtd.boardy.views.activities

import android.os.Bundle
import android.view.View
import com.kostovtd.boardy.R
import com.kostovtd.boardy.presenters.SplashPresenter
import kotlinx.android.synthetic.main.activity_base.*

/**
 * Created by tosheto on 22.11.20.
 */
class SplashActivity: BaseActivity(), SplashView {

    private val presenter = SplashPresenter()


    override fun getLayout(): View = layoutInflater.inflate(R.layout.activity_splash, null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.attachView(this)

        toolbar.visibility = View.GONE
    }


    override fun onResume() {
        super.onResume()
        presenter.isSignedIn()
    }


    override fun goToSignInActivity() {
        SignInActivity.newIntent(this)
    }


    override fun goToMainActivity() {
        MainActivity.newIntent(this)
    }
}