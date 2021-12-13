package com.gamesofni.memoriarty.overview

import android.os.Bundle
import android.text.Layout
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.gamesofni.memoriarty.R
import com.gamesofni.memoriarty.databinding.TodayFragmentOverviewBinding
import com.gamesofni.memoriarty.repeat.RepeatDetailFragment

class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by viewModels()

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TodayFragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        binding.photosGrid.adapter = RepeatsGridAdapter( RepeatListener { description ->
//            Toast.makeText(context, description, Toast.LENGTH_LONG).show()
            viewModel.onRepeatClicked(description)
        } )


        viewModel.navigateToRepeatDetail.observe(viewLifecycleOwner, { description ->
            description?.let {
                // just starting without args:
//                this.findNavController().navigate(R.id.action_overviewFragment_to_repeatDetailFragment)
                // passing args with safe args plugin (it generates OverviewFragmentDirections
                // class)
                this.findNavController().navigate(OverviewFragmentDirections
                    .actionOverviewFragmentToRepeatDetailFragment(description))
                viewModel.onRepeatDetailNavigated()
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
            onNavDestinationSelected(item,requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }
}
