package com.gamesofni.memoriarty.overview

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.gamesofni.memoriarty.R
import com.gamesofni.memoriarty.ui.MemoriartyTheme
import java.util.*


class MainComposeAktivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoriartyTheme(dynamicColor = true) {
                AppContainer(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun AppContainer(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }
}


@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Memoriarty!")
        Text("Let me show you around!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Continue")
        }
    }
}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(100) { "$it" }
) {
    Image(
        painter = painterResource(R.drawable.celebration),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth()

    )


    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) {
            name -> Greeting(name = name)
        }
    }
}


val random = Random(100)


@Composable
private fun Greeting(name: String) {
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
                    Text(text = name,
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 2,
                        modifier = Modifier.padding(end = 16.dp),
                    )
//                Text(text = name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight= FontWeight.ExtraBold))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,

                    ) {
                        Text(text = "Hello, I'm the project name if you ever wondered ", maxLines
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
                        Text(
                            ("Composem ipsum color sit lazy, " +
                                    "padding theme elit, sed do bouncy. ").repeat(
                                name.toInt().mod(10)
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
//            Text(if (expanded) "Show less" else "Show more")

            }
        }
//    }
}


@Preview
@Composable
fun MyAppPreview() {
    MemoriartyTheme (dynamicColor = false) {
        AppContainer(Modifier.fillMaxSize())
    }
}


@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    MemoriartyTheme(dynamicColor = false){
        OnboardingScreen(onContinueClicked = {})
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
// TODO: can I change shouldShowOnboarding var for testing?
fun AppPreview() {
    MemoriartyTheme(dynamicColor = false){
        AppContainer()
    }
}


@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Composable
fun GreetingsPreviewDark() {
    MemoriartyTheme(darkTheme = true, dynamicColor = false) {
        Greetings()
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingsPreview() {
    MemoriartyTheme(darkTheme = false, dynamicColor = true) {
        Greetings()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreviewDark() {
    MemoriartyTheme(darkTheme = true, dynamicColor = true) {
        Greeting("I'm the Chunk name ... a very long chunk name in fact")
    }
}

@Preview(showBackground = true, backgroundColor = 1)
@Composable
fun GreetingPreview() {
    MemoriartyTheme(darkTheme = false, dynamicColor = true) {
//        Surface(color = Color.Black) {
            Greeting("Shpyak")
//        }
    }
}


@Composable
fun FloatingActionButton(
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
) {
    val derivedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    Surface(color = MaterialTheme.colorScheme.primary,
//      contentColorFor:  if you set a primary background, it will return onPrimary as the
//      content color
        contentColor = contentColorFor(backgroundColor)) {
        Text(
            text = "Hard coded colors don't respond to theme changes :(",
            color = Color(0xffff00ff),
//            color = MaterialTheme.colorScheme.primary
        )
    }
}


