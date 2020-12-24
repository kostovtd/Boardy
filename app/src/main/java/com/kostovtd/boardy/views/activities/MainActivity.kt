package com.kostovtd.boardy.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kostovtd.boardy.R
import com.kostovtd.boardy.presenters.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by tosheto on 21.10.20.
 */
class MainActivity : BaseActivity(), MainView {

    private val mainPresenter = MainPresenter()

    override fun getLayout(): View = layoutInflater.inflate(R.layout.activity_main, null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        mainPresenter.attachView(this)

        signout.setOnClickListener {
            mainPresenter.signOut()
        }
    }


    override fun goToSignInActivity() {
        SignInActivity.newIntent(this)
    }


    companion object {

        fun newIntent(context: Context, clearBackStack: Boolean = false) {
            val intent = Intent(context, MainActivity::class.java)

            if(clearBackStack) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

            context.startActivity(intent)
        }

    }
}