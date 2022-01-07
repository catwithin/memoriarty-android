package com.gamesofni.memoriarty.repeat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.databinding.FragmentRepeatDetailBinding
import com.gamesofni.memoriarty.databinding.FragmentRepeatEditBinding
import kotlinx.coroutines.launch

class RepeatEditFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args = RepeatEditFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application
        val repeatsDao = MemoriartyDatabase.getInstance(application).repeatsDao
        val viewModelFactory = RepeatViewModelFactory(args.description, repeatsDao, application)
        val viewModel = ViewModelProvider(this, viewModelFactory)[RepeatViewModel::class.java]

        val binding = FragmentRepeatEditBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToDetailRepeat.observe(this.viewLifecycleOwner, { r ->
            r?.let {
                this.findNavController().navigate(RepeatEditFragmentDirections
                    .actionRepeatEditFragmentToRepeatDetailFragment(r.description))
                viewModel.doneNavigatingToDetail()
            }
        })

        return binding.root
    }

}