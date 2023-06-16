package com.capstone.temantanam.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.temantanam.R
import com.capstone.temantanam.response.GetCollectionItem
import com.capstone.temantanam.response.GetUserData
import com.capstone.temantanam.ui.collection.PlantCollectionIdViewModel
import com.capstone.temantanam.ui.components.PlantCollectionItem
import com.capstone.temantanam.ui.navigation.Screen
import com.capstone.temantanam.ui.session.UserSessionViewModel
import com.capstone.temantanam.ui.theme.TemanTanamTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel,
    plantCollectionIdViewModel: PlantCollectionIdViewModel
) {

    val homeViewModel: HomeViewModel = viewModel()

    val userId: String by userSessionViewModel.userId.observeAsState("")
    val token: String by userSessionViewModel.token.observeAsState("")

    val result: List<GetCollectionItem?>? by homeViewModel.result.observeAsState()
    val isLoading by homeViewModel.isLoading.observeAsState(false)

    val showPopup = remember { mutableStateOf(false) }
    val deletedId = remember { mutableStateOf("") }
    val deletedName = remember { mutableStateOf("") }
    var deletedPlantId = remember { mutableStateOf(0) }

    homeViewModel.getCollection(userId, token)

    println(plantCollectionIdViewModel.plantIdList.value)

    Box(modifier = modifier.fillMaxSize()) {
        // Title and message box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "My Plant Collection",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Keep your plants organized and healthy",
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }

        // Plant collection
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp)
                .clip(RoundedCornerShape(8.dp)), // Add rounded corners to the box
            elevation = 4.dp // Apply elevation for the shadow effect
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    items(items = result ?: emptyList()) { plantCollectionItem ->
                        PlantCollectionItem(
                            plantCollectionData = plantCollectionItem!!,
                            onItemClick = {
                                navController.navigate(
                                    Screen.PlantDescription.createRoute(plantCollectionItem.plantName)
                                )
                            },
                            onDeleteClick = {
                                deletedId.value = plantCollectionItem.collectionId.toString()
                                deletedName.value = plantCollectionItem.plantName.toString()
                                deletedPlantId.value = plantCollectionItem.plantId!!
                                showPopup.value = true
                            }
                        )
                        // Add the plant ID to plantCollectionIdViewModel
                        plantCollectionItem.plantId?.let {
                            plantCollectionIdViewModel.addPlantId(it)
                        }
                    }
                }
            }
        }
    }

    if (showPopup.value) {
        AlertDialog(
            onDismissRequest = { showPopup.value = false },
            title = { Text("Removing ${deletedName.value}") },
            text = { Text("Are you sure you want to remove ${deletedName.value} from your collection?") },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showPopup.value = false
                    },
                    content = { Text("No") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPopup.value = false
                        homeViewModel.deleteCollection(deletedId.value, token)
                        plantCollectionIdViewModel.deletePlantId(deletedPlantId.value)
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    content = { Text("Yes") }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(
    navController: NavHostController = rememberNavController(),
    userSessionViewModel: UserSessionViewModel = viewModel(),
    plantCollectionIdViewModel: PlantCollectionIdViewModel = viewModel()
) {
    TemanTanamTheme {
        HomeScreen(
            navController = navController,
            userSessionViewModel = userSessionViewModel,
            plantCollectionIdViewModel = plantCollectionIdViewModel
        )
    }
}