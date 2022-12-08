package com.gamesofni.memoriarty.overview

import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gamesofni.memoriarty.R
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.repeat.Repeat
import com.gamesofni.memoriarty.ui.MemoriartyTheme
import java.time.Instant.now
import java.util.*


class MainComposeAktivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this).application
        val repeatsDao = MemoriartyDatabase.getInstance(application).repeatsDao
        val overviewModelFactory = OverviewViewModelFactory(repeatsDao, application)


        setContent {
            MemoriartyTheme(dynamicColor = true) {
                AppContainer(
                    overviewModelFactory = overviewModelFactory,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun AppContainer(
    overviewModelFactory: OverviewViewModelFactory,
    overviewViewModel: OverviewViewModel = viewModel(factory = overviewModelFactory),
    modifier: Modifier = Modifier,
) {
    viewModelFactory {}
    // TODO: save state of the shouldShowOnboarding into user's settings
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(false) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            val todayRepeats by overviewViewModel.todayRepeats.observeAsState()
            val overdueRepeats by overviewViewModel.overdueRepeats.observeAsState()

            // background
            Image(
                painter = painterResource(R.drawable.celebration),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = modifier
                    .fillMaxHeight()
                    .fillMaxWidth()

            )
            Repeats(
                todayRepeats,
                overdueRepeats,
                onDoneRepeat = { repeat: Repeat -> overviewViewModel.markAsDone(repeat) },
                modifier = modifier
            )
        }
    }
}

// TODO: find a way to have scrollable lists inside scrollable container
@Composable
private fun Repeats(
    todayRepeats: List<Repeat>?,
    overdueRepeats: List<Repeat>?,
    onDoneRepeat: (Repeat) -> Unit,
    modifier: Modifier
) {
    LazyColumn(modifier = modifier
        .padding(vertical = 4.dp)
//        .verticalScroll(rememberScrollState(), enabled = true)
    ) {

        item(){ SectionName(
            sectionName = "Today Repeats:",
        )}

        items(
            todayRepeats.orEmpty(),
            // TODO: thoroughly test which items are recomposed - can we do btr
            key = { it.id }
        ) {
                repeat -> RepeatComposable(
            repeat = repeat,
            onDone = onDoneRepeat,
            modifier = modifier
        )}

        item(){ SectionName(
            sectionName = "Old Repeats to Do:",
        )}

        items(
            overdueRepeats.orEmpty(),
            // TODO: thoroughly test which items are recomposed - can we do btr
            key = { it.id }
        ) {
                repeat -> RepeatComposable(
            repeat = repeat,
            onDone = onDoneRepeat,
            modifier = modifier
        )}

        item(){ AddChunk()}
    }
}

@Composable
fun SectionName(sectionName: String) {
    Text(sectionName)
}


//@Composable
//fun RepeatsSection(
//    sectionName: String,
//    repeats: List<Repeat>?,
//    onDoneRepeat: (Repeat) -> Unit,
//) {
//    Column() {
//
//    }
//    ListOfRepeats(
//        repeats = repeats,
//        onDoneRepeat = onDoneRepeat,
//    )
//}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: implement a real onboarding screen, with the kitten
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
fun AddChunk(modifier: Modifier = Modifier) {
    // TODO: implement add Chunk
    Column(modifier = modifier.padding(16.dp)) {
        var count by  rememberSaveable { mutableStateOf(0) }

        if (count > 0) {
            // This text is present if the button has been clicked
            // at least once; absent otherwise
            Text("You've had $count glasses.", color = MaterialTheme.colorScheme.onBackground)
        }
        // TODO: #BUG why enabled=false makes butto disappear instead of rendering it disabled
        Button(
            onClick = { count++ },
            Modifier.padding(top = 8.dp),
            enabled = count < 10
        ) {
            Text("Add one")
        }
    }
}


// TODO: find a way to have scrollable lists inside scrollable container
//@Composable
//internal fun ListOfRepeats(
//    repeats: List<Repeat>?,
//    onDoneRepeat: (Repeat) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//    LazyColumn(modifier = modifier
//        .padding(vertical = 4.dp)
////        .verticalScroll(rememberScrollState(), enabled = false)
//    ) {
//        items(
//            repeats.orEmpty(),
//            // TODO: thoroughly test which items are recomposed - can we do btr
//            key = { it.id }
//        ) {
//            repeat -> RepeatComposable(
//                repeat = repeat,
//                onDone = onDoneRepeat,
//                modifier = modifier
//            )
//        }
//    }
//}

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

// <---- Previews ---->

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    MemoriartyTheme(dynamicColor = false){
        OnboardingScreen(onContinueClicked = {})
    }
}

// TODO: maybe move out LayoutPreview Data from Activity code
var previewRepeat = Repeat(
    "long_repeat",
    Date.from(now()),
    "I'm a long description of some chunk of info",
    Date.from(now()),
"some_project_id",
    )
var previewRepeat2 = Repeat(
    "long_repeat2",
    Date.from(now()),
    "I'm a long description of some chunk of info",
    Date.from(now()),
    "some_project_id",
)
//@Preview(
//    showBackground = true,
//    widthDp = 320,
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "Dark"
//)
//@Composable
//fun TodayRepeatsPreviewDark() {
//    MemoriartyTheme(darkTheme = true, dynamicColor = false) {
//        ListOfRepeats(
//            listOf(previewRepeat, previewRepeat2),
//            { repeat: Repeat  -> {}}
//        )
//    }
//}
//
//@Preview(showBackground = true, widthDp = 320)
//@Composable
//fun TodayRepeatsPreviewLight() {
//    MemoriartyTheme(darkTheme = false, dynamicColor = true) {
//        ListOfRepeats(
//            listOf(previewRepeat),
//            { repeat: Repeat  -> {}}
//        )
//    }
//}

//@Preview(showBackground = true, widthDp = 320, heightDp = 320)
//@Composable
// TODO: can I change shouldShowOnboarding var for testing?
// TODO: replace with testable DAO and ViewModels
//fun AppSmallScreenPreview() {
//    MemoriartyTheme(dynamicColor = false){
//        AppContainer()
//    }
//}
