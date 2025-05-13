package com.wngud.pomosound.ui.presentation.setting

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onBackClick: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf(25) } // Default time
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            TimerSettingsSection(onTimeSettingClick = { showBottomSheet = true })

            ScreenSettingsSection()

            NotificationsAndAccessibilitySection()

            AppInfoAndSupportSection()

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            contentColor = Color.White
        ) {
            TimePickerBottomSheet()
        }
    }
}

@Composable
fun TimePickerBottomSheet() {
    val minute by remember { mutableStateOf(25) }

    val minuteListState = rememberLazyListState(minute - 25)

    val density = LocalDensity.current
    val threshold = remember { density.run { 20.dp.toPx() } } // 한 칸 높이의 절반
    val min by remember { derivedStateOf { (minuteListState.firstVisibleItemIndex + if (minuteListState.firstVisibleItemScrollOffset >= threshold) 26 else 25) } }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        LazyColumn(
            state = minuteListState,
            contentPadding = PaddingValues(16.dp, 80.dp),
            flingBehavior = rememberSnapFlingBehavior(minuteListState),
            modifier = Modifier
                .height(200.dp)
        ) {
            items((25..60).toList()) { int ->
                val textColor by animateColorAsState(
                    if (int == min) MaterialTheme.colorScheme.primary else Color.White
                )

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    BasicText(
                        text = "${int}분",
                        style = LocalTextStyle.current.copy(
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = { textColor }
                    )
                }
            }
        }
    }
}

@Composable
fun TimerSettingsSection(onTimeSettingClick: () -> Unit) {
    SectionHeader(title = "타이머 설정")

    SettingItem(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "⏰",
        title = "시간 설정",
        description = "기본 25분 외에 자유 입력 또는 미리 설정된 옵션",
        onClick = onTimeSettingClick // Add onClick
    )

    SettingItem(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "⏱️",
        title = "포모도로 모드",
        description = "집중 시간 + 휴식 시간 반복 사이클 설정"
    )

    SettingItem(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "🔔",
        title = "타이머 알림",
        description = "종료 시 알림음, 볼륨, 진동 및 반복 설정"
    )
}

@Composable
fun ScreenSettingsSection() {
    SectionHeader(title = "화면 꺼짐 방지")

    // Keep Screen On
    SettingItemWithSwitch(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "📱",
        title = "화면 유지",
        description = "타이머 작동 중 화면 설정 옵션",
        initialSwitchState = true
    )

    // Battery Saving Mode
    SettingItemWithSwitch(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "🔋",
        title = "배터리 절약 모드",
        description = "화면 밝기 자동 낮춤 + 동영상 일시정지 옵션",
        initialSwitchState = false
    )
}

@Composable
fun NotificationsAndAccessibilitySection() {
    SectionHeader(title = "알림 및 접근성")

    SettingItemWithSwitch(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "🔔",
        title = "푸시 알림",
        description = "집중 시간 리마인더 설정",
        initialSwitchState = true
    )

    SettingItem(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "🌐",
        title = "언어 선택",
        description = "앱 내 언어 및 설명 언어 설정"
    )
}

@Composable
fun AppInfoAndSupportSection() {
    SectionHeader(title = "앱 정보 및 지원")

    SettingItem(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "💬",
        title = "피드백 및 문의",
        description = "개발자에게 피드백 전송 및 FAQ"
    )

    SettingItem(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "📝",
        title = "약관 및 개인정보",
        description = "서비스 이용약관, 개인정보 처리방침"
    )

    SettingItem(
        iconBackground = MaterialTheme.colorScheme.surfaceContainerLowest,
        icon = "ℹ️",
        title = "버전 정보",
        description = "현재 앱 버전 표시 및 업데이트 확인"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    navigationIcon: ImageVector?,
    actionIcon: ImageVector?,
    onNavigationClick: () -> Unit,
    onActionClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            if (navigationIcon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(imageVector = navigationIcon, contentDescription = "Navigation")
                }
            }
        },
        actions = {
            if (actionIcon != null) {
                IconButton(onClick = onActionClick) {
                    Icon(imageVector = actionIcon, contentDescription = "Action")
                }
            }
        }
    )
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun SettingItem(
    iconBackground: Color,
    icon: String,
    title: String,
    description: String,
    onClick: () -> Unit = {} // Add onClick
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }, // Make clickable
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBackground)
            ) {
                Text(
                    text = icon,
                    fontSize = 16.sp
                )
            }

            // Text content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun SettingItemWithSwitch(
    iconBackground: Color,
    icon: String,
    title: String,
    description: String,
    initialSwitchState: Boolean
) {
    var switchState by remember { mutableStateOf(initialSwitchState) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBackground)
            ) {
                Text(
                    text = icon,
                    fontSize = 16.sp
                )
            }

            // Text content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Switch
            Switch(
                checked = switchState,
                onCheckedChange = { switchState = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingScreen() {
    SettingScreen(onBackClick = {})
}
