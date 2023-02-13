package com.gamesofni.memoriarty.repeat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RepeatDetailScreen (
    repeatId: String,
    modifier: Modifier,
) {
    Row(modifier.
        background(Color.White),
    ) {
        Text("Just a stub. RepeatId: $repeatId")
    }
}
