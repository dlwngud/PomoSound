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
import com.wngud.pomosound.R
import com.wngud.pomosound.ui.components.CustomBottomAppBar
import com.wngud.pomosound.ui.components.CustomTopAppBar
import com.wngud.pomosound.ui.theme.PomoSoundTheme

data class SoundData(
    val id: Int,
    val name: String,
    val icon: Int,
)

data class SoundCategory(
    val name: String,
    val sounds: List<SoundData>
)

val soundCategories = listOf(
    SoundCategory(
        name = "자연",
        sounds = listOf(
            SoundData(1, "비", R.drawable.ic_rain),
            SoundData(2, "천둥", R.drawable.ic_thunder),
            SoundData(3, "파도", R.drawable.ic_sea_waves),
            SoundData(4, "새", R.drawable.ic_bird),
            SoundData(5, "계곡", R.drawable.ic_valley),
            SoundData(6, "바람", R.drawable.ic_wind),
            SoundData(7, "풀벌레", R.drawable.ic_bug),
            SoundData(8, "모닥불", R.drawable.ic_fire),
            SoundData(9, "낙엽", R.drawable.ic_leaf),
            SoundData(10, "물방울", R.drawable.ic_water_drop),
            SoundData(11, "눈", R.drawable.ic_snow),
        )
    ),
    SoundCategory(
        name = "일상",
        sounds = listOf(
            SoundData(12, "책", R.drawable.ic_book),
            SoundData(13, "선풍기", R.drawable.ic_pan),
            SoundData(14, "커피 머신", R.drawable.ic_cafe),
            SoundData(15, "시계", R.drawable.ic_clock),
            SoundData(16, "연필", R.drawable.ic_pencil),
            SoundData(17, "키보드", R.drawable.ic_keyboard),
        )
    ),
    SoundCategory(
        name = "도시",
        sounds = listOf(
            SoundData(18, "웅성거림", R.drawable.ic_noisily),
            SoundData(19, "자동차", R.drawable.ic_car),
            SoundData(20, "기차", R.drawable.ic_train),
            SoundData(21, "공사", R.drawable.ic_build),
            SoundData(22, "비행기", R.drawable.ic_plane),
        )
    ),
)

val iconBackgroundColor = Color(0xFF4F545C)
val selectedBorderColor = Color(0xFF66DDAA)
val iconColor = Color.White

const val DEFAULT_VOLUME = 0.0f

@Composable
fun SoundScreen(
    onBackClick: () -> Unit = {},
    onNextClick: () -> Unit = {}
) {
    val selectedSounds = remember { mutableStateMapOf<Int, Float>() }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "어떤 소리를 들을래요?",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                actionIcon = null,
                onNavigationClick = onBackClick,
                onActionClick = {}
            )
        },
        bottomBar = {
            CustomBottomAppBar(
                onFavoriteClick = {},
                onAddClick = {},
                onPlayClick = { },
                onNextClick = { onNextClick() }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(soundCategories) { category ->
                Column {
                    Text(
                        text = category.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 85.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(((category.sounds.size + 2) / 3 * 140).dp),
                        contentPadding = PaddingValues(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            12.dp,
                            Alignment.CenterHorizontally
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        userScrollEnabled = false
                    ) {
                        items(category.sounds) { sound ->
                            val isSelected = selectedSounds.containsKey(sound.id)
                            SoundIcon(
                                sound = sound,
                                isSelected = isSelected,
                                volume = selectedSounds[sound.id] ?: 0f,
                                onSelect = {
                                    if (isSelected) {
                                        selectedSounds.remove(sound.id)
                                    } else {
                                        selectedSounds[sound.id] = DEFAULT_VOLUME
                                    }
                                },
                                onVolumeChange = { newVolume ->
                                    if (isSelected) {
                                        selectedSounds[sound.id] = newVolume
                                    }
                                }
                            )
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
    isSelected: Boolean,
    volume: Float,
    onSelect: () -> Unit,
    onVolumeChange: (Float) -> Unit
) {
    val iconBgColor = iconBackgroundColor
    val border = if (isSelected) BorderStroke(2.dp, selectedBorderColor) else null
    val iconSize = 80.dp
    val sliderHeight = 48.dp

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
                tint = if (!isSelected) iconColor else selectedBorderColor,
                modifier = Modifier.size(iconSize * 0.5f)
            )
        }

        if (isSelected) {
            Slider(
                value = volume,
                onValueChange = onVolumeChange,
                modifier = Modifier
                    .width(iconSize)
                    .height(sliderHeight),
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = selectedBorderColor,
                    activeTrackColor = selectedBorderColor.copy(alpha = 0.7f),
                    inactiveTrackColor = iconBackgroundColor.copy(alpha = 0.5f)
                )
            )
        } else {
            Text(
                text = sound.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.height(sliderHeight)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF2C2F33)
@Composable
fun SoundScreenPreview() {
    PomoSoundTheme {
        SoundScreen()
    }
}
