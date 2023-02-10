package com.gamesofni.memoriarty


sealed class Routes(val route: String) {
    object SignUp : Routes("SignUp")
    object ForgotPassword : Routes("ForgotPassword")
    object Login : Routes("Login")
    object Overview : Routes("Overview")
}

interface MemoriartyDestination {
//    val icon: ImageVector
    val route: String
}

object Overview : MemoriartyDestination {
    override val route = "overview"
}

object Login : MemoriartyDestination {
    override val route = "login"
}

object SignUp : MemoriartyDestination {
    override val route = "signup"
}

object ForgotPassword : MemoriartyDestination {
    override val route = "forgotpassword"
}
