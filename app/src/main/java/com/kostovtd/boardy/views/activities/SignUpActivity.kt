package com.kostovtd.boardy.views.activities

import android.content.Context
import android.content.Intent
import android.view.View
import com.kostovtd.boardy.R

/**
 * Created by tosheto on 23.11.20.
 */
class SignUpActivity: BaseActivity(), SignUpView {

    override fun getLayout(): View = layoutInflater.inflate(R.layout.activity_sign_up, null)


    companion object {

        fun newIntent(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }

    }
}