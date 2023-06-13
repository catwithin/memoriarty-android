package com.gamesofni.memoriarty

import androidx.navigation.NavType
import androidx.navigation.navArgument


interface MemoriartyDestination {
//    val icon: ImageVector
    val route: String
}

object Loading : MemoriartyDestination {
    override val route = "Loading"
}

object Overview : MemoriartyDestination {
    override val route = "Overview"
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
    override val route = "Login"
}

object SignUp : MemoriartyDestination {
    override val route = "SignUp"
}

object ForgotPassword : MemoriartyDestination {
    override val route = "ForgotPassword"
}

object Onboarding : MemoriartyDestination {
    override val route = "onboarding"
}
