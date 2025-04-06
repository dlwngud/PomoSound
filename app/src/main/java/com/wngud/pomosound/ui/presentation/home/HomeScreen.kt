package com.wngud.pomosound.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.wngud.pomosound.ui.components.CustomTopAppBar
import com.wngud.pomosound.ui.theme.PomoSoundTheme

val places = listOf(
    PlaceItemData(1, "모닥불", com.wngud.pomosound.R.drawable.ic_place_fire),
    PlaceItemData(2, "비오는 방 안", com.wngud.pomosound.R.drawable.ic_place_rain),
    PlaceItemData(3, "도서관", com.wngud.pomosound.R.drawable.ic_place_library),
    PlaceItemData(4, "고요한 숲 속", com.wngud.pomosound.R.drawable.ic_place_forest),
    PlaceItemData(5, "해변", com.wngud.pomosound.R.drawable.ic_place_beach),
    PlaceItemData(6, "계곡", com.wngud.pomosound.R.drawable.ic_place_valley),
    PlaceItemData(7, "바람 부는 들판", com.wngud.pomosound.R.drawable.ic_place_field),
    PlaceItemData(8, "느리게 흘러가는 구름", com.wngud.pomosound.R.drawable.ic_place_cloud),
    PlaceItemData(9, "따뜻한 카페 안", com.wngud.pomosound.R.drawable.ic_place_cafe),
    PlaceItemData(10, "우주", com.wngud.pomosound.R.drawable.ic_place_space),
    PlaceItemData(11, "강가의 자갈밭", com.wngud.pomosound.R.drawable.ic_place_stone),
    PlaceItemData(12, "떨어지는 물방울", com.wngud.pomosound.R.drawable.ic_place_water_drop),
    PlaceItemData(13, "도시의 야경", com.wngud.pomosound.R.drawable.ic_place_nightscape),
    PlaceItemData(14, "조용한 공원 벤치", com.wngud.pomosound.R.drawable.ic_place_park),
    PlaceItemData(15, "고요히 내리는 눈", com.wngud.pomosound.R.drawable.ic_place_snow),
    PlaceItemData(16, "깊은 바다속", com.wngud.pomosound.R.drawable.ic_place_deep_sea),
    PlaceItemData(17, "폭포", com.wngud.pomosound.R.drawable.ic_place_waterfall),
    PlaceItemData(18, "사막", com.wngud.pomosound.R.drawable.ic_place_desert),
    PlaceItemData(19, "비행", com.wngud.pomosound.R.drawable.ic_place_fly),
    PlaceItemData(20, "꽃밭", com.wngud.pomosound.R.drawable.ic_place_flower)
)

@Composable
fun HomeScreen(
    onNextClick: () -> Unit = {},
    onSettingClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "어디서 집중할까요?",
                navigationIcon = null,
                actionIcon = Icons.Outlined.Settings,
                onNavigationClick = {},
                onActionClick = { onSettingClick() }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(places) { item ->
                SoundItem(item = item) {
                    onNextClick()
                }
            }
        }
    }
}

@Composable
fun SoundItem(
    item: PlaceItemData,
    onNextClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onNextClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.imageRes)
                    .size(256, 256)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = item.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.3f)),
                            startY = 0.6f * Float.MAX_VALUE
                        )
                    )
            )

            Text(
                text = item.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = androidx.compose.ui.geometry.Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PomoSoundTheme {
        HomeScreen({}, {})
    }
}