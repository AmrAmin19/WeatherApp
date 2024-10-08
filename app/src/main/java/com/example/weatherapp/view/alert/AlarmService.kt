package com.example.weatherapp.view.alert

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.content.Context
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.example.weatherapp.R
import com.example.weatherapp.databinding.AlarmDialogBinding
import com.example.weatherapp.view.MainActivity

class AlarmService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var binding:AlarmDialogBinding

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        overlayView = LayoutInflater.from(this).inflate(R.layout.alarm_dialog, null)

        binding=AlarmDialogBinding.bind(overlayView)




        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        )
        params.let {
          it.gravity=Gravity.TOP
        }



        // insert the view into the window and then display it
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, params)

        // open the media player and start looping
        val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(this, notificationUri)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

//        overlayView.setOnClickListener {
//            stopSelf()
//        }

       binding.stopButton.setOnClickListener {
           stopSelf()
       }

        binding.snoozeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            stopSelf()
            startActivity(intent)

        }



    }

    override fun onDestroy() {
        super.onDestroy()

        if (::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }

        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}