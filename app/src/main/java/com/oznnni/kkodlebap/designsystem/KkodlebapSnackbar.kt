package com.oznnni.kkodlebap.designsystem

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFilterNotNull
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import com.oznnni.kkodlebap.ui.theme.KkodlebapTheme
import com.oznnni.kkodlebap.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@Composable
fun KkodlebapSnackbar(message: String) {
    Column(
        modifier = Modifier
            .padding(horizontal = 18.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier
                .background(KkodlebapTheme.colors.gray700, RoundedCornerShape(45.dp))
                .padding(vertical = 16.dp, horizontal = 36.dp),
            text = message,
            color = KkodlebapTheme.colors.white,
            textAlign = TextAlign.Center,
            style = Typography.SuitR5,
        )

        Spacer(modifier = Modifier.size(36.dp))
    }
}

@Composable
private fun animatedOpacity(
    animation: AnimationSpec<Float>,
    visible: Boolean,
    onAnimationFinish: () -> Unit
): androidx.compose.runtime.State<Float> {
    val targetAlpha = if (visible) 1f else 0f
    return animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = animation,
        finishedListener = {
            if (!visible) onAnimationFinish()
        }, label = ""
    )
}

@Composable
private fun animatedScale(
    animation: AnimationSpec<Float>,
    visible: Boolean
): androidx.compose.runtime.State<Float> {
    val targetScale = if (visible) 1f else 0.8f
    return animateFloatAsState(
        targetValue = targetScale,
        animationSpec = animation,
        label = ""
    )
}

/**
 * @param current 현재 보여줄 스낵바 데이터
 * @param modifier Modifier 적용
 * @param content 실제 스낵바의 UI (T는 데이터 타입)
 */
@Composable
fun <T> KkodlebapSnackbarHost(
    current: T?,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    val state = remember { State<T>() }

    if (current != state.current) {
        state.current = current
        val keys = state.items.fastMap { it.first }.toMutableList()
        if (current != null && !keys.contains(current)) {
            keys.add(current)
        }

        state.items.clear()
        keys.mapTo(state.items) { key ->
            key to @Composable { children ->
                val isVisible = key == current
                val duration = if (isVisible) SnackbarFadeInMillis else SnackbarFadeOutMillis
                val delay = SnackbarFadeOutMillis + SnackbarInBetweenDelayMillis
                val animationDelay = if (isVisible && keys.fastFilterNotNull().size != 1) {
                    delay
                } else {
                    0
                }

                val opacity = animatedOpacity(
                    animation = tween(
                        durationMillis = duration,
                        delayMillis = animationDelay,
                        easing = LinearEasing
                    ),
                    visible = isVisible,
                    onAnimationFinish = {
                        if (key != state.current) {
                            state.items.removeAll { it.first == key }
                            state.scope?.invalidate()
                        }
                    }
                )

                val scale = animatedScale(
                    animation = tween(
                        durationMillis = duration,
                        delayMillis = animationDelay,
                        easing = FastOutSlowInEasing
                    ),
                    visible = isVisible
                )

                Box(
                    Modifier
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            alpha = opacity.value
                        )
                ) {
                    children()
                }
            }
        }
    }

    Box(modifier) {
        state.scope = currentRecomposeScope
        state.items.fastForEach { (item, opacity) ->
            key(item) {
                opacity {
                    if (item != null) content(item)
                }
            }
        }
    }
}

private class State<T> {
    var current: T? = null
    var scope: RecomposeScope? = null
    val items = mutableStateListOf<Pair<T?, @Composable ((@Composable () -> Unit) -> Unit)>>()
}

fun SnackbarDuration.toMillis(): Long = when (this) {
    SnackbarDuration.Indefinite -> Long.MAX_VALUE
    SnackbarDuration.Long -> 4000L
    SnackbarDuration.Short -> 1500L
}

suspend fun showKkodelbapSnackbar(
    message: String,
    snackbarDuration: SnackbarDuration = SnackbarDuration.Long,
    updateSnackbarData: (String?) -> Unit,
) {
    updateSnackbarData(message)
    delay(snackbarDuration.toMillis())
    updateSnackbarData(null)
}

private const val SnackbarFadeInMillis = 150
private const val SnackbarFadeOutMillis = 75
private const val SnackbarInBetweenDelayMillis = 0
