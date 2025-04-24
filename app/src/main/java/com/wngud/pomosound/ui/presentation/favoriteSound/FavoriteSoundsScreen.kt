package com.wngud.pomosound.ui.presentation.favoriteSound

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wngud.pomosound.R
import com.wngud.pomosound.ui.components.CustomTopAppBar
import com.wngud.pomosound.ui.theme.PomoSoundTheme

// Dummy data class for demonstration
data class FavoriteSound(
    val id: Int,
    val title: String,
    val icons: List<Int>, // List of drawable resource IDs
    val isPlaying: Boolean = false
)

// Dummy data list
val dummyFavoriteSounds = listOf(
    FavoriteSound(
        1,
        "비 내리는 숲속",
        listOf(R.drawable.ic_rain, R.drawable.ic_water_drop, R.drawable.ic_wind)
    ),
    FavoriteSound(
        2,
        "도시 소음",
        listOf(R.drawable.ic_car, R.drawable.ic_cafe, R.drawable.ic_keyboard, R.drawable.ic_build),
        isPlaying = true
    ),
    FavoriteSound(
        3,
        "바다 소리",
        listOf(R.drawable.ic_sea_waves, R.drawable.ic_wind, R.drawable.ic_bird)
    ), // Assuming bird for the last icon
    FavoriteSound(
        4,
        "수면 모드",
        listOf(
            R.drawable.ic_wind,
            R.drawable.ic_water_drop,
            R.drawable.ic_clock,
            R.drawable.ic_fire
        )
    ), // Assuming clock and fire
    FavoriteSound(
        5,
        "집중 모드",
        listOf(R.drawable.ic_wind, R.drawable.ic_fire, R.drawable.ic_clock)
    ) // Assuming clock
)

@Composable
fun FavoriteSoundsScreen(
    onNavigateBack: () -> Unit = {},
) {
    var favoriteSounds by remember { mutableStateOf(dummyFavoriteSounds) }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "즐겨찾기한 소리",
                navigationIcon = Icons.Filled.Close,
                actionIcon = null,
                onNavigationClick = onNavigateBack,
                onActionClick = { }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp) // Add padding top and bottom for the list
        ) {
            items(favoriteSounds, key = { it.id }) { sound ->
                FavoriteSoundItem(
                    sound = sound,
                    onPlayPauseClick = { id ->
                        favoriteSounds = favoriteSounds.map {
                            if (it.id == id) it.copy(isPlaying = !it.isPlaying) else it.copy(
                                isPlaying = false
                            ) // Toggle play, stop others
                        }
                    },
                    onDeleteClick = { id ->
                        favoriteSounds = favoriteSounds.filterNot { it.id == id }
                    }
                )
            }
        }
    }
}

@Composable
fun FavoriteSoundItem(
    sound: FavoriteSound,
    onPlayPauseClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest // Dark gray background from image
        ),
        border = if (sound.isPlaying) BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.primary
        ) else null // Teal border if playing
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp), // Increased vertical padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp) // Space between title and icons
            ) {
                Text(
                    text = sound.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    sound.icons.forEach { iconRes ->
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null, // Decorative icons
                            tint = MaterialTheme.colorScheme.primary, // Teal color for icons
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Play/Pause Button
                IconButton(
                    onClick = { onPlayPauseClick(sound.id) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(id = if (sound.isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                        contentDescription = if (sound.isPlaying) "Pause" else "Play",
                        tint = Color.White, // Icon color inside button
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Delete Button
                IconButton(
                    onClick = { onDeleteClick(sound.id) },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.White, // Icon color inside button
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E) // Dark background for preview
@Composable
fun FavoriteSoundsScreenPreview() {
    PomoSoundTheme {
        FavoriteSoundsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteSoundItemPreview() {
    PomoSoundTheme {
        FavoriteSoundItem(
            sound = dummyFavoriteSounds[0],
            onPlayPauseClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteSoundItemPlayingPreview() {
    PomoSoundTheme {
        FavoriteSoundItem(
            sound = dummyFavoriteSounds[1],
            onPlayPauseClick = {},
            onDeleteClick = {}
        )
    }
}
