package com.wngud.pomosound.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wngud.pomosound.R

@Composable
fun CustomBottomAppBar(
    onFavoriteClick: () -> Unit,
    onAddClick: () -> Unit,
    onPlayClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Box {
        BottomAppBar(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
            actions = {
                Row(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { onFavoriteClick() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart),
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = { onAddClick() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart_plus),
                            contentDescription = "Localized description"
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNextClick() },
                    containerColor = Color(0xFF66DDAA)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, "Localized description")
                }
            }
        )

        IconButton(
            onClick = { onPlayClick() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "Localized description"
            )
        }
    }
}

@Preview
@Composable
fun CustomBottomAppBarPreview() {
    Scaffold(
        bottomBar = {
            CustomBottomAppBar(
                onFavoriteClick = {},
                onAddClick = {},
                onPlayClick = {},
                onNextClick = {}
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
    }
}