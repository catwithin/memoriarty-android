package com.gamesofni.memoriarty.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gamesofni.memoriarty.databinding.TodayGridViewItemBinding
import com.gamesofni.memoriarty.network.RepeatItem

class RepeatsGridAdapter :
    ListAdapter<RepeatItem, RepeatsGridAdapter.RepeatsViewHolder>(DiffCallback) {

    /**
     * The RepeatsViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full [RepeatItem] information.
     */
    class RepeatsViewHolder(
        private var binding: TodayGridViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(repeat: RepeatItem) {
            binding.repeat = repeat
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * [RepeatItem] has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<RepeatItem>() {
        override fun areItemsTheSame(oldItem: RepeatItem, newItem: RepeatItem): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: RepeatItem, newItem: RepeatItem): Boolean {
            return oldItem.description == newItem.description
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepeatsViewHolder {

        return RepeatsViewHolder(TodayGridViewItemBinding
            .inflate(LayoutInflater.from(parent.context))
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RepeatsViewHolder, position: Int) {
        val repeat = getItem(position)
        holder.bind(repeat)
    }
}
