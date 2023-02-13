package com.gamesofni.memoriarty

import androidx.navigation.NavType
import androidx.navigation.navArgument


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

object RepeatDetail : MemoriartyDestination {
    override val route = "detailed_repeat"
    const val repeatIdArg = "repeat_id"
    val routeWithArgs = "${route}/{${repeatIdArg}}"
    val arguments = listOf(
        navArgument(repeatIdArg) { type = NavType.StringType }
    )
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
