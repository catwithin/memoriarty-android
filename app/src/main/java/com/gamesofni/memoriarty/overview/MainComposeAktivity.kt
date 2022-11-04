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
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.gamesofni.memoriarty.ui.MemoriartyTheme
import java.util.*


class MainComposeAktivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoriartyTheme {
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

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    MemoriartyTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}


@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" }
) {
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

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier
            .padding(24.dp)
            //                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(text = "Hello, ")
//                Text(text = name, style = MemoriartyTheme.typography.headlineMedium)
                Text(text = name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight= FontWeight.ExtraBold))
                if (expanded) {
                    Text(("Composem ipsum color sit lazy, " +
                            "padding theme elit, sed do bouncy. ").repeat(name.toInt().mod(10)))
                }
            }
//            Text(if (expanded) "Show less" else "Show more")
            IconButton(onClick = { expanded = !expanded }) {
                Icon(

                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) "Show less" else "Show more"
                )
            }
        }
    }
}


@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingsPreview() {
    MemoriartyTheme {
        Greetings()
    }
}


@Preview
@Composable
fun MyAppPreview() {
    MemoriartyTheme {
        AppContainer(Modifier.fillMaxSize())
    }
}
