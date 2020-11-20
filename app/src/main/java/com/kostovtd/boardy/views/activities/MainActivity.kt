package com.kostovtd.boardy.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kostovtd.boardy.R
import com.kostovtd.boardy.data.ErrorType

/**
 * Created by tosheto on 21.10.20.
 */
class MainActivity: BaseActivity() {


    override fun getLayout(): View = layoutInflater.inflate(R.layout.activity_main, null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setHomeButtonEnabled(false)

        title = "Main"
    }


    override fun showError(errorType: ErrorType) {
        TODO("Not yet implemented")
    }

    companion object {

        fun newIntent(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }

    }
}