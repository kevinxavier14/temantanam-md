package com.capstone.temantanam.ui.screen.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.capstone.temantanam.response.GetHistoryItem
import com.capstone.temantanam.ui.components.PlantCollectionItem
import com.capstone.temantanam.ui.components.PlantHistoryItem
import com.capstone.temantanam.ui.navigation.Screen
import com.capstone.temantanam.ui.session.UserSessionViewModel

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel,
) {
    val historyViewModel: HistoryViewModel = viewModel()

    val userId: String by userSessionViewModel.userId.observeAsState("")
    val token: String by userSessionViewModel.token.observeAsState("")

    val result: List<GetHistoryItem?>? by historyViewModel.result.observeAsState()
    val isLoading by historyViewModel.isLoading.observeAsState(false)

    historyViewModel.getHistory(userId, token)

    Box(modifier = modifier.fillMaxSize()) {
        // Title and message box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.TopStart) // Align the Row to the start (left)
            ) {
                Text(
                    text = "Scan history",
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp) // Add left padding to the Text
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
                    items(items = result ?: emptyList()) { plantHistoryItem ->
                        PlantHistoryItem(
                            plantHistoryData = plantHistoryItem!!,
                            onItemClick = {
                                navController.navigate(
                                    Screen.PlantDescription.createRoute(plantHistoryItem.plantName)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
