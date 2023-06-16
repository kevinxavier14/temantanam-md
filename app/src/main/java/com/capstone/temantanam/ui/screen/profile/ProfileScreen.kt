package com.capstone.temantanam.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.capstone.temantanam.response.GetUserData
import com.capstone.temantanam.ui.navigation.Screen
import com.capstone.temantanam.ui.session.UserSessionViewModel
import com.capstone.temantanam.ui.theme.TemanTanamTheme

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel
) {
    val profileViewModel: ProfileViewModel = viewModel()

    val userId: String by userSessionViewModel.userId.observeAsState("")
    val token: String by userSessionViewModel.token.observeAsState("")

    val result: GetUserData? by profileViewModel.result.observeAsState()

    // GET API PROFILE
    var userProfilePicture by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf<String?>(null) }
    var userEmail by remember { mutableStateOf<String?>(null) }

    profileViewModel.getUser(userId, token)

    if (result != null) {
//        userProfilePicture = result!!.imageUrl
        userName = result!!.name
        userEmail = result!!.email
    }

    // DUMMY PROFILE
//    val userProfilePicture = "https://media.licdn.com/dms/image/D5603AQGrxsv843c_6Q/profile-displayphoto-shrink_200_200/0/1685629999168?e=1691625600&v=beta&t=8eQVHt9QXZv_vzoRSYVG-L6-WRj9oW4G-KMIM4f2qlw"
//    val userName = "Dummy Name"
//    val userEmail = "dummyemail@gmail.com"

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center
        ) {
            // Display profile picture
            if (userProfilePicture != null) {
                Image(
                    painter = rememberAsyncImagePainter(userProfilePicture!!),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    tint = Color.Gray,
                    contentDescription = "Profile Icon",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .align(Alignment.CenterHorizontally)
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Display account name
            userName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display account email
            userEmail?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp),
            shape = CircleShape,
            color = Color(0xFF9C27B0), // Replace with your desired color
        ) {
            IconButton(
                onClick = { navController.navigate(Screen.UpdateProfile.route) },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    tint = Color.White,
                    contentDescription = "Edit Profile"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(
    navController: NavHostController = rememberNavController(),
    userSessionViewModel: UserSessionViewModel = viewModel()
) {
    TemanTanamTheme {
        ProfileScreen(
            navController = navController,
            userSessionViewModel = userSessionViewModel
        )
    }
}