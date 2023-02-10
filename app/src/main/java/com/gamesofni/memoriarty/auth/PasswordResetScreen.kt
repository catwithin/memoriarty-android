package com.gamesofni.memoriarty.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp

@Composable
fun PasswordResetScreen(
    onSubmitPasswordReset: () -> Unit,
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
            SubmitFormButton(onSubmitPasswordReset, "Send link")

            Spacer(Modifier.weight(2f))
            SwitchBetweenSignupLogin(
                onSwitchToLogin,
                "",
                "Back to Login",
            )

            Spacer(Modifier.weight(1f))
        }
    }
}
