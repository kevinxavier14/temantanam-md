package com.capstone.temantanam.ui.screen.description

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.capstone.temantanam.R
import com.capstone.temantanam.response.GetCollectionItem
import com.capstone.temantanam.response.GetPlantData
import com.capstone.temantanam.ui.collection.PlantCollectionIdViewModel
import com.capstone.temantanam.ui.navigation.Screen
import com.capstone.temantanam.ui.session.UserSessionViewModel
import com.capstone.temantanam.ui.theme.TemanTanamTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantDescriptionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel,
    plantName: String,
    plantCollectionIdViewModel: PlantCollectionIdViewModel
) {
    val plantDescriptionViewModel: PlantDescriptionViewModel = viewModel()

    val userId: String by userSessionViewModel.userId.observeAsState("")
    val token: String by userSessionViewModel.token.observeAsState("")

    // API Plant Data
    val result: GetPlantData? by plantDescriptionViewModel.result.observeAsState()
    val isLoading by plantDescriptionViewModel.isLoading.observeAsState(false)


    var imageUrl by remember { mutableStateOf<String?>(null) }
    var id by remember { mutableStateOf<Int?>(null) }
    var name by remember { mutableStateOf<String?>(null) }
    var species by remember { mutableStateOf<String?>(null) }
    var description by remember { mutableStateOf<String?>(null) }

    plantDescriptionViewModel.getPlantByName(plantName, token)

    if (result != null) {
        imageUrl = result!!.imageUrl
        id = result!!.id
        name = result!!.name
        species = result!!.species
        description = result!!.description
    } else {
        plantDescriptionViewModel.getPlantByClassification(plantName, userId, token)
    }

    // Filter Collection item or not
    val insideCollection = remember { mutableStateOf(false) }
    insideCollection.value = plantCollectionIdViewModel.inCollection(result)

    //Popup add collection
    val showPopup = remember { mutableStateOf(false) }

    Box {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = { Text(text = "") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = Color.White
                )
            },
            content = { innerPadding ->
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        item {
                            // Large image of the plant at the top
                            Image(
                                painter = rememberAsyncImagePainter(imageUrl),
                                contentDescription = "Plant Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        item {
                            // Box containing plant descriptions
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 16.dp,
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 8.dp
                                    ),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                Column(
                                    modifier = Modifier
                                        .background(Color.White, RoundedCornerShape(8.dp))
                                        .padding(16.dp)
                                ) {

                                    // Plant name in title format
                                    name?.let {
                                        Text(
                                            text = it,
                                            style = TextStyle(
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }

                                    // Plant species
                                    species?.let {
                                        Text(
                                            text = it,
                                            style = TextStyle(fontSize = 16.sp)
                                        )
                                    }

                                    // Plant description in a single paragraph
                                    description?.let {
                                        Text(
                                            text = it,
                                            style = TextStyle(fontSize = 16.sp),
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (insideCollection.value) {
                            item {
                                Card(
                                    modifier = modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 8.dp
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = 4.dp,
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Icon(
                                                imageVector = Icons.Default.Light,
                                                contentDescription = "Lighting icon",
                                                modifier = Modifier.size(20.dp)
                                            )

                                            Text(
                                                text = "Lighting",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp
                                                ),
                                                modifier = Modifier.padding(4.dp)
                                            )

                                            Text(
                                                text = "${result?.lightning}",
                                                style = TextStyle(fontSize = 16.sp),
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Column {
                                            Icon(
                                                imageVector = Icons.Default.Cloud,
                                                contentDescription = "Shade icon",
                                                modifier = Modifier.size(20.dp)
                                            )

                                            Text(
                                                text = "Shade",
                                                style = TextStyle(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp
                                                ),
                                                modifier = Modifier.padding(4.dp)
                                            )

                                            Text(
                                                text = "${result?.shading}",
                                                style = TextStyle(fontSize = 16.sp),
                                                modifier = Modifier.padding(4.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                Card(
                                    modifier = modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 8.dp
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = 4.dp,
                                ) {
                                    Column {
                                        Icon(
                                            imageVector = Icons.Default.WaterDrop,
                                            contentDescription = "Water icon",
                                            modifier = Modifier.size(20.dp)
                                                .padding(start = 4.dp, top = 4.dp)
                                        )

                                        Text(
                                            text = "Water: ${result?.wateringRecommendation}",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            ),
                                            modifier = Modifier.padding(4.dp)
                                        )

                                        Text(
                                            text = "${result?.wateringDescription}",
                                            style = TextStyle(fontSize = 16.sp),
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }
                            }

                            item {
                                Card(
                                    modifier = modifier.padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 8.dp
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = 4.dp,
                                ) {
                                    Column {
                                        Icon(
                                            imageVector = Icons.Default.Grass,
                                            contentDescription = "Fertilize icon",
                                            modifier = Modifier.size(20.dp)
                                                .padding(start = 4.dp, top = 4.dp)
                                        )

                                        Text(
                                            text = "Fertilize: ${result?.fertilizingRecommendation}",
                                            style = TextStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 16.sp
                                            ),
                                            modifier = Modifier.padding(4.dp)
                                        )

                                        Text(
                                            text = "${result?.fertilizingDescription}",
                                            style = TextStyle(fontSize = 16.sp),
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }
                            }
                        } else {
                            item {
                                Button(
                                    onClick = {
                                        id?.let {
                                            plantDescriptionViewModel.addCollection(
                                                it,
                                                userId,
                                                token
                                            )
                                        }
                                        showPopup.value = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            top = 8.dp,
                                            start = 16.dp,
                                            end = 16.dp,
                                            bottom = 16.dp
                                        )
                                ) {
                                    Text(text = stringResource(R.string.add_collection))
                                }
                            }
                        }
                    }

                    if (showPopup.value) {
                        AlertDialog(
                            onDismissRequest = { showPopup.value = false },
                            title = { Text("Added to Collection") },
                            text = { Text("Congrats! $name has been added to your collection") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showPopup.value = false
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.PlantDescription.route) {
                                                inclusive = true
                                            }
                                        }
                                    },
                                    content = { Text("Back to Home") }
                                )
                            }
                        )
                    }
                }
            },
            backgroundColor = MaterialTheme.colors.background
        )
    }
}
