package com.example.weatherapp.view.alert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.model.ALERT_KEYS
import com.example.weatherapp.model.AlarmData
import com.example.weatherapp.model.Repo
import com.example.weatherapp.model.SharedPreferencesKeys
import com.example.weatherapp.model.local.LocalData
import com.example.weatherapp.model.local.SharedPreferences
import com.example.weatherapp.model.remote.RemoteData
import com.example.weatherapp.viewModel.AlertFactory
import com.example.weatherapp.viewModel.AlertViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale


class AlertFragment : Fragment() {

    lateinit var binding:FragmentAlertBinding
    lateinit var factory:AlertFactory
    lateinit var viewModel:AlertViewModel
    lateinit var myAdapter:AlarmAdapter

        private val sharedPreferencesName = "alert_preferences"
        private val requestCodeKey = "request_code"
        var requestCode: Int = 0

     var notificationSharedPreferences:String="enable"

    private val notificationChannelId = "channel_id"

    companion object {
        private const val REQUEST_CODE_OVERLAY_PERMISSION = 1001
        private const val REQUEST_CODE_NOTIFICATION_PERMISSION = 1002
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding= FragmentAlertBinding.inflate(inflater,container,false)

        factory= AlertFactory(Repo.getInstance(RemoteData(), LocalData(requireContext()),
            SharedPreferences(requireContext())
        ))

        viewModel=ViewModelProvider(this,factory).get(AlertViewModel::class.java)

        requestCode = getRequestCodeFromPreferences()

        createNotificationChannel()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         notificationSharedPreferences =viewModel.getSettingsPrefs(SharedPreferencesKeys.Notification_key,"enable")

        myAdapter=AlarmAdapter(){
            deleteAlarm(requireContext(),it)

        }
        binding.AlarmRecycelView.layoutManager= LinearLayoutManager(context)
        binding.AlarmRecycelView.adapter=myAdapter

       binding.fabOpenMap.setOnClickListener {
           showAlertDialog()
       }

           viewModel.deleteOldAlarms(System.currentTimeMillis())

    viewLifecycleOwner.lifecycleScope.launch {
        viewModel.alarms.collect{
            myAdapter.submitList(it)
        }
    }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Weather Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Weather Alert Notifications"
            }
            val notificationManager = requireContext().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAlertDialog() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                TimePickerDialog(
                    requireContext(),
                    { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(Calendar.SECOND, 0)
                        calendar.set(Calendar.MILLISECOND, 0)

                        showTypeDialog(calendar)
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showTypeDialog(calendar: Calendar) {
        // test
       updateRequestCode()

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Type")

        val types = arrayOf("Alarm", "Notification")
        builder.setItems(types) { _, which ->
            when (which) {
                0 -> checkAlarmPermission(calendar)
                1 -> {
                    when(notificationSharedPreferences)
                    {
                        "enable" -> checkNotificationPermission(calendar)
                        else -> {Toast.makeText(requireContext(), "Notification is disabled", Toast.LENGTH_SHORT).show()
                           findNavController().navigate(AlertFragmentDirections.actionAlertFragmentToSettingsFragment())

                        }
                    }
                }
            }
        }
        builder.show()
    }

    private fun checkAlarmPermission(calendar: Calendar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestExactAlarmPermission(calendar)
        } else {
            setAlarm(calendar)
        }
    }

    @SuppressLint("NewApi")
    private fun checkNotificationPermission(calendar: Calendar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission(calendar)
        } else {
            // here puc check
            showNotification(calendar)
        }
    }

    private fun setAlarm(calendar: Calendar) {
        var alarmRequest = getRequestCodeFromPreferences()
        // Ensure the selected time is in the future
        if (calendar.before(Calendar.getInstance())) {
            Toast.makeText(requireContext(), "Cannot set alarm for past time!", Toast.LENGTH_SHORT).show()
            return
        }

        val alarmTimeInMillis = calendar.timeInMillis
        Log.d("AlarmTime", "Setting alarm for: $alarmTimeInMillis (${calendar.time})")

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        // Pass the scheduled time
//        intent.putExtra("SCHEDULED_TIME", alarmTimeInMillis)
        intent.action="Alarm.Action"

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
          alarmRequest,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            // Set the alarm
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTimeInMillis,
                pendingIntent
            )

            // Check Overlay Permission
            checkOverlayPermission()

            viewModel.insertAlarm(AlarmData(alarmRequest,alarmTimeInMillis))
          //  updateRequestCode()
            Toast.makeText(requireContext(), "Alarm Set Successfully! ${viewModel.getAlertPrefs(ALERT_KEYS.ALARM_REQUEST_CODE,0)}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Failed to set alarm: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("AlarmError", "Error setting alarm", e)
        }
    }


    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            // Request the Overlay permission
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${requireContext().packageName}"))
            startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION)
        }
    }


    private fun showNotification(calendar: Calendar) {
        val notificationRequest =getRequestCodeFromPreferences()
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        // Pass notification type and scheduled time to the receiver
//        intent.putExtra("ALERT_TYPE", "NOTIFICATION")
        intent.putExtra("notification", notificationRequest)

        intent.action="Notification.Action"

        // Create a PendingIntent for the AlarmReceiver
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationRequest,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule the alarm to trigger the notification at the selected time
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        Toast.makeText(requireContext(), "Notification Scheduled! ${viewModel.getAlertPrefs(ALERT_KEYS.ALARM_REQUEST_CODE,0)}", Toast.LENGTH_SHORT).show()
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestExactAlarmPermission(calendar: Calendar) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            startActivity(intent)
        } else {
            setAlarm(calendar)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission(calendar: Calendar) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_NOTIFICATION_PERMISSION)
        } else {
            showNotification(calendar)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_NOTIFICATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(requireContext(), "Notification Permission Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Notification Permission Denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun getRequestCodeFromPreferences(): Int {
     return  viewModel.getAlertPrefs(ALERT_KEYS.ALARM_REQUEST_CODE,0)
    }
//
//
//    // Example function where you can modify the requestCode and save it
    private fun updateRequestCode() {

        requestCode ++

        viewModel.addAlertPrefs(ALERT_KEYS.ALARM_REQUEST_CODE,requestCode)
        }



    fun deleteAlarm(context: Context, alarmData: AlarmData) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmRequestCode = alarmData.requestCode

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = "Alarm.Action"
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmRequestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)

        pendingIntent.cancel()

        viewModel.deleteAlarm(alarmData)
        }


}