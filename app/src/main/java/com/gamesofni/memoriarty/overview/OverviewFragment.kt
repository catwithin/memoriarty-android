package com.gamesofni.memoriarty.overview

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import com.gamesofni.memoriarty.R
import com.gamesofni.memoriarty.database.MemoriartyDatabase
import com.gamesofni.memoriarty.databinding.TodayFragmentOverviewBinding

class OverviewFragment : Fragment() {

//    private val viewModel: OverviewViewModel by viewModels()

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = TodayFragmentOverviewBinding.inflate(inflater)



        val application = requireNotNull(this.activity).application
        val repeatsDao = MemoriartyDatabase.getInstance(application).repeatsDao
        val viewModelFactory = OverviewViewModelFactory(repeatsDao, application)
        val viewModel = ViewModelProvider(
            this, viewModelFactory).get(OverviewViewModel::class.java)



        val adapter = RepeatsGridAdapter(RepeatListener { description ->
//            Toast.makeText(context, description, Toast.LENGTH_LONG).show()
            viewModel.onRepeatClicked(description)
        })
        binding.photosGrid.adapter = adapter

        viewModel.todayRepeats.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it, viewModel.overdueRepeats.value)
            }
        })

        viewModel.overdueRepeats.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(viewModel.todayRepeats.value, it)
            }
        })


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
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)


        val manager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
//        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int) =  when (position) {
//                0 -> 2
//                else -> 1
//            }
//        }
        binding.photosGrid.layoutManager = manager

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Observer for the network error.
        viewModel.networkError.observe(viewLifecycleOwner, Observer<Boolean> { isError ->
            if (isError) {
                Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            }

        })

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
