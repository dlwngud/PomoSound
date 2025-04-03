package com.wngud.pomosound.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navigationIcon: ImageVector? = null,
    actionIcon: ImageVector? = null,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = navigationIcon?.let { icon ->
            {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Navigation icon"
                    )
                }
            }
        } ?: {},
        actions = actionIcon?.let { icon ->
            {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Action icon"
                    )
                }
            }
        } ?: {}
    )
}