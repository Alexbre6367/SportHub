package com.example.sporthub.data.timer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.sporthub.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TimerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())

    companion object {
        const val ACTION_STOP_SERVICE = "STOP_TIMER_SERVICE"
    }

    override fun onBind(intent: Intent) : IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            "TIMER_CHANNEL_ID",
            "Timer Service Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(serviceChannel)
    }

    @SuppressLint("ForegroundServiceType")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent?.action == ACTION_STOP_SERVICE) {
            stopSelf()
            return START_NOT_STICKY
        }

        createNotificationChannel()

        val notification = createNotificationBuilder(0).build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(1, notification, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH)
        } else {
            startForeground(1, notification)
        }

        startTimerLogic()

        return  START_STICKY
    }


    private fun startTimerLogic() {
        serviceScope.launch {
            TimerManager.timeLeft.collect { seconds ->
                if(seconds <= 0) {
                    stopSelf()
                } else {
                    updateNotification(seconds.toInt())
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun createNotificationBuilder(time: Int): NotificationCompat.Builder {
        val minutes = time / 60
        val seconds = time % 60
        val timeString = String.format("%02d:%02d", minutes, seconds)

        return NotificationCompat.Builder(this, "TIMER_CHANNEL_ID")
            .setContentTitle("Таймер")
            .setContentText(timeString)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
    }

    private fun updateNotification(time: Int) {
        val notification = createNotificationBuilder(time).build()
        val manager = getSystemService(NotificationManager::class.java)
        manager?.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}