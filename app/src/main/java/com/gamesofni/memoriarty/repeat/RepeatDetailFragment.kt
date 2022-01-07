package com.gamesofni.memoriarty.repeat

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gamesofni.memoriarty.R
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.databinding.FragmentRepeatDetailBinding


class RepeatDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args = RepeatDetailFragmentArgs.fromBundle(requireArguments())
        Toast.makeText(context, "Description: ${args.description}", Toast.LENGTH_LONG).show()

        setHasOptionsMenu(true)


        val application = requireNotNull(this.activity).application
        val repeatsDao = MemoriartyDatabase.getInstance(application).repeatsDao
        val viewModelFactory = RepeatViewModelFactory(args.description, repeatsDao, application)
        val viewModel = ViewModelProvider(
            this, viewModelFactory).get(RepeatViewModel::class.java)

        val binding = FragmentRepeatDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToEditRepeat.observe(this.viewLifecycleOwner, { r ->
            r?.let {
                this.findNavController().navigate(RepeatDetailFragmentDirections
                    .actionRepeatDetailFragmentToRepeatEditFragment(r.description))
                viewModel.doneNavigatingToEdit()
            }
        })

        return binding.root
    }


    private fun getShareIntent() : Intent {
        val args = RepeatDetailFragmentArgs.fromBundle(requireArguments())
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_memoriarty_repeat, args
                .description))
        return shareIntent
    }

    private fun shareSuccess() {
        startActivity(getShareIntent())
    }

    // Showing the Share Menu Item Dynamically
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.share_menu, menu)
        if(getShareIntent().resolveActivity(requireActivity().packageManager)==null){
            menu.findItem(R.id.share).isVisible = false
        }
    }

    // Sharing from the Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.share -> shareSuccess()
        }
        return super.onOptionsItemSelected(item)
    }
}
