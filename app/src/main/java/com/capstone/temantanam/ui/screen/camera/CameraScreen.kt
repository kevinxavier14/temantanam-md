package com.capstone.temantanam.ui.screen.camera

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.capstone.temantanam.ui.theme.TemanTanamTheme
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.temantanam.R
import com.capstone.temantanam.ui.navigation.Screen
import com.capstone.temantanam.ui.screen.description.PlantDescriptionViewModel
import com.capstone.temantanam.ui.session.UserSessionViewModel
import java.io.ByteArrayOutputStream

private fun loadImageBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun convertBitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    val byteArray = outputStream.toByteArray()
    val imageBase64 =  Base64.encodeToString(byteArray, Base64.DEFAULT)
    return "data:image/png;base64,$imageBase64"
}

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel,
    navigateToPlantDescription: (String) -> Unit
    ) {
    val userId: String by userSessionViewModel.userId.observeAsState("")
    val token: String by userSessionViewModel.token.observeAsState("")

    val cameraViewModel: CameraViewModel = viewModel()

    val result: String? by cameraViewModel.result.observeAsState()
    val isLoading by cameraViewModel.isLoading.observeAsState(false)

    val context = LocalContext.current
    val activity = LocalContext.current as? Activity

    var capturedImageBitMap by remember { mutableStateOf<Bitmap?>(null) }
    var capturedImageBase64 by remember { mutableStateOf("") }

    var identifyButtonEnabled by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the selected image from the gallery
        uri?.let { imageUri ->
            // Perform desired operations with the selected image URI
            val imageBitmap = loadImageBitmapFromUri(context, imageUri)
            if (imageBitmap != null) {
                // Handle the selected image (bitmap)
                capturedImageBitMap = imageBitmap
                capturedImageBase64 = convertBitmapToBase64(imageBitmap)

                identifyButtonEnabled = true // Enable the identify button

                Toast.makeText(context, "Image captured", Toast.LENGTH_SHORT).show()
            } else {
                // Handle failure or cancellation
                Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap != null) {
            // Handle the captured image (bitmap)
            capturedImageBitMap = bitmap
            capturedImageBase64 = convertBitmapToBase64(bitmap)

            identifyButtonEnabled = true // Enable the identify button

            Toast.makeText(context, "Image captured", Toast.LENGTH_SHORT).show()
        } else {
            // Handle failure or cancellation
            Toast.makeText(context, "Failed to capture image", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.title_camera_page),
            textAlign = TextAlign.Start, // Align text to the left
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        )

        Text(
            text = stringResource(R.string.message_camera_page),
            textAlign = TextAlign.Start, // Align text to the left
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(300.dp) // Adjust the size of the box as needed
                .padding(16.dp)
                .border(4.dp, Color(0xFF9C27B0), shape = RoundedCornerShape(8.dp)) // Border modifier
        ) {
            if (capturedImageBitMap != null) {
                Image(
                    bitmap = capturedImageBitMap!!.asImageBitmap(),
                    contentDescription = "Captured Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                Icon(
                    imageVector = Icons.Default.LocalFlorist,
                    contentDescription = "Potted Plant Icon",
                    modifier = Modifier
                        .size(128.dp)
                        .align(Alignment.Center)
                )
            }
        }

        Button(
            onClick = {
                cameraViewModel.classifyPlant(capturedImageBase64)
            },
            enabled = identifyButtonEnabled,
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White, // Adjust the color if necessary
                    modifier = Modifier
                        .size(24.dp) // Adjust the size as needed
                ) // Show progress indicator when loading
            } else {
                Text(text = stringResource(R.string.identify_plant))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularButton(
                onClick = { galleryLauncher.launch("image/*") },
                backgroundColor = Color.Transparent,
                icon = Icons.Default.AddPhotoAlternate,
                iconTint = Color.Black
            )

            Spacer(modifier = Modifier.width(16.dp))

            CircularButton(
                onClick = { activity?.let { cameraLauncher.launch(null, ActivityOptionsCompat.makeSceneTransitionAnimation(it)) } },
                backgroundColor = Color(0xFF9C27B0),
                icon = Icons.Default.PhotoCamera,
                iconTint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            CircularButton(
                onClick = { /* Handle help button click */ },
                backgroundColor = Color.Transparent,
                icon = Icons.Default.Help,
                iconTint = Color.Black
            )
        }
    }

    LaunchedEffect(result) {
        result?.let { plantName ->
            navigateToPlantDescription(plantName)
        }
    }

}

@Composable
fun CircularButton(
    onClick: () -> Unit,
    backgroundColor: Color,
    icon: ImageVector,
    iconTint: Color
) {
    Box(
        modifier = Modifier
            .size(100.dp) // Adjust the size as needed
            .padding(8.dp) // Adjust the padding as needed
            .background(backgroundColor, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(48.dp), // Adjust the icon size as needed
            imageVector = icon,
            contentDescription = null,
            tint = iconTint
        )
    }
}
