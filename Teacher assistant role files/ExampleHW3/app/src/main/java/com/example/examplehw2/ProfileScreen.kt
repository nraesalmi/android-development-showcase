package com.example.examplehw2

import android.app.Application
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import java.io.FileOutputStream
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter

import com.example.examplehw2.data.User
import com.example.examplehw2.data.UserViewModel
import com.example.examplehw2.data.UserViewModelFactory


@Composable
fun ProfileScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(context.applicationContext as Application))
    val user by userViewModel.userData.collectAsState(initial = null)


    var imageFile = File(context.filesDir, "profile.jpg")
    var updateImage by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {imageUri ->
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val outputFile = File(context.filesDir, "profile.jpg")
            FileOutputStream(outputFile).use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
            imageFile = outputFile

            updateImage = !updateImage
        }
    }
    Column {
        Row(modifier = Modifier.padding(all = 10.dp)) {

            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back_button",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(30.dp)
                )
            }

            Text(
                text = "Profile Screen",
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterVertically)

            )
        }

        Spacer(modifier = Modifier.padding(20.dp))

        Row(modifier = Modifier.padding(140.dp, 20.dp)) {
            val painter = if (updateImage) {
                rememberAsyncImagePainter(imageFile)
            } else {
                rememberAsyncImagePainter(imageFile)
            }

            Image(
                painter = painter,
                contentDescription = "user_image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                    .clickable {
                        photoPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
            )
        }

        Row(modifier = Modifier.padding(80.dp, 10.dp)) {
            OutlinedTextField(
                value = user?.userName ?: "",
                onValueChange = {newName ->
                    userViewModel.updateUser(user?.copy(userName = newName) ?: User(userName = "User"))
                },
                label = { Text("Username") },
            )
        }
    }
}
