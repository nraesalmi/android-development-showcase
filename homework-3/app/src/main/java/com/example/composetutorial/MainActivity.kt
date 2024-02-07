/**
 *
 * The project takes heavy inspiration from the Android Jetpack Compose tutorial
 * found at: https://developer.android.com/jetpack/compose/tutorial
 *
 * Contents of randomProfilePictures, randomNames, and randomFunFacts have been
 * generated using ChatGPT. randomProfilePictures have been randomly generated
 * using the website thispersondoesnotexist.com.
 *
 */

package com.example.composetutorial

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.ui.unit.sp
import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.compose.composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter

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

class UserViewModel : ViewModel() {
    var user = mutableStateOf(User("Username", null))
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTutorialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MyAppNavHost()
                }
            }
        }
    }
}

@Composable
fun MyAppNavHost(
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
            MainScreen(navController)
        }
        composable("messagesPage") {
            MessagesPage(navController)
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    val viewModel: UserViewModel = viewModel()
    val background = painterResource(R.drawable.wavebackground)
    val lightBulbIcon = painterResource(id = R.drawable.lightbulb)
    var buttonClicked by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("Username") }
    var selectedPhotoUri by remember { mutableStateOf<Uri?>(null) }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            selectedPhotoUri = uri
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
                            .data(selectedPhotoUri ?: R.drawable.no_profile_photo)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.no_profile_photo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(75.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    if (isEditing) {
                        TextField(
                            value = viewModel.user.value.username,
                            onValueChange = { newUsername ->
                                val newUser = viewModel.user.value.copy(username = newUsername)
                                viewModel.user.value = newUser
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
                        },
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(text = "Pick Photo")
                    }
                    val pickMedia =
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

            Image(
                painter = lightBulbIcon,
                contentDescription = "light bulb icon",
                modifier = Modifier
                    .scale(1.3f)
                    .align(Alignment.Center)
                    .absoluteOffset(y = (-70).dp)
            )

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

@Composable
fun MessagesPage(navController: NavHostController) {
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
            items(12) {
                val randomProfilePicture = remember { randomProfilePictures.random() }
                val randomName = remember { randomNames.random() }
                val randomFunFact = remember { randomFunFacts.random() }
                MessageCard(Message(randomName, randomFunFact), profilePicture = randomProfilePicture)
            }
        }
    }
}

@Composable
fun MessageCard(msg: Message, profilePicture: Int) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(id = profilePicture),
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


@Preview
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController)
}