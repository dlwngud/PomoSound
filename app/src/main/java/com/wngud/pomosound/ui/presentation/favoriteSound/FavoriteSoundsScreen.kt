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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wngud.pomosound.R
import com.wngud.pomosound.data.db.FavoriteSoundWithItems
import com.wngud.pomosound.ui.components.CustomTopAppBar
import com.wngud.pomosound.ui.theme.PomoSoundTheme


@Composable
fun FavoriteSoundsScreen(
    onBackClick: () -> Unit = {},
    viewModel: FavoriteSoundsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "즐겨찾기한 소리",
                navigationIcon = Icons.Filled.Close,
                actionIcon = null,
                onNavigationClick = onBackClick,
                onActionClick = { } // No action needed in this screen's top bar
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center // Center loading indicator
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (state.favoriteSounds.isEmpty()) {
                Text(
                    text = "즐겨찾기한 소리가 없습니다.",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(state.favoriteSounds, key = { it.favoriteSound.id }) { favoriteSoundWithItems ->
                        val isPlaying = state.playingSoundId == favoriteSoundWithItems.favoriteSound.id
                        FavoriteSoundItem(
                            sound = favoriteSoundWithItems,
                            isPlaying = isPlaying,
                            onPlayPauseClick = {
                                if (isPlaying) {
                                    viewModel.handleIntent(FavoriteSoundsIntent.PauseSound)
                                } else {
                                    viewModel.handleIntent(FavoriteSoundsIntent.PlaySound(favoriteSoundWithItems))
                                }
                            },
                            onDeleteClick = {
                                viewModel.handleIntent(FavoriteSoundsIntent.DeleteSound(favoriteSoundWithItems.favoriteSound))
                            },
                            soundIdToResIdMap = state.soundIdToResIdMap
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteSoundItem(
    sound: FavoriteSoundWithItems,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    soundIdToResIdMap: Map<Int, Int>, // Add the map as a parameter
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        border = if (isPlaying) BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.primary
        ) else null
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp), // Increased vertical padding
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = sound.favoriteSound.name, // Use name from FavoriteSound entity
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Display icons based on the soundId from FavoriteSoundItem
                    sound.items.forEach { item ->
                        // Use the map from the ViewModel to get the correct resource ID
                        val soundResId = soundIdToResIdMap[item.soundId] ?: R.drawable.ic_plane // Default icon if not found
                        val iconRes = getIconResourceForSoundId(soundResId)
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Play/Pause Button
                IconButton(
                    onClick = onPlayPauseClick, // Use the passed lambda directly
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Delete Button
                IconButton(
                    onClick = onDeleteClick, // Use the passed lambda directly
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

// Helper function to map soundId to drawable resource ID
// TODO: Replace with your actual mapping logic
fun getIconResourceForSoundId(soundId: Int): Int {
    return when (soundId) {
        R.raw.rain -> R.drawable.ic_rain
        R.raw.thunder -> R.drawable.ic_thunder
        R.raw.wave -> R.drawable.ic_sea_waves
        R.raw.bird -> R.drawable.ic_bird
        R.raw.valley -> R.drawable.ic_valley
        R.raw.soft_wind -> R.drawable.ic_wind
        R.raw.bug -> R.drawable.ic_bug
        R.raw.fire -> R.drawable.ic_fire
        R.raw.fallen_leaves -> R.drawable.ic_leaf
        R.raw.water_drop -> R.drawable.ic_water_drop
        R.raw.snow -> R.drawable.ic_snow
        R.raw.book -> R.drawable.ic_book
        R.raw.fan -> R.drawable.ic_pan
        R.raw.coffee -> R.drawable.ic_cafe
        R.raw.clock -> R.drawable.ic_clock
        R.raw.pencil -> R.drawable.ic_pencil
        R.raw.keyboard -> R.drawable.ic_keyboard
        R.raw.conversation -> R.drawable.ic_noisily
        R.raw.car -> R.drawable.ic_car
        R.raw.train -> R.drawable.ic_train
        R.raw.roadworks -> R.drawable.ic_build
        else -> R.drawable.ic_plane
    }
}

// Previews might need adjustment or dummy ViewModel/State for isolation
@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E)
@Composable
fun FavoriteSoundsScreenPreview() {
    PomoSoundTheme {
        // Preview with dummy data or a fake ViewModel might be needed
        // For simplicity, showing an empty screen state
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    title = "즐겨찾기한 소리",
                    navigationIcon = Icons.Filled.Close,
                    onNavigationClick = {}
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Preview - No Data", color = Color.White)
            }
        }
    }
}