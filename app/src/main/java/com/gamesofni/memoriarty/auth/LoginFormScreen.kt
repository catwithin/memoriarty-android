package com.gamesofni.memoriarty.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamesofni.memoriarty.ui.MemoriartyTypography


// TODO: move all sizes to dimen, modify for smaller screens
// TODO: can pull up column modifier?

@Composable
fun LoginFormScreen(
    loginViewModel: AuthorisationViewModel,
    onForgotPassword: () -> Unit,
    onLoginSuccessNavigate: () -> Unit,
    onSwitchToSignup: () -> Unit,
    modifier: Modifier,
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
            LoginFormFields(onForgotPassword, onLoginSuccessNavigate, loginViewModel)

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
fun SignUpFormScreen(
    onSubmitSignup: () -> Unit,
    onSwitchToLogin: () -> Unit,
    modifier: Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
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
            SignUpFormFields(onSubmitSignup)

            Spacer(Modifier.weight(2f))
            SwitchBetweenSignupLogin(
                onSwitchToLogin,
                "Already have an account?",
                "Login now",
            )

            Spacer(Modifier.weight(1f))
        }
    }
}


@Composable
private fun LoginFormFields(
    onForgotPassword: () -> Unit,
    onLoginSuccessNavigate: () -> Unit,
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
        SubmitFormButton({loginViewModel.submitLogin(onLoginSuccessNavigate)},"Login")
        ForgotPasswordLink(onForgotPassword)
    }
}

@Composable
private fun SignUpFormFields(onSubmitSignUp: () -> Unit) {
    val email = remember { mutableStateOf(TextFieldValue()) }
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmailField(email)
        UsernameField(username)
        PasswordField(password)
        SubmitFormButton(onSubmitSignUp, "Register")
    }
}


// TODO: find TextFields with validation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailField(email: MutableState<TextFieldValue>) {
    TextField(
        label = { Text(text = "Email") },
        value = email.value,
        onValueChange = { email.value = it },
        modifier = Modifier.padding(bottom = 16.dp),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun UsernameField(username:String, onChange: (String) -> Unit) {
    TextField(
        label = { Text(text = "Username") },
        value = username,
        onValueChange = { onChange(it) },
        modifier = Modifier.padding(bottom = 16.dp),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PasswordField(password: String, onChange: (String) -> Unit) {
    TextField(
        label = { Text(text = "Password") },
        value = password,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = { onChange(it) },
    )
}


@Composable
internal fun SubmitFormButton(onSubmit: () -> Unit, buttonText: String) {
    Button(
        onClick = onSubmit,
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .padding(24.dp, 18.dp, 24.dp, 0.dp)
            .height(50.dp)
            .fillMaxWidth()
    ) {
        Text(text = buttonText, style = MemoriartyTypography.headlineSmall)
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


@Composable
internal fun SwitchBetweenSignupLogin(
    onSwitchBetween: () -> Unit,
    promptText: String,
    linkText: String,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = promptText,
            style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.Cursive),
        )

        ClickableText(
            text = AnnotatedString(linkText),
            onClick = { _ -> onSwitchBetween() },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
            )
        )
    }
}


@Composable
internal fun MemoriartyTitle() {
    Text(
        text = "Memoriarty",
        style = MemoriartyTypography.titleLarge,
        modifier = Modifier
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
