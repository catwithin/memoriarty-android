package com.gamesofni.memoriarty.overview

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gamesofni.memoriarty.*
import com.gamesofni.memoriarty.R
import com.gamesofni.memoriarty.auth.*
import com.gamesofni.memoriarty.overview.MemoriartyApiStatus.*
import com.gamesofni.memoriarty.repeat.Repeat
import com.gamesofni.memoriarty.repeat.RepeatDetailScreen
import com.gamesofni.memoriarty.ui.MemoriartyTheme
import timber.log.Timber
import java.time.Instant.now
import java.util.*


class MainComposeAktivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this).application

        val overviewModelFactory = OverviewViewModelFactory(application,
            DataStoreRepository(dataStore)
        )
        val loginViewModelFactory = AuthorisationViewModelFactory(application,
            DataStoreRepository(dataStore)
        )

        setContent {
            MemoriartyTheme(dynamicColor = true) {
                AppContainer(
                    overviewModelFactory = overviewModelFactory,
                    loginViewModelFactory = loginViewModelFactory,
                    application = application,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun AppContainer(
    modifier: Modifier = Modifier,
    overviewModelFactory: OverviewViewModelFactory,
    overviewViewModel: OverviewViewModel = viewModel(factory = overviewModelFactory),
    loginViewModelFactory: AuthorisationViewModelFactory,
    loginViewModel: AuthorisationViewModel = viewModel(factory = loginViewModelFactory),
    application: Application,
) {
//    viewModelFactory {}
    // TODO: save state of the shouldShowOnboarding into user's settings
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(false) }

    val navController = rememberNavController()
    val authorised = loginViewModel.status.observeAsState()

    BackgroundImg()

    NavHost(
        navController = navController,
        startDestination = Loading.route,
        modifier = modifier,
    ) {
        composable(route = Loading.route) {
            WelcomeScreen(modifier)
        }

        composable(route = Overview.route) {
            Timber.d("recomposed Overview")
            OverviewScreen(
                // TODO: state hoist overviewModel
                overviewViewModel,
                authorised,
                onDetailClicked = { repeatId ->
                    navController.navigate("${RepeatDetail.route}/$repeatId")
                },
                navigateToLogin =  {
                    Timber.d("navigated to Login)")
                    navController.navigateSingleTop(Login.route)
                },
                onLogout = { loginViewModel.submitLogout {
                    navController.navigate(Login.route) }
                },
                context = application.applicationContext,
                modifier,
            )
        }
        composable(
            route = RepeatDetail.routeWithArgs,
            arguments = RepeatDetail.arguments,
        ) { navBackStackEntry ->
            val repeatId = navBackStackEntry.arguments?.getString(RepeatDetail.repeatIdArg)?: ""

            RepeatDetailScreen(repeatId, modifier)
        }
        composable(route = Login.route) {
            Timber.d("recomposed LoginScreen)")

            LoginScreen(
                loginViewModel = loginViewModel,
                onForgotPassword = { navController.navigate(ForgotPassword.route) },
                onLoginSuccessNavigate = { navController.navigatePopping(Overview.route) },
                onSwitchToSignup = { navController.navigate(SignUp.route) },
                context = application.applicationContext,
                modifier,
            )
        }
        composable(route = SignUp.route) {
            SignUpScreen(
//                onSubmitSignup = { navController.navigate(Overview.route) },
                email = loginViewModel.email,
                username = loginViewModel.username,
                password = loginViewModel.password,
                status = loginViewModel.status,
                setEmail =  { s: String -> loginViewModel.setEmail(s) },
                setUsername =  { s: String -> loginViewModel.setUsername(s) },
                setPassword =  { s: String -> loginViewModel.setPassword(s) },
                onSwitchToLogin =  { navController.navigate(Login.route) },
                onSubmitSignup =  { loginViewModel.submitSignup() },
                modifier = modifier,
            )
        }
        composable(route = ForgotPassword.route) {
            PasswordResetScreen(
                email = loginViewModel.email,
                onSubmitPasswordReset = { navController.navigate(Overview.route) },
                onSwitchToLogin =  { navController.navigate(Login.route) },
                onEmailChange =  { s: String -> loginViewModel.setEmail(s) },
                modifier = modifier,
            )
        }
        composable(route = Onboarding.route) {
            OnboardingScreen(
                onContinueClicked = { shouldShowOnboarding = false },
                modifier,
            )
        }
    }

}

@Composable
private fun BackgroundImg() {
    Image(
        painter = painterResource(R.drawable.celebration),
        contentDescription = null,
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    )
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun OverviewScreen(
    overviewViewModel: OverviewViewModel,
    authorised: State<MemoriartyLoginStatus?>,
    onDetailClicked: (String) -> Unit,
    navigateToLogin: () -> Unit,
    onLogout: (() -> Unit) -> Unit,
    context: Context,
    modifier: Modifier,
) {
    val todayRepeats by overviewViewModel.todayRepeats.observeAsState()
    val overdueRepeats by overviewViewModel.overdueRepeats.observeAsState()
    val state by overviewViewModel.status.observeAsState()

    Timber.d("state: $state, overviewModel.status: ${overviewViewModel.status.value}")

    when (state) {
        LOADING -> {
            //        var atEnd by remember { mutableStateOf(false) }
            val image = AnimatedImageVector.animatedVectorResource(R.drawable.loading_animation)
            Image(
                painter = rememberAnimatedVectorPainter(image, true),
                contentDescription = "Loading...",
                modifier = modifier
                    .size(248.dp)
                    .fillMaxSize()
                //                .clickable { atEnd = !atEnd }
            )
        }

        UNAUTHORISED -> {
            if (authorised.value == MemoriartyLoginStatus.LOGGED_IN) {
                overviewViewModel.refreshDataFromRepository()
            } else {
                LaunchedEffect(true) {
                    Toast.makeText(context,
                        "Unauthorised, please login", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                }
            }
        }

        NETWORK_ERROR -> {
            Text("Network error",
                Modifier.background(Color.Yellow)
            )

            LaunchedEffect(true) {
                Toast.makeText(context,
                    "Network Error", Toast.LENGTH_SHORT).show()
            }
        }

        DONE -> {
            ListOfRepeats(
                todayRepeats,
                overdueRepeats,
                onDoneRepeat = { repeat -> overviewViewModel.markAsDone(repeat) },
                onOpenDetailedView = { repeat -> onDetailClicked(repeat.id) },
                onLogout = onLogout,
                modifier = modifier
            )
        }
        null -> {}
    }
}

// TODO: find a way to have scrollable lists inside scrollable container
// https://youtu.be/1ANt65eoNhQ?t=896
@Composable
private fun ListOfRepeats(
    todayRepeats: List<Repeat>?,
    overdueRepeats: List<Repeat>?,
    onDoneRepeat: (Repeat) -> Unit,
    onOpenDetailedView: (Repeat) -> Unit,
    onLogout: (() -> Unit) -> Unit,
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
            key = { it.id }
        ) { repeat -> RepeatComposable(
              repeat = repeat,
              onDone = onDoneRepeat,
              onOpenDetailedView = onOpenDetailedView,
        )}

        item(){ SectionName(
            sectionName = "Old Repeats to Do:",
        )}

        items(
            overdueRepeats.orEmpty(),
            key = { it.id }
        ) { repeat -> RepeatComposable(
              repeat = repeat,
              onDone = onDoneRepeat,
              onOpenDetailedView = onOpenDetailedView,
        )}

        item(){ AddChunk(onLogout, modifier)}
    }
}

@Composable
fun SectionName(sectionName: String) {
    Text(sectionName)
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier,
) {
    // TODO: implement a real onboarding screen, with the kitten
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
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
fun AddChunk(onLogout: (() -> Unit) -> Unit, modifier: Modifier = Modifier) {
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
            enabled = count < 2
        ) {
            Text("Add one")
        }

        Button(
            onClick = { onLogout {} },
        ) {
            Text("Logout")
        }
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


fun NavHostController.navigateSingleTop(route: String) =
    this.navigate(route) { launchSingleTop = true }


fun NavHostController.navigatePopping(route: String) =
    this.navigate(route) {
        launchSingleTop = true
        popUpTo(route)
    }


// <---- Previews ---->


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
@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Composable
fun TodayRepeatsPreviewDark() {
    MemoriartyTheme(darkTheme = true, dynamicColor = false) {
        ListOfRepeats(
            listOf(previewRepeat, previewRepeat2),
            listOf(),
            { repeat: Repeat  -> {}},
            { repeat: Repeat  -> {}},
            { },
            Modifier
        )
    }
}
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
