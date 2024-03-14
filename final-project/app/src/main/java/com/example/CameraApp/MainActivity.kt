package com.example.CameraApp

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
data class User(var username: String, var profilePhotoUri: Uri?)

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERA_PERMISSIONS, 0
            )
        }

        setContent {
            ComposeTutorialTheme {
                val scope = rememberCoroutineScope()
                val scaffoldState = rememberBottomSheetScaffoldState()
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(
                            CameraController.IMAGE_CAPTURE or
                                    CameraController.VIDEO_CAPTURE
                        )
                    }
                }
                val viewModel = viewModel<MainViewingModel>()
                val photoData by viewModel.photoData.collectAsState()

                var isEditing by remember { mutableStateOf(false) }
                val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
                val userViewModel: UserViewModel = ViewModelProvider(this, UserViewModelFactory(sharedPreferences)).get(UserViewModel::class.java)


                val userProfilePhotoUri = userViewModel.user.profilePhotoUri
                val username = userViewModel.user.username
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

                        // Save the URI of the copied image and update the ViewModel
                        userViewModel.user = userViewModel.user.copy(profilePhotoUri = copiedImageUri)
                        userViewModel.saveUserData()
                    } else {
                        Log.d("PhotoPicker", "No media selected")
                    }
                }

                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = 0.dp,
                    sheetContent = {
                        PhotoBottomSheetContent(
                            bitmaps = photoData.map { it.first },
                            timestamps = photoData.map { it.second },
                            userProfilePhoto = userViewModel,
                            username = userViewModel.user.username,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {
                        CameraPreview(
                            controller = controller,
                            modifier = Modifier
                                .fillMaxSize()
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
                                            userViewModel.user = userViewModel.user.copy(username = newUsername)
                                            userViewModel.saveUserData()
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
                                        pickMedia.launch(
                                            PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                        userViewModel.saveUserData()
                                    },
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Text(text = "Pick Photo")
                                }
                                rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                                    if (uri != null) {
                                        Log.d("PhotoPicker", "Selected URI: $uri")
                                        userViewModel.user = userViewModel.user.copy(profilePhotoUri = uri)
                                        userViewModel.saveUserData()
                                    } else {
                                        Log.d("PhotoPicker", "No media selected")
                                    }
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                controller.cameraSelector =
                                    if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else CameraSelector.DEFAULT_BACK_CAMERA
                            },
                            modifier = Modifier
                                .offset(16.dp, 116.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cameraswitch,
                                contentDescription = "Switch camera"
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(36.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Photo,
                                    contentDescription = "Open Gallery"
                                )
                            }
                            IconButton(
                                modifier = Modifier
                                    .offset(-82.dp, -4.dp)
                                    .size(60.dp),

                                onClick = {
                                    val currentTimestamp = getCurrentTimestamp()
                                    takePhoto(
                                        controller = controller,
                                        onPhotoTaken = { bitmap ->
                                            viewModel.addPhoto(bitmap, currentTimestamp)
                                        }
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckBoxOutlineBlank,
                                    contentDescription = "Take Photo",
                                    modifier = Modifier
                                        .size(60.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun getCurrentTimestamp(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    private fun takePhoto(
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit
    ) {
        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    onPhotoTaken(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            }
        )
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERA_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERA_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
        )
    }
}

fun copyImageToAppStorage(context: Context, uri: Uri, destinationFile: File) {
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = destinationFile.outputStream()

    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
}

class UserViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    var user by mutableStateOf(User("Username", null))

    init {
        // Load the user data from SharedPreferences when the ViewModel is created
        val username = sharedPreferences.getString("username", "Username")
        val profilePhotoUri = sharedPreferences.getString("profilePhotoUri", null)?.let { Uri.parse(it) }
        user = User(username!!, profilePhotoUri)
    }

    fun saveUserData() {
        // Save the user data to SharedPreferences whenever it changes
        with (sharedPreferences.edit()) {
            putString("username", user.username)
            putString("profilePhotoUri", user.profilePhotoUri?.toString() ?: "")
            apply()
        }
    }
}

class UserViewModelFactory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainViewingModel : ViewModel() {
    private val _photoData = MutableStateFlow<List<Pair<Bitmap, String>>>(emptyList())
    val photoData: StateFlow<List<Pair<Bitmap, String>>> = _photoData

    fun addPhoto(bitmap: Bitmap, timestamp: String) {
        _photoData.value = _photoData.value.toMutableList().apply {
            add(bitmap to timestamp)
        }
    }
}