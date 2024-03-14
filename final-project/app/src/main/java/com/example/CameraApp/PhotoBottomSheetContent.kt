package com.example.CameraApp

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
@Composable
fun PhotoBottomSheetContent(
    bitmaps: List<Bitmap>,
    timestamps: List<String>, // List of timestamps corresponding to each bitmap
    userProfilePhoto: UserViewModel,
    username: String,
    modifier: Modifier = Modifier
) {
    if (bitmaps.isEmpty()) {
        Box(
            modifier = modifier
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("There are no photos yet")
        }
    } else {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalItemSpacing = 16.dp,
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
        ) {
            itemsIndexed(bitmaps) { index, bitmap ->
                val timestamp = timestamps.getOrNull(index) ?: ""
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    // Photo on the right side
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Adjust the height as needed
                            .padding(start = 208.dp)
                    )

                    // Profile photo, username, and timestamp on the left side
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxHeight()
                    ) {
                        userProfilePhoto.user.profilePhotoUri?.let { profilePhotoUri ->
                            Image(
                                painter = rememberImagePainter(data = profilePhotoUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 5.dp)
                        ) {
                            Text(
                                username,
                                modifier = Modifier
                                    .padding(start = 55.dp)
                            )
                            Text(
                                timestamp,
                                modifier = Modifier
                                    .padding(start = 50.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
