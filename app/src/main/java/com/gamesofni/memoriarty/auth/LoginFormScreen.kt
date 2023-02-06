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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gamesofni.memoriarty.Routes
import com.gamesofni.memoriarty.ui.MemoriartyTheme
import com.gamesofni.memoriarty.ui.MemoriartyTypography


// TODO: move all sizes to dimen, modify for smaller screens

@Composable
fun LoginFormScreen(navController: NavHostController) {
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
        LoginFormFields(navController)

        Spacer(Modifier.weight(2f))
        SwitchBetweenSignupLogin(
            navController,
            "New to Memoriarty?",
            "Create account",
            Routes.SignUp.route
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun SignUpFormScreen(navController: NavHostController) {
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
        SignUpFormFields(navController = navController)

        Spacer(Modifier.weight(2f))
        SwitchBetweenSignupLogin(
            navController,
            "Already have an account?",
            "Login now",
            Routes.Login.route
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun LoginFormFields(navController: NavHostController) {
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UsernameField(username)
        PasswordField(password)
        SubmitFormButton(navController, "Login")
        ForgotPasswordLink(navController)
    }
}

@Composable
private fun SignUpFormFields(navController: NavHostController) {
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
        SubmitFormButton(navController, "Register")
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
private fun UsernameField(username: MutableState<TextFieldValue>) {
    TextField(
        label = { Text(text = "Username") },
        value = username.value,
        onValueChange = { username.value = it },
        modifier = Modifier.padding(bottom = 16.dp),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PasswordField(password: MutableState<TextFieldValue>) {
    TextField(
        label = { Text(text = "Password") },
        value = password.value,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = { password.value = it },
    )
}

@Composable
private fun SubmitFormButton(navController: NavHostController, buttonText: String) {
    Button(
        onClick = { navController.navigate(Routes.Overview.route) },
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
private fun ForgotPasswordLink(navController: NavHostController) {
    ClickableText(
        text = AnnotatedString("Forgot password?"),
        onClick = { navController.navigate(Routes.ForgotPassword.route) },
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
private fun SwitchBetweenSignupLogin(
    navController: NavHostController,
    promptText: String,
    linkText: String,
    route: String,
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
            onClick = { navController.navigate(route) },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
            )
        )
    }
}

@Composable
private fun MemoriartyTitle() {
    Text(
        text = "Memoriarty",
        style = MemoriartyTypography.titleLarge,
        modifier = Modifier
    )
}


// TODO: find out why preview doesn't work - bc of the rememberNavController fun call?
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginFormScreenPreview() {
    MemoriartyTheme(dynamicColor = true){
        Surface(Modifier) {
            LoginFormScreen(rememberNavController())
        }
    }
}
