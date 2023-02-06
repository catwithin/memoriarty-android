package com.gamesofni.memoriarty

sealed class Routes(val route: String) {
    object SignUp : Routes("SignUp")
    object ForgotPassword : Routes("ForgotPassword")
    object Login : Routes("Login")
    object Overview : Routes("Overview")
}
