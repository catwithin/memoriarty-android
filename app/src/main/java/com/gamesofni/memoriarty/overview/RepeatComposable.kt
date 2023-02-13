package com.gamesofni.memoriarty.overview

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gamesofni.memoriarty.R
import com.gamesofni.memoriarty.repeat.Repeat
import com.gamesofni.memoriarty.ui.MemoriartyTheme


@Composable
internal fun RepeatComposable(
    repeat: Repeat,
    onDone: (Repeat) -> Unit,
    onOpenDetailedView: (Repeat) -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: this state is not saved on device rotation (recyvled composable?)
    var expanded by rememberSaveable { mutableStateOf(false) }
//    val extraPadding by animateDpAsState(
//        targetValue = if (expanded) 56.dp else 0.dp,
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow
//        )
//    )
//    MemoriartyTheme() {
    Card(
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),

        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                //                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.niedi),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                    )
            ) {
                Text(text = repeat.description,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 2,
                    modifier = Modifier.padding(end = 16.dp),
                )
//                Text(text = name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight= FontWeight.ExtraBold))
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Text(text = "project_id: $repeat.projectId", maxLines
                    = 2, modifier = Modifier.weight(0.8f))
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onSurface,
                            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (expanded) "Show less" else "Show more",
                            modifier = Modifier.weight(0.2f)
                        )
                    }
                }
                if (expanded) {
                    Text(text = repeat.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(end = 16.dp),
                    )
                    Row() {
                        Button(onClick = { onDone(repeat) }) {
                            Text("Done")
                        }
                        Button(onClick = { onOpenDetailedView(repeat) }) {
                            Text("Details")
                        }

                    }
                }

            }
//            Text(if (expanded) "Show less" else "Show more")

        }
    }
//    }
}


// <---- Previews ---->

@Preview(showBackground = true)
@Composable
fun RepeatPreviewDark() {
    MemoriartyTheme(darkTheme = true, dynamicColor = true) {
        RepeatComposable(
            previewRepeat,
            onDone = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 1)
@Composable
fun RepeatPreviewLight() {
    MemoriartyTheme(darkTheme = false, dynamicColor = true) {
        RepeatComposable(
            previewRepeat2,
            onDone = {})
    }
}
