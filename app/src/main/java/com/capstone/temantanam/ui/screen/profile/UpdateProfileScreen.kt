package com.capstone.temantanam.ui.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.capstone.temantanam.R
import com.capstone.temantanam.ui.navigation.Screen
import com.capstone.temantanam.ui.session.UserSessionViewModel

@Composable
fun UpdateProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel
) {
    val updateProfileViewModel: UpdateProfileViewModel = viewModel()

    val nameState = remember { mutableStateOf("") }
    val imageUrlState = remember { mutableStateOf("") }

    val userId: String by userSessionViewModel.userId.observeAsState("")
    val token: String by userSessionViewModel.token.observeAsState("")

    val isUpdated by updateProfileViewModel.isUpdated.observeAsState(false)

    val showPopup = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title_update_profile_page),
            textAlign = TextAlign.Start, // Align text to the left
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.name),
            textAlign = TextAlign.Start, // Align text to the left
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        // Name Text Field
        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text(stringResource(R.string.name)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.image_url) + " (Optional)",
            textAlign = TextAlign.Start, // Align text to the left
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )

        // ImageUrl Text Field
        OutlinedTextField(
            value = imageUrlState.value,
            onValueChange = { imageUrlState.value = it },
            label = { Text(stringResource(R.string.image_url)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val name = nameState.value
                val imageUrl = imageUrlState.value

                // ViewModel Here
                updateProfileViewModel.updateProfile(userId, token, name, imageUrl)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Text(text = stringResource(R.string.update_profile))
        }
    }

    if (isUpdated) {
        showPopup.value = true
    }

    if (showPopup.value) {
        AlertDialog(
            onDismissRequest = { showPopup.value = false },
            title = { Text(text = stringResource(R.string.update_profile_popup_title)) },
            text = { Text(text = stringResource(R.string.update_profile_popup_message)) },
            confirmButton = {
                Button(
                    onClick = {
                        showPopup.value = false
                        navController.navigate(Screen.Profile.route) {
                            popUpTo(Screen.UpdateProfile.route) { inclusive = true }
                        }
                    },
                    content = { Text(text = stringResource(R.string.update_profile_popup_confirm_button)) }
                )
            }
        )
    }
}