package com.example.examplehw2

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

@Composable
fun getLightBulbPainterResource(temperature: Float): Painter {
    return if (temperature < 0) {
        painterResource(id = R.drawable.lamp_off)
    } else {
        painterResource(id = R.drawable.lamp_on)
    }
}

@Composable
fun TemperatureDependentLight(activity: Activity) {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

    var currentTemperature by remember { mutableFloatStateOf(0.0f) }
    var resetNotification = false

    val listener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                currentTemperature = it.values[0]


                 if (currentTemperature < 0.0f && ActivityCompat.checkSelfPermission(
                    activity.applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED ) {
                    resetNotification = true
                }
                else if (resetNotification && currentTemperature > 40.0f && ActivityCompat.checkSelfPermission(
                        activity.applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED ) {
                    sendTemperatureNotification(context, "High Temperature!", "The temperature is above 40°C!")
                    resetNotification = false
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        sensorManager.registerListener(listener, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    DisposableEffect(Unit) {
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = getLightBulbPainterResource(currentTemperature),
            contentDescription = "light bulb icon",
            modifier = Modifier.scale(0.4f)
        )
    }
}

@SuppressLint("ServiceCast")
fun sendTemperatureNotification(context: Context, title: String, content: String) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val channelId = "temp_channel_id"
    val channel = NotificationChannel(
        channelId,
        "Temperature Notifications",
        NotificationManager.IMPORTANCE_HIGH
    ).apply {
        description = "Temperature notification channel"
    }
    notificationManager.createNotificationChannel(channel)

    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.lightbulb)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build()

    notificationManager.notify(1, notification)
}

