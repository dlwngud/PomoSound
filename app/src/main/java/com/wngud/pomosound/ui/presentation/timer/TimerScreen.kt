package com.wngud.pomosound.ui.presentation.timer

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.skydoves.cloudy.cloudy
import com.wngud.pomosound.R
import com.wngud.pomosound.ui.theme.PomoSoundTheme
import kotlinx.coroutines.delay

@Composable
fun TimerScreen() {
    /*
    신경과학적으로 입증된 방법
    집중 모드 활성화:

    소리내어 "5, 4, 3, 2, 1" 카운트다운
    깊게 숨을 들이마시고 잠시 참기
    일을 시작하세요!
    */
    val context = LocalContext.current
    var applyBlur by remember { mutableStateOf(true) }
    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/${R.raw.fire_bg}")
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            repeatMode = Player.REPEAT_MODE_ONE
            prepare()
            playWhenReady = true

            MediaMetadataRetriever().use { retriever ->
                retriever.setDataSource(context, videoUri)
                thumbnail = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST)
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            VideoPlayer(
                exoPlayer = exoPlayer,
                modifier = Modifier
                    .fillMaxSize()
            )

            if(applyBlur) {
                thumbnail?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .cloudy(radius = 20)
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CountdownScreen(onTimerStart = { applyBlur = false })
            }
        }
    }
}

@Composable
fun VideoPlayer(exoPlayer: ExoPlayer, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        },
        modifier = modifier
    )
}

@Composable
fun CountdownScreen(onTimerStart: () -> Unit) {
    var count by remember { mutableStateOf(5) }
    var showLetsGo by remember { mutableStateOf(false) }
    var showTimer by remember { mutableStateOf(false) }
    val scale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (count > 0) {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500)
            )
            delay(500)
            scale.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 500)
            )
            count--
        }
        showLetsGo = true
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
        delay(500)
        scale.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 500)
        ) {
            showLetsGo = false
            showTimer = true
            onTimerStart()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            count > 0 -> {
                Text(
                    text = count.toString(),
                    fontSize = 100.sp,
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.scale(scale.value),
                    color = Color.White
                )
            }
            showLetsGo -> {
                Text(
                    text = "Let's Go!",
                    fontSize = 60.sp,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.scale(scale.value),
                    color = Color.White
                )
            }
            showTimer -> {
                PomodoroTimer(
                    totalTime = 25 * 60 * 1000L,
                    modifier = Modifier.size(300.dp)
                )
            }
        }
    }
}

@Composable
fun PomodoroTimer(
    totalTime: Long,
    modifier: Modifier = Modifier
) {
    var remainingTime by remember { mutableStateOf(totalTime) }

    LaunchedEffect(remainingTime) {
        if (remainingTime > 0) {
            delay(1000L)
            remainingTime -= 1000L
        }
    }

    val minutes = (remainingTime / 1000 / 60).toInt()
    val seconds = (remainingTime / 1000 % 60).toInt()
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = formattedTime,
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    PomoSoundTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Timer Screen Preview (Video not shown)")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PomodoroTimerPreview() {
    PomoSoundTheme {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            PomodoroTimer(
                totalTime = 25 * 60 * 1000L,
                modifier = Modifier.size(300.dp)
            )
        }
    }
}