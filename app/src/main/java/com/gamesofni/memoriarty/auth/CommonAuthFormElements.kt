package com.gamesofni.memoriarty.auth

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gamesofni.memoriarty.R
import com.gamesofni.memoriarty.ui.MemoriartyTypography

@Composable
@OptIn(ExperimentalAnimationGraphicsApi::class)
internal fun LoadingAnimation(modifier: Modifier) {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.loading_animation)
    Image(
        painter = rememberAnimatedVectorPainter(image, true),
        contentDescription = "Loading...",
        modifier = modifier
            .size(248.dp)
            .fillMaxSize()
    )
}

// TODO: find TextFields with validation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EmailField(email: String, onChange: (String) -> Unit) {
    TextField(
        label = { Text(text = "Email") },
        value = email,
        onValueChange = { onChange(it) },
        modifier = Modifier.padding(bottom = 16.dp),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun UsernameField(username:String, onChange: (String) -> Unit) {
    TextField(
        label = { Text(text = "Username") },
        value = username,
        onValueChange = { onChange(it) },
        modifier = Modifier.padding(bottom = 16.dp),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun PasswordField(password: String, onChange: (String) -> Unit) {
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
