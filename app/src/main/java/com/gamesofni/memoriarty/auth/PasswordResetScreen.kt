package com.gamesofni.memoriarty.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData

@Composable
fun PasswordResetScreen(
    email: MutableLiveData<String>,
    onEmailChange: (String) -> Unit,
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

            val eml by email.observeAsState()

            Spacer(Modifier.weight(2f))
            MemoriartyTitle()

            Spacer(Modifier.weight(1f))
            Text(
                text = "Send the password reset link",
                style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.Cursive),
            )

            Spacer(Modifier.weight(1f))
            EmailField(eml?:"") {onEmailChange(it)}
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
