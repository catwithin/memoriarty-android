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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginFormScreen(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(0.8f)
            .fillMaxHeight(0.85f)
            .verticalScroll(rememberScrollState())
    ) {

        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }

        Spacer(Modifier.weight(2f))

        Text(
            text = "Memoriarty",
            style = MemoriartyTypography.titleLarge,
            modifier = Modifier
        )

        Spacer(Modifier.weight(1f))

        Column(
            modifier = Modifier.padding(horizontal = 40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                label = { Text(text = "Username") },
                value = username.value,
                onValueChange = { username.value = it },
                modifier = Modifier.padding(bottom = 16.dp),
            )

            TextField(
                label = { Text(text = "Password") },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it },
            )

            Button(
                onClick = { navController.navigate(Routes.Overview.route) },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .padding(24.dp, 18.dp, 24.dp, 0.dp)
                    .height(50.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Login", style = MemoriartyTypography.headlineSmall)
            }

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

        Spacer(Modifier.weight(2f))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "New to Memoriarty?",
                style = TextStyle(fontSize = 20.sp, fontFamily = FontFamily.Cursive),
            )

            ClickableText(
                text = AnnotatedString("Create account"),
                onClick = { navController.navigate(Routes.SignUp.route) },
                style = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                )
            )
        }

        Spacer(Modifier.weight(1f))
    }
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
