package com.gamesofni.memoriarty.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gamesofni.memoriarty.Routes

@Composable
fun PasswordResetScreen(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.85f)
//            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        val email = remember { mutableStateOf(TextFieldValue()) }

        Spacer(Modifier.weight(2f))

        MemoriartyTitle()

        Spacer(Modifier.weight(1f))

        Text(
            text = "Send the password reset link",
            style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.Cursive),
        )

        Spacer(Modifier.weight(1f))

        EmailField(email)
        SubmitFormButton(navController, "Send link")

        Spacer(Modifier.weight(2f))

        SwitchBetweenSignupLogin(
            navController,
            "",
            "Back to Login",
            Routes.Login.route
        )

        Spacer(Modifier.weight(1f))
    }
}