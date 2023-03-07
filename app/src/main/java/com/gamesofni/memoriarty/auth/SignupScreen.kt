package com.gamesofni.memoriarty.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


@Composable
fun SignUpScreen(
    email: MutableLiveData<String>,
    username: MutableLiveData<String>,
    password: MutableLiveData<String>,
    status: LiveData<MemoriartyLoginStatus>,
    setEmail: (String) -> Unit,
    setUsername: (String) -> Unit,
    setPassword: (String) -> Unit,
    onSubmitSignup: () -> Unit,
    onSwitchToLogin: () -> Unit,
    modifier: Modifier,
) {
    val state by status.observeAsState()

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
            SignUpFormFields(email, username, password, setEmail, setUsername, setPassword,
                onSubmitSignup)

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
private fun SignUpFormFields(
    email: MutableLiveData<String>,
    username: MutableLiveData<String>,
    password: MutableLiveData<String>,
    setEmail: (String) -> Unit,
    setUsername: (String) -> Unit,
    setPassword: (String) -> Unit,
    onSubmitSignUp: () -> Unit
) {
    val eml by email.observeAsState()
    val usrnm by username.observeAsState()
    val pwd by password.observeAsState()
    Column(
        modifier = Modifier.padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmailField(eml ?: "") { setEmail(it) }
        UsernameField(usrnm ?: "") { setUsername(it) }
        PasswordField(pwd ?: "") { setPassword(it) }
        SubmitFormButton(onSubmitSignUp, "Register")
    }
}
