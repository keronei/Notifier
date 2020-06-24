package com.keronei.notifierclient.data.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.keronei.notifierclient.R
import com.keronei.notifierclient.data.dataobjects.Constants

class NotificationUtil {

    fun createTestNotification( context: Context, count : Int){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL, "Local notes", NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_dhis_launcher)
                .setContentTitle("DHIS2 Event")
                .setContentText("Events you have subscribed to will be notified in this manner.")
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText("Events you have subscribed to will be notified in this manner. You can then log in to " +
                                "DHIS2 and check what is happening."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        Log.d("NOTIFICATION BUILT", builder.toString())
        with(notificationManager) {
            // notificationId is a unique int for each notification that you must define
            Log.d("NOTIFY", "with parsed")
            notify(Constants.NOTIFICATION_LOCAL_ID, builder.build())
        }

    }
}