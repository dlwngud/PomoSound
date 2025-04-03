package com.wngud.pomosound.ui.presentation.setting

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.wngud.pomosound.ui.components.CustomTopAppBar

@Composable
fun SettingScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "설정",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                actionIcon = null,
                onNavigationClick = onBackClick,
                onActionClick = {}
            )
        }
    ) {

    }
}