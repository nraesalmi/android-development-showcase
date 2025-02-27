package com.example.examplehw2

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.example.examplehw2.ui.theme.ExampleHW2Theme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val notificationHelper = NotificationHelper(this)
        notificationHelper.notificationChannel()

        setContent {
            ExampleHW2Theme {
                MainScreen(notificationHelper)

            }
        }
    }
}

@Composable
fun MainScreen(notificationHelper: NotificationHelper) {
    val activity = LocalActivity.current as Activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isEnabled ->
            if (isEnabled){
                notificationHelper.createNotification("Test notification","Notifications enabled!")
            }
        }
    )

    Column {
        Spacer(modifier = Modifier.padding(10.dp))
        Row(modifier = Modifier.padding(20.dp)) {
            ExtendedFloatingActionButton(
                onClick = { notificationHelper.requestPermission(launcher) },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
            )
            {
            Text(text = "Enable Notifications")
            }
        }
        TemperatureDependentLight(activity)
    }
}
