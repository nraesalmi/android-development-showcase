/**
 *Mobile Computing -course Homework 3 - Databases
 * Niklas Raesalmi
 * 13th Feb 2024
 *
 * The MessagesPage layout takes heavy inspiration from the Android Jetpack Compose tutorial
 * found at: https://developer.android.com/jetpack/compose/tutorial
 *
 * Contents of randomProfilePictures, randomNames, and randomFunFacts have been
 * generated using ChatGPT. randomProfilePictures have been randomly generated
 * using the website thispersondoesnotexist.com.
 *
 * Docstrings are generated using ChatGPT.
 *
 */

package com.example.composetutorial

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import java.io.File

data class Message(val author: String, val body: String)
data class User(var username: String, var profilePhotoUri: Uri?)


val randomProfilePictures = listOf(
    R.drawable.lataus,
    R.drawable.lataus__1_,
    R.drawable.lataus__2_,
    R.drawable.lataus__3_,
    R.drawable.lataus__4_,
    R.drawable.lataus__5_,
    R.drawable.lataus__6_,
    R.drawable.lataus__7_,
    R.drawable.lataus__8_,
    R.drawable.lataus__9_
)

val randomNames = listOf(
    "Alice",
    "Bob",
    "Charlie",
    "David",
    "Eva",
    "Frank",
    "Grace",
    "Henry",
    "Ivy",
    "Jack",
    "Katherine",
    "Leo",
    "Mia",
    "Nathan",
    "Olivia",
    "Patrick",
    "Quinn",
    "Rachel",
    "Samuel",
)

val randomFunFacts = listOf(
    "Did you know that honey never spoils? Archaeologists have found pots of honey in ancient Egyptian tombs that are over 3,000 years old and still perfectly edible.",
    "Cows have best friends, and they tend to get stressed when they are separated. It's a moo-tivating fact!",
    "Bananas are berries, but strawberries aren't! Botanically speaking, berries are fruits produced from a single ovary, and bananas fit the bill.",
    "In Japan, there is a town named 'Unalaska.' Despite its name, it's not in Alaska; it's in Japan.",
    "Octopuses have three hearts and blue blood. Imagine having a heart-to-heart conversation with an octopus!",
    "A group of flamingos is called a 'flamboyance.' Now that's a fancy way to describe a gathering of flamingos!",
    "The shortest war in history lasted only 38-45 minutes! It was fought between Britain and Zanzibar on August 27, 1896.",
    "A jiffy is an actual unit of time, equivalent to 1/100th of a second. So, the phrase 'I'll be back in a jiffy' might not be as quick as it sounds!",
    "There is a species of jellyfish known as Turritopsis dohrnii, or the 'immortal jellyfish,' which is capable of reverting its cells back to their earliest form and starting its life cycle anew.",
    "The inventor of the frisbee was turned into a frisbee! Walter Morrison, the inventor, was cremated, and his ashes were turned into a limited edition of frisbees.",
    "Honeybees can recognize human faces. They have the ability to remember and recognize faces, which is a rare skill for insects.",
    "A day on Venus (one full rotation on its axis) is longer than a year on Venus (one full orbit around the Sun). Venus has an extremely slow rotation, taking about 243 Earth days to complete one rotation.",
    "Cats have five toes on their front paws but only four toes on their back paws. Some cats may even have extra toes, a condition known as polydactylism.",
    "The world's largest desert is not the Sahara; it's Antarctica. A desert is defined by its low precipitation levels, and Antarctica is the driest and windiest continent on Earth.",
    "The Eiffel Tower can be 15 cm taller during the summer. When a substance is heated up, its particles move more and it expands. The iron structure of the Eiffel Tower expands in the heat of the summer and contracts in the winter."
)

/**
 * Composable function representing a clickable image button.
 *
 * @param onClick The lambda to be executed when the image button is clicked.
 * @param modifier Modifier for styling the image button.
 * @param painter The painter for the image.
 * @param contentDescription The content description for the image.
 */
@Composable
fun ClickableImageButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp)

    )
}

/**
 * Function to copy an image to the app's storage.
 *
 * @param context The context of the application.
 * @param uri The URI of the image to be copied.
 * @param destinationFile The destination file where the image will be copied.
 */
fun copyImageToAppStorage(context: Context, uri: Uri, destinationFile: File) {
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = destinationFile.outputStream()

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
}

/**
 * ViewModel class for managing user data.
 *
 * @property sharedPreferences The shared preferences for storing user data.
 */
class UserViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    var user = mutableStateOf(User("Username", null))

    init {
        // Loads the user data from SharedPreferences when the ViewModel is created
        val username = sharedPreferences.getString("username", "Username")
        val profilePhotoUri = sharedPreferences.getString("profilePhotoUri", null)?.let { Uri.parse(it) }
        user.value = User(username!!, profilePhotoUri)
    }

    fun saveUserData() {
        // Save the user data to SharedPreferences whenever it changes
        with (sharedPreferences.edit()) {
            putString("username", user.value.username)
            putString("profilePhotoUri", user.value.profilePhotoUri.toString())
            apply()
        }
    }
}

/**
 * ViewModel factory for creating instances of [UserViewModel].
 *
 * @property sharedPreferences The shared preferences for storing user data.
 */
class UserViewModelFactory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/**
 * Composable function displaying a light bulb icon with a gradient effect based on ambient temperature.
 *
 * The light bulb icon changes dynamically between on and off states.
 * The appearance is determined by the current ambient temperature.
 */
