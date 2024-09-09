# Mobile Computing
Niklas Raesalmi

## Final Project
My project is a camera app that uses an integrated camera feature and stores the taken photos with the timestamp and the name and profile photo of the user in its gallery. There is also a feature to turn the camera around and switch back and forth between the back and front cameras. I used the layout from my previous homework projects for the top bar and selecting the profile photo and name, and I followed a tutorial on how to implement CameraX into the app. I used Icons for the camera buttons for easier readability and picked a square box for the “Take photo” feature and placed it in the center at the bottom. Next to it for easy access I placed the gallery button and clicking it opens a drawer with the photos at the bottom. At the left top corner under the profile button is the change cameras button to balance out the camera section of the screen.

I feel like I was able to implement everything as I wanted but adding the timestamps proved to be a little difficult with the way the app was laid out. Luckily I managed to find a proper solution to make it work sufficiently. For the code I used Android Jetpack Components, Compose UI Toolkit for the layout, CameraX API for the camera functionalities, Coil Image Loading Library for the asynchronous image loading and Bitmaps for the image storing and processing, file operations for copying images to app storage and URIs for representing resource identifiers. The app seems to work well, although switching profile photos requires a restart after the first time.

The app could be better by making it a diary app for example, after taking a photo it could ask “What’s on your mind?” and add it with the photo into the gallery. Also the entries should be saved into the device memory for later viewing and the gallery should be made easier to read if there were to be more entries.

[![Final project Demo](https://img.youtube.com/vi/I1yu1J28WSw/0.jpg)](https://www.youtube.com/shorts/I1yu1J28WSw)

## Homework 4
I first got the notification channel 1 to show up in the permissions tab by creating a notification channel and I created its contents using the NotificationCompat.Builder(). I made the notifications pop up with intervals of 10 seconds and made it interactable by adding .setContentIntent() to the notification builder. I decided to use the temperature sensor by first giving it permissions and creating fun TemperatureDependentLight() which uses a sensorManager to get the data and a listener to look for changes in it. Then I made two versions of a light bulb png, one that is on and one that is off and made them switch depending on whether the temperature is below or above 0 degrees celsius. 

[![Homework 4 Demo](https://img.youtube.com/vi/uo8IzB-1-tQ/0.jpg)](https://www.youtube.com/shorts/uo8IzB-1-tQ)

## Homework 3
I first got picking an image working by using the pickMedia photo picker and following the tutorial form from https://developer.android.com/training/data-storage/shared/photopicker. For text input i used TextField(). Storing the information so that the data can be found by the app even after a restart comes from the app first creating a new instance of UserViewModel and saving the username and profilePhotoUri and changes to the data are made using saveUserData. The data is then saved using sharedPreferences and the destination of the information is set to context.filesDir

[![Homework 3 Demo](https://img.youtube.com/vi/LOeTRpFdccM/0.jpg)](https://www.youtube.com/shorts/LOeTRpFdccM)

## Homework 2
The most important part for implementing navigation is setting up a NavHost that keeps track of the page that you're currently on and where each button leads to.

I got the navigation working by first setting both pages into the NavHost and then for the buttons I used navController.navigate() and put the page that the button should lead to into the parenthesis. To prevent circular navigation, instead of the back button on the message page leading back to the main screen I instead used navController.popBackStack() to go back one page.

[![Homework 2 Demo](https://img.youtube.com/vi/c9yMqHfl4TA/0.jpg)](https://www.youtube.com/shorts/c9yMqHfl4TA)
## Homework 1

Following the Jetpack Compose Tutorial went relatively straightforwardly and without any bigger hiccups. I only encountered problems with the dark mode not working at the start. I first followed the tutorial line-by-line to learn the basics and after that I added my own twist to it by tweaking the values and adding other modifiers to the author, body and the structure of them. I then added multiple different possible choices for the profile photo, title and text and used randomization to pick them for each entry.

I added images by drag-and-dropping them into the Resource Manager's Drawable section and edited their size and shape using the foundation.layout, foundation.border and ui.draw.clip imports. I modified the size and spacing of the text, made the titles bolded and changed the title's colors. By using foundation.lazy.LazyColumn, I added multiple text entries and made the app scrollable. By using imports from runtime and foundation.clickable and by using a boolean variable and if statements to check its state, I made the entries interactable.

[![Homework 1 Demo](https://img.youtube.com/vi/K5z_BPJC6U0/0.jpg)](https://www.youtube.com/shorts/K5z_BPJC6U0)
