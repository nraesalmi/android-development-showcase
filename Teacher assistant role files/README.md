# Mobile Computing -course exercise session materials, by Niklas Raesalmi

## Homework 3
[![Homework 3 Demo](https://img.youtube.com/vi/IM3HtKzRCjg/0.jpg)](https://www.youtube.com/shorts/IM3HtKzRCjg)

## Homework 3 Instructions:

URI
- Add coil implementation into module level build gradle
- Use rememberAsyncImagePainter for image loading

PhotoPicker
- use rememberLauncherForActivityResult()
- Launch photopicker in the image where the feature is wanted


For Room Database
- Go to .idea>kotlinc.xml and check kotlin ver
- Go check the ksp ver that matches
- Head to top level build gradle and add plugin
- Head to module level build gradle and use plugin
- Add dependencies for room

- Create data class User
- Create UserDatabase
- Create UserDao Kotlin Interface
- Create abstract class AppDatabase

Class UserViewModel is used for handling data interaction
- Create UserViewModel and handle user data there
- New UserViewModels are created using UserViewModelFactory

## Homework 4
[![Homework 4 Demo](https://img.youtube.com/vi/bi98gQphsL0/0.jpg)](https://www.youtube.com/shorts/bi98gQphsL0)

## HW 4 Instructions:

Notification Setup

- Add necessary permissions in AndroidManifest.xml (POST_NOTIFICATIONS)
- Create class NotificationHelper() which includes requestPermission, 
new notificationChannel and createNotification
- Use rememberLauncherForActivityResult() to request notification permission
- Check permission status and send a notification when permission is granted using createNotification
- In MainActivity, create an instance of NotificationHelper.
- Call notificationHelper.notificationChannel() in onCreate() to set up the notification channel.


Trigger notification when app is not in foreground

- Use e.g. SensorEventListener to trigger the notification.
- Use notificationBuilder to create and send notifications from the background.


Make Notification Interactable

- Add an intent to the notification using PendingIntent.
- Set intent to open MainActivity when the notification is tapped.
- In createNotification() set .setContentIntent(intent) to make it interactive.
- Use flags to prevent multiple instances of activity when reopening the app.

Sensor Integration

- Add sensor permissions if required (if using e.g. gps, mic)
- Use SensorManager to get the desired sensor (e.g., Sensor.TYPE_AMBIENT_TEMPERATURE).
- Set SensorEventListener to listen for changes in sensor data.
- In onSensorChanged(), display or use data 