@Composable
fun TemperatureDependentLight() {
    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

    // Create a composable function to get the painter resource based on temperature
    @Composable
    fun getLightBulbPainterResource(temperature: Float): Painter {
        return if (temperature < 0) {
            painterResource(id = R.drawable.lamp_off)
        } else {
            painterResource(id = R.drawable.lamp_on)
        }
    }

    // Keep track of the current temperature
    var currentTemperature by remember { mutableFloatStateOf(0.0f) }

    // Update the temperature based on sensor changes
    val listener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                // Update the current temperature
                currentTemperature = it.values[0]
            }
        }
    }

    // Register the listener
    sensorManager.registerListener(listener, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = getLightBulbPainterResource(currentTemperature),
            contentDescription = "light bulb icon",
            modifier = Modifier
                .scale(0.4f)
                .align(Alignment.Center)
                .absoluteOffset(y = (-250).dp)
        )
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val viewModel: UserViewModel = ViewModelProvider(this, UserViewModelFactory(sharedPreferences)).get(UserViewModel::class.java)
        setContent {
            ComposeTutorialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyAppNavHost(viewModel)
                }
            }
        }
    }
}

/**
 * Composable function representing the main navigation host.
 *
 * @param viewModel The [UserViewModel] for managing user data.
 * @param modifier Modifier for styling the navigation host.
 * @param navController The navigation controller.
 * @param startDestination The start destination for navigation.
 */
@Composable
fun MyAppNavHost(
    viewModel: UserViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "mainScreen"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("mainScreen") {
            MainScreen(navController, viewModel)
        }
        composable("messagesPage") {
            MessagesPage(navController, viewModel)
        }
    }
}

/**
 * Composable function representing the main screen of the application.
 *
 * @param navController The navigation controller.
 * @param viewModel The [UserViewModel] for managing user data.
 */
@Composable
fun MainScreen(navController: NavHostController, viewModel: UserViewModel) {
    val background = painterResource(R.drawable.wavebackground)
    var buttonClicked by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    val username = viewModel.user.value.username
    val userProfilePhotoUri = viewModel.user.value.profilePhotoUri
    val context = LocalContext.current

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")

            // Define the destination file
            val destinationFile = File(context.filesDir, "profilePhoto.jpg")

            // Call the function to copy the image
            copyImageToAppStorage(context, uri, destinationFile)

            // Get the URI of the copied image
            val copiedImageUri = Uri.fromFile(destinationFile)

            // Save the URI of the copied image
            val newUser = viewModel.user.value.copy(profilePhotoUri = copiedImageUri)
            viewModel.user.value = newUser
            viewModel.saveUserData()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            Image(
                painter = background,
                contentDescription = "background",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)
                    .scale(2.0f)
                    .absoluteOffset(y = (120).dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                    .clickable {
                        isEditing = true
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userProfilePhotoUri ?: R.drawable.no_pfp)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.no_pfp),
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    if (isEditing) {
                        TextField(
                            value = username,
                            onValueChange = { newUsername ->
                                val newUser = viewModel.user.value.copy(username = newUsername)
                                viewModel.user.value = newUser
                                viewModel.saveUserData()
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    isEditing = false
                                }
                            )
                        )
                    } else {
                        ClickableText(
                            text = AnnotatedString(username),
                            onClick = {
                                isEditing = true
                            },
                            modifier = Modifier
                                .weight(1f)
                        )
                    }

                    Button(
                        onClick = {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            viewModel.saveUserData()
                        },
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(text = "Pick Photo")
                    }
                        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                            if (uri != null) {
                                Log.d("PhotoPicker", "Selected URI: $uri")
                                val newUser = viewModel.user.value.copy(profilePhotoUri = uri)
                                viewModel.user.value = newUser
                            } else {
                                Log.d("PhotoPicker", "No media selected")
                            }
                        }
                }
            }

            TemperatureDependentLight()

            Button(
                onClick = {
                    buttonClicked = !buttonClicked
                    navController.navigate("messagesPage")
                },
                modifier = Modifier
                    .absoluteOffset(y = 20.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(30.dp)
            ) {
                Text(
                    text = "View fun facts!",
                    fontSize = 17.sp
                )
            }
        }
    }
}

/**
 * Composable function representing the messages page of the application.
 *
 * @param navController The navigation controller.
 * @param viewModel The [UserViewModel] for managing user data.
 */
@Composable
fun MessagesPage(navController: NavHostController, viewModel: UserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            ClickableImageButton(
                onClick = { navController.popBackStack() },
                painter = painterResource(id = R.drawable._90784_200),
                contentDescription = "Back button",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .scale(1.3f, 1.3f)
            )
        }
        LazyColumn {
            item {
                val userProfilePhoto = viewModel.user.value.profilePhotoUri
                val username = viewModel.user.value.username
                val randomFunFact = remember { randomFunFacts.random() }
                MessageCard(Message(username, randomFunFact), profilePictureUri = userProfilePhoto)
            }
            items(12) {
                val randomProfilePicture = remember { randomProfilePictures.random() }
                val randomName = remember { randomNames.random() }
                val randomFunFact = remember { randomFunFacts.random() }
                MessageCard(Message(randomName, randomFunFact),  profilePictureResId  = randomProfilePicture)
            }
        }
    }
}

/**
 * Composable function representing a chat message card.
 *
 * @param msg The message to be displayed.
 * @param profilePictureUri The URI of the sender's profile picture.
 * @param profilePictureResId The resource ID of the sender's profile picture.
 */
@Composable
fun MessageCard(msg: Message, profilePictureUri: Uri? = null, profilePictureResId: Int = R.drawable.no_pfp) {
    val imagePainter = profilePictureUri?.let { rememberAsyncImagePainter(model = it) }
        ?: painterResource(id = profilePictureResId)

    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = imagePainter,
            contentDescription = "Profile photo",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        var isExpanded by remember { mutableStateOf(false)}
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            label = "",
        )
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                shadowElevation = 2.5.dp,
                color = surfaceColor,
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)

            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 3.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
            }
        }
    }
}
