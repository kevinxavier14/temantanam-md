package com.capstone.temantanam

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.temantanam.ui.collection.PlantCollectionIdViewModel
import com.capstone.temantanam.ui.session.UserSessionViewModel
import com.capstone.temantanam.ui.theme.TemanTanamTheme

class MainActivity : ComponentActivity() {

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 101

    private val userSessionViewModel: UserSessionViewModel by viewModels()
    private val plantCollectionIdViewModel: PlantCollectionIdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemanTanamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TemanTanamApp()
                }
            }
        }

        checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()

        // Clear session data when MainActivity is destroyed
        userSessionViewModel.clearUserData()

        // Clear all plant id when MainActivity is destroyed
        plantCollectionIdViewModel.clearAllPlantId()
    }

    private fun checkPermissions() {
        val cameraPermission = Manifest.permission.CAMERA
        val readExternalStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE

        val hasCameraPermission = ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED
        val hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(this, readExternalStoragePermission) == PackageManager.PERMISSION_GRANTED

        val permissionsToRequest = mutableListOf<String>()
        if (!hasCameraPermission) {
            permissionsToRequest.add(cameraPermission)
        }
        if (!hasReadExternalStoragePermission) {
            permissionsToRequest.add(readExternalStoragePermission)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }
}