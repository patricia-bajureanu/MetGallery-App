package com.patriciabajureanu.metgallery.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.clickable
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patriciabajureanu.metgallery.artworks.api.MetArtwork
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

@Composable
fun ArtworkCard(item: MetArtwork, onClick: () -> Unit, onRemove: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD1C4E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
            Text(text = item.artist ?: "Unknown Artist", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Remove from Favorites",
                color = Color.Red,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.clickable { onRemove() }
            )
        }
    }
}




