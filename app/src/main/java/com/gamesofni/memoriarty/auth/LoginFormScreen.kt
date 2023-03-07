package com.gamesofni.memoriarty.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import timber.log.Timber


// TODO: move all sizes to dimen, modify for smaller screens
// TODO: can pull up column modifier?

@Composable
fun LoginScreen(
    loginViewModel: AuthorisationViewModel,
    onForgotPassword: () -> Unit,
    onLoginSuccessNavigate: () -> Unit,
    onSwitchToSignup: () -> Unit,
    context: Context,
    modifier: Modifier,
) {
    val state by loginViewModel.status.observeAsState()

    when {
        state == MemoriartyLoginStatus.LOADING -> { LoadingAnimation(modifier) }
        state == MemoriartyLoginStatus.WRONG_LOGIN -> {
            LaunchedEffect(true) {
                Timber.d("launched wrong login toast")
                Toast.makeText(context,
                    "Wrong credentials, please try again", Toast.LENGTH_SHORT).show()
                loginViewModel.clearStatus()
            }
        }
        state == MemoriartyLoginStatus.NETWORK_ERROR -> {
            LaunchedEffect(true) {
                Timber.d("launched netw err toast")
                Toast.makeText(context,
                    "Network Error", Toast.LENGTH_SHORT).show()
                loginViewModel.clearStatus()
            }
        }
        state == MemoriartyLoginStatus.LOGGED_OUT -> {
            LoginForm(
                modifier,
                onForgotPassword,
                loginViewModel,
                onSwitchToSignup
            )
        }
        state == MemoriartyLoginStatus.LOGGED_IN -> {
            Timber.d("success toast")

            LaunchedEffect(true) {
                Toast.makeText(context,
                    "Success! Hello, ${loginViewModel.username.value}!",
                    Toast.LENGTH_SHORT).show()
                onLoginSuccessNavigate()
            }
        }
    }
}

@Composable
private fun LoginForm(
    modifier: Modifier,
    onForgotPassword: () -> Unit,
    loginViewModel: AuthorisationViewModel,
    onSwitchToSignup: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.85f)
//            .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.weight(2f))
            MemoriartyTitle()

            Spacer(Modifier.weight(1f))
            LoginFormFields(onForgotPassword, loginViewModel)

            Spacer(Modifier.weight(2f))
            SwitchBetweenSignupLogin(
                onSwitchToSignup,
                "New to Memoriarty?",
                "Create account",
            )

            Spacer(Modifier.weight(1f))
        }
    }
}


@Composable
private fun LoginFormFields(
    onForgotPassword: () -> Unit,
    loginViewModel: AuthorisationViewModel,
) {
    val username = loginViewModel.username.observeAsState()
    val password = loginViewModel.password.observeAsState()

    Column(
        modifier = Modifier.padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UsernameField(username.value?:"") { loginViewModel.setUsername(it) }
        PasswordField(password.value?:"") { loginViewModel.setPassword(it) }
        SubmitFormButton({loginViewModel.submitLogin()},"Login")
        ForgotPasswordLink(onForgotPassword)
    }
}


@Composable
private fun ForgotPasswordLink(onForgotPassword: () -> Unit) {
    ClickableText(
        text = AnnotatedString("Forgot your password?"),
        onClick = { _ -> onForgotPassword() },
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = FontFamily.Default,
            textDecoration = TextDecoration.Underline,
        ),
        modifier = Modifier
            .padding(top = 12.dp)
    )
}


// TODO: find out why preview doesn't work - bc of the rememberNavController fun call?
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun LoginFormScreenPreview() {
//    MemoriartyTheme(dynamicColor = true){
//        Surface(Modifier) {
//            LoginFormScreen(
//                rememberNavController(),
//                { navController.navigate(Routes.Overview.route) },
//                Modifier
//            )
//        }
//    }
//}
