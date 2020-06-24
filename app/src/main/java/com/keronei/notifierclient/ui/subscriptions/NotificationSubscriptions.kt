package com.keronei.notifierclient.ui.subscriptions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.keronei.notifierclient.R
import com.keronei.notifierclient.data.utils.NotificationUtil
import kotlinx.android.synthetic.main.activity_notification_subscriptions.*
import kotlinx.android.synthetic.main.content_notification_subscriptions.*

class NotificationSubscriptions : AppCompatActivity() {

    companion object{
    fun getIntent(context: Context?): Intent? {
        return Intent(context, NotificationSubscriptions::class.java)
    }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_subscriptions)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        test_notification_button.setOnClickListener {
            Log.d("TEST", "click-listener")
            NotificationUtil().createTestNotification(this, 0)
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Add programs with events", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

}
