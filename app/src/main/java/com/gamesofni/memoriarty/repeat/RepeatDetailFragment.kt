package com.gamesofni.memoriarty.repeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.gamesofni.memoriarty.R


class RepeatDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = RepeatDetailFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "Description: ${args.description}", Toast.LENGTH_LONG).show()
        return inflater.inflate(R.layout.fragment_repeat_detail, container, false)
    }

}