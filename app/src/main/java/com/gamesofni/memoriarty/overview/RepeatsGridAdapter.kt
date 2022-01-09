package com.gamesofni.memoriarty.overview

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gamesofni.memoriarty.databinding.TodayGridViewItemBinding
import com.gamesofni.memoriarty.network.RepeatItem

class RepeatsGridAdapter(val clickListener: RepeatListener) :
    ListAdapter<RepeatItem, RepeatsViewHolder>(RepeatItemDiffCallback()) {

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RepeatsViewHolder {
        // keep inflation in the ViewHolder
        return RepeatsViewHolder.from(parent)
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RepeatsViewHolder, position: Int) {
        val repeat = getItem(position)
        holder.bind(repeat, clickListener)

        // All of this deals w ViewHolder and should be inside bind fun
        // in fact, even in the BindingAdapters
        // 1. playing with recycling views: if not resetted, some views will be red erroneously
//        if (repeat.description.length <= 20) {
//            holder.binding.description.setTextColor(Color.RED)
//            holder.itemView.setBackgroundColor(Color.RED)
//        } else {
//            holder.binding.description.setTextColor(Color.BLACK)
//            holder.itemView.setBackgroundColor(Color.WHITE)
//        }
        // 2. playing w formatting
//        val res = holder.itemView.context.resources
//        holder.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)


    }
}


/**
 * The RepeatsViewHolder constructor takes the binding variable from the associated
 * GridViewItem, which nicely gives it access to the full [RepeatItem] information.
 */
class RepeatsViewHolder private constructor(private var binding: TodayGridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(repeat: RepeatItem, clickListener: RepeatListener) {
        binding.repeat = repeat
        binding.clickListener = clickListener
        // This is important, because it forces the data binding to execute immediately,
        // which allows the RecyclerView to make the correct view size measurements
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RepeatsViewHolder {
            return RepeatsViewHolder(
                TodayGridViewItemBinding
                    .inflate(LayoutInflater.from(parent.context))
            )
        }
    }
}

/**
 * Allows the RecyclerView to determine which items have changed when the [List] of
 * [RepeatItem] has been updated.
 */
class RepeatItemDiffCallback : DiffUtil.ItemCallback<RepeatItem>() {
    override fun areItemsTheSame(oldItem: RepeatItem, newItem: RepeatItem): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: RepeatItem, newItem: RepeatItem): Boolean {
        // for data class RepeatItem this will check all the fields
        return oldItem == newItem
    }
}


class RepeatListener(val clickListener: (description: String) -> Unit) {
    fun onClick(repeat: RepeatItem) = clickListener(repeat.description)
}


