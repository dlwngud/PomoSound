package com.wngud.pomosound.ui.presentation.sound

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wngud.pomosound.data.model.SoundData
import com.wngud.pomosound.ui.components.CustomBottomAppBar
import com.wngud.pomosound.ui.components.CustomTopAppBar
import com.wngud.pomosound.ui.theme.PomoSoundTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SoundScreen(
    onBackClick: () -> Unit = {},
    onNextClick: (Int) -> Unit = {},
    soundViewModel: SoundViewModel = hiltViewModel()
) {
    val uiState by soundViewModel.uiState.collectAsStateWithLifecycle()
    val isPlaying by soundViewModel.isPlaying.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        soundViewModel.sideEffects.collectLatest { sideEffect ->
            when (sideEffect) {
                SoundSideEffect.NavigateBack -> onBackClick()
                is SoundSideEffect.NavigateToNext -> onNextClick(sideEffect.id)
                is SoundSideEffect.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(sideEffect.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "어떤 소리를 들을래요?",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                actionIcon = null,
                onNavigationClick = { soundViewModel.navigateToBack() },
                onActionClick = {}
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                isPlaying = isPlaying,
                onFavoriteClick = {},
                onAddClick = {},
                onPlayClick = { soundViewModel.postEvent(SoundEvent.GlobalPlayPauseClicked) },
                onNextClick = { soundViewModel.navigateToNext(soundViewModel.placeId) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.soundCategories) { category ->
                    Column {
                        Text(
                            text = category.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp),
                            color = Color.White
                        )
                        val columns = 3
                        val rows = (category.sounds.size + columns - 1) / columns
                        val gridHeight =
                            (rows * (80 + 4 + 48 + 12)).dp // icon + spacer + control + vertical spacing

                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 85.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(gridHeight), // Use calculated height
                            contentPadding = PaddingValues(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                12.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            userScrollEnabled = false
                        ) {
                            items(category.sounds) { sound ->
                                val isSoundSelected =
                                    uiState.isSoundSelected(sound.id) // Check if selected
                                val volume = uiState.getVolume(sound.id)
                                SoundIcon(
                                    sound = sound,
                                    isSelected = isSoundSelected, // Use selected state for UI
                                    volume = volume, // Use volume from ViewModel
                                    onSelect = {
                                        soundViewModel.postEvent(SoundEvent.SoundItemClicked(sound))
                                    },
                                    onVolumeChange = { newVolume ->
                                        soundViewModel.postEvent(
                                            SoundEvent.VolumeChanged(
                                                sound.id,
                                                newVolume
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SoundIcon(
    sound: SoundData,
    isSelected: Boolean, // Renamed from isSoundPlaying for clarity in this scope
    volume: Float,
    onSelect: () -> Unit,
    onVolumeChange: (Float) -> Unit
) {
    val iconBgColor = MaterialTheme.colorScheme.surfaceContainerLowest
    val border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    val iconSize = 80.dp
    val controlHeight = 48.dp // Height for either Slider or Text

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(iconSize)
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(12.dp))
                .background(iconBgColor)
                .then(
                    if (border != null) Modifier.border(
                        border,
                        RoundedCornerShape(12.dp)
                    ) else Modifier
                )
                .clickable { onSelect() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = sound.icon),
                contentDescription = sound.name,
                tint = if (!isSelected) Color.White else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(iconSize * 0.5f)
            )
        }

        // Spacer for consistent layout regardless of slider visibility
        Spacer(modifier = Modifier.height(4.dp))

        Box(modifier = Modifier.height(controlHeight)) { // Use Box to reserve space
            if (isSelected) {
                Slider(
                    value = volume,
                    onValueChange = onVolumeChange,
                    modifier = Modifier
                        .width(iconSize)
                        .fillMaxHeight() // Fill the reserved height
                        .align(Alignment.Center), // Center slider within the box
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(
                            alpha = 0.5f
                        )
                    )
                )
            } else {
                Text(
                    text = sound.name,
                    fontSize = 14.sp, // Slightly smaller to fit better
                    fontWeight = FontWeight.Medium, // Adjusted weight
                    color = Color.White, // Ensure text is visible
                    modifier = Modifier.align(Alignment.Center) // Center text within the box
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1C1E)
@Composable
fun SoundScreenPreview() {
    PomoSoundTheme {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    title = "어떤 소리를 들을래요?",
                    navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                    actionIcon = null,
                    onNavigationClick = { },
                    onActionClick = {}
                )
            },
            bottomBar = {
                CustomBottomAppBar(
                    isPlaying = false, // Preview with default state
                    onFavoriteClick = {},
                    onAddClick = {},
                    onPlayClick = { },
                    onNextClick = { }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Text("Preview Content", Modifier.padding(paddingValues), color = Color.White)
        }
    }
}
