package com.kostovtd.boardy.tests.ui

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import com.kostovtd.boardy.views.activities.BaseActivity

/**
 * Created by tosheto on 22.11.20.
 */
open class BaseUITest {

    fun ActivityTestRule<out BaseActivity>.startActivity(intent: Intent) {
        this.launchActivity(intent)
    }

}