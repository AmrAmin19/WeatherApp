package com.example.weatherapp.view.alert

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.R
import com.example.weatherapp.view.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarm Received")

        // Retrieve the scheduled time and type from the intent
//        val scheduledTime = intent?.getLongExtra("SCHEDULED_TIME", -1L) ?: -1L
//
//        val alertType = intent?.getStringExtra("ALERT_TYPE") ?: "ALARM"
        val notificationRequest = intent?.getIntExtra("notification",0)


        // Check if the current time has reached or passed the scheduled time
//        if (scheduledTime != -1L && System.currentTimeMillis() >= scheduledTime) {
            when (intent?.action) {
                "Alarm.Action" -> {
                    // Start the overlay service for alarm
                    val serviceIntent = Intent(context, AlarmService::class.java)
                    context?.startService(serviceIntent)
                    Log.d("AlarmReceiver", "Alarm Triggered and Overlay Service Started")
                }
                "Notification.Action" -> {
                    // Show a notification for the scheduled time
                    showNotification(context,notificationRequest?:0)
                    Log.d("AlarmReceiver", "Notification Triggered")
                }
                else -> Log.d("AlarmReceiver", "Alarm or Notification is not yet due:")
            }
//        } else {
//            Log.d("AlarmReceiver", "Alarm or Notification is not yet due: $scheduledTime")
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(context: Context?, notificationRequest:Int) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, notificationRequest, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = Notification.Builder(context, "channel_id")
            .setContentTitle("Alert")
            .setContentText("It's time to check the weather!")
            .setSmallIcon(R.drawable.baseline_alarm_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}