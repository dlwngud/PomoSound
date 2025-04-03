package com.wngud.pomosound.ui.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.wngud.pomosound.ui.components.CustomTopAppBar

@Composable
fun HomeScreen(
    onNextClick: () -> Unit,
    onSettingClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "어디서 공부할래요?",
                navigationIcon = null,
                actionIcon = Icons.Default.Settings,
                onNavigationClick = {},
                onActionClick = onSettingClick
            )
        },
    ) {

    }
}