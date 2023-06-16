package com.capstone.temantanam.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.More
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.capstone.temantanam.response.GetCollectionItem
import com.capstone.temantanam.ui.screen.home.HomeViewModel
import com.capstone.temantanam.ui.screen.profile.ProfileScreen
import com.capstone.temantanam.ui.session.UserSessionViewModel
import com.capstone.temantanam.ui.theme.TemanTanamTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantCollectionItem(
    modifier: Modifier = Modifier,
    plantCollectionData: GetCollectionItem,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            onClick = onItemClick
        ) {
            Box(Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Image
                    Image(
                        painter = rememberAsyncImagePainter("${plantCollectionData.plantImageUrl}"),
                        contentDescription = "Plant Image",
                        modifier = Modifier.size(64.dp).clip(shape = RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop,
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // Text
                    Column {
                        plantCollectionData.plantName?.let {
                            Text(
                                text = it,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        plantCollectionData.plantSpecies?.let {
                            Text(
                                text = it,
                                style = TextStyle(fontStyle = FontStyle.Italic),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 8.dp)
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Delete",
                        tint = Color(0xFF9C27B0)
                    )
                }
            }
        }
    }
}
