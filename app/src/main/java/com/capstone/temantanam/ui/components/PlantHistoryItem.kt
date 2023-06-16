package com.capstone.temantanam.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.capstone.temantanam.response.GetHistoryItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PlantHistoryItem(
    modifier: Modifier = Modifier,
    plantHistoryData: GetHistoryItem,
    onItemClick: () -> Unit
) {
    val inputDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputDateFormat: DateFormat = SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault())

    val createdAtDate: Date? = try {
        plantHistoryData.createdAt?.let { inputDateFormat.parse(it) }
    } catch (e: Exception) {
        null
    }

    val formattedCreatedAt: String = createdAtDate?.let { outputDateFormat.format(it) } ?: ""

    Box(
        modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp) // Apply padding to the Box
    ) {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            onClick = onItemClick
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image
                Image(
                    painter = rememberAsyncImagePainter("${plantHistoryData.plantImageUrl}"),
                    contentDescription = "Plant Image",
                    modifier = Modifier.size(64.dp).clip(shape = RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop,
                )

                // Spacing
                Spacer(modifier = Modifier.width(16.dp))

                // Text
                Column {
                    plantHistoryData.plantName?.let {
                        Text(
                            text = it,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    plantHistoryData.plantSpecies?.let {
                        Text(
                            text = it,
                            style = TextStyle(fontStyle = FontStyle.Italic),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text(
                        text = formattedCreatedAt,
                        style = TextStyle(fontSize = 12.sp, color = Color.Gray),
                        modifier = Modifier.fillMaxWidth()
                    )

                }
            }
        }
    }
}