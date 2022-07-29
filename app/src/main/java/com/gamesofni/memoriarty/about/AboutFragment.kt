package com.gamesofni.memoriarty.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview


class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                Surface() {
                    Greeting("Memoriarty")
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Things about $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
        Greeting("Memoriarty")
}
