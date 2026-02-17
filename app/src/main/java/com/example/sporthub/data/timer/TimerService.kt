package com.example.sporthub.data.timer

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.sporthub.MainActivity
import com.example.sporthub.R
import com.nothing.ketchum.Common
import com.nothing.ketchum.Glyph
import com.nothing.ketchum.GlyphManager
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

        val currentTime = TimerManager.timeLeft.value.toInt()
        val notification = createNotificationBuilder(currentTime).build()

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
                    blinkingGlyph()
                } else {
                    updateNotification(seconds.toInt())
                    updateGlyph(seconds, TimerManager.totalSeconds.value)
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun createNotificationBuilder(time: Int): NotificationCompat.Builder {
        val minutes = time / 60
        val seconds = time % 60
        val timeString = String.format("%02d:%02d", minutes, seconds)

        val contentIntent = PendingIntent.getActivity(
            this,
            0, //уникальный id под сервис
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, TimerService::class.java).apply {
            action = ACTION_STOP_SERVICE
        }

        val stopContentIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, "TIMER_CHANNEL_ID")
            .setContentTitle("Timer")
            .setContentText(timeString)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setContentIntent(contentIntent)
            .addAction(
                R.drawable.ic_launcher_foreground,
                "Stop",
                stopContentIntent
            )
    }

    private fun updateNotification(time: Int) {
        val notification = createNotificationBuilder(time).build()
        val manager = getSystemService(NotificationManager::class.java)
        manager?.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        mGlyphManager?.turnOff()
    }

    //glyph
    private var mGlyphManager: GlyphManager? = null
    private val mGlyphCallback = object : GlyphManager.Callback {
        override fun onServiceConnected(componentName: ComponentName?) {
            val manager = mGlyphManager ?: return

            if (Common.is20111()) mGlyphManager!!.register(Glyph.DEVICE_20111)
            if (Common.is22111()) mGlyphManager!!.register(Glyph.DEVICE_22111)
            if (Common.is23111()) mGlyphManager!!.register(Glyph.DEVICE_23111)
            if (Common.is23113()) mGlyphManager!!.register(Glyph.DEVICE_23113)
            if (Common.is24111()) mGlyphManager!!.register(Glyph.DEVICE_24111)

            try {
                manager.openSession()
            } catch (e: Exception) {
                Log.e("MyLog", "Ошибка регистрации глифов ${e.message}")
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mGlyphManager = null
        }
    }

    override fun onCreate() {
        super.onCreate()
        mGlyphManager = GlyphManager.getInstance(applicationContext)
        mGlyphManager?.init(mGlyphCallback)
    }

    private fun updateGlyph(secondsLeft: Long, totalSeconds: Long) {
        if(totalSeconds <= 0) return

        val progress = ((secondsLeft.toFloat() / totalSeconds) * 100).toInt()

        try {
            val builder = mGlyphManager?.glyphFrameBuilder ?: return

            if(Common.is20111()) builder.buildChannel(Glyph.Code_20111.D1_1)
            if(Common.is22111()) builder.buildChannel(Glyph.Code_22111.C1_1)
            if(Common.is23111()) builder.buildChannel(Glyph.Code_23111.C_1)
            if(Common.is24111()) builder.buildChannel(Glyph.Code_24111.A_1)

            val baseFrame = builder.build()

            mGlyphManager!!.displayProgress(baseFrame, progress, false)
        } catch (e: Exception) {
            Log.e("MyLog", "Ошибка обновления глифов: ${e.message}")
        }
    }

    private fun blinkingGlyph() {
        serviceScope.launch {
            val manager = mGlyphManager ?: return@launch
            val builder = manager.glyphFrameBuilder ?: return@launch

            if(Common.is20111()) builder.buildChannel(Glyph.Code_20111.E1)
            if(Common.is22111()) builder.buildChannel(Glyph.Code_22111.E1)
            if(Common.is23111()) builder.buildChannel(Glyph.Code_23111.A)
            if(Common.is24111()) builder.buildChannel(Glyph.Code_24111.B_1)

            val frame = builder.build()

            repeat(5) {
                manager.toggle(frame)
                kotlinx.coroutines.delay(500)
                manager.turnOff()
                kotlinx.coroutines.delay(500)
            }

            stopSelf()
        }
    }
}