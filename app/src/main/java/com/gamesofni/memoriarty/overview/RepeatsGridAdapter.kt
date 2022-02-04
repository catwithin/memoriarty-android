package com.gamesofni.memoriarty.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gamesofni.memoriarty.R
import com.gamesofni.memoriarty.databinding.TodayGridViewItemBinding
import com.gamesofni.memoriarty.repeat.Repeat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER_TODAY = 0
private const val ITEM_VIEW_TYPE_HEADER_OVERDUE = 1
private const val ITEM_VIEW_TYPE_ITEM = 2

class RepeatsGridAdapter(val clickListener: RepeatListener) :
    ListAdapter<OverviewListItem, RecyclerView.ViewHolder>(OverviewListItemDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        // keep inflation in the ViewHolder
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER_TODAY -> HeaderViewHolder.from(parent, ITEM_VIEW_TYPE_HEADER_TODAY)
            ITEM_VIEW_TYPE_HEADER_OVERDUE -> HeaderViewHolder.from(parent, ITEM_VIEW_TYPE_HEADER_OVERDUE)
            ITEM_VIEW_TYPE_ITEM -> RepeatsViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RepeatsViewHolder)  {
            val repeatListItem = getItem(position) as OverviewListItem.RepeatListItem
            holder.bind(repeatListItem.repeat, clickListener)
        }

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

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is OverviewListItem.Header -> getItem(position).id.toInt()
            is OverviewListItem.RepeatListItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    fun addHeaderAndSubmitList(todayRepeats: List<Repeat>?, overdueRepeats: List<Repeat>?) {
        adapterScope.launch {
            val items = listOf(OverviewListItem.Header(ITEM_VIEW_TYPE_HEADER_TODAY.toString())) +
                when (todayRepeats) {
                    null -> listOf()
                    else -> todayRepeats.map {OverviewListItem
                     .RepeatListItem(it)}
                } + listOf(OverviewListItem.Header(ITEM_VIEW_TYPE_HEADER_OVERDUE.toString())) +
                when (overdueRepeats) {
                    null -> listOf()
                    else -> overdueRepeats.map {OverviewListItem
                        .RepeatListItem(it)}
                }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }
}


/**
 * The RepeatsViewHolder constructor takes the binding variable from the associated
 * GridViewItem, which nicely gives it access to the full [Repeat] information.
 */
class RepeatsViewHolder private constructor(private var binding: TodayGridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(repeat: Repeat, clickListener: RepeatListener) {
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

class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup, headerType: Int): HeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.today_grid_view_header, parent, false)
            val textView: TextView = view.findViewById(R.id.header_title) as TextView
            textView.text = when (headerType) {
                ITEM_VIEW_TYPE_HEADER_OVERDUE -> parent.resources.getString(R.string
                    .header_overdue_repeats)
                ITEM_VIEW_TYPE_HEADER_TODAY -> parent.resources.getString(R.string
                    .header_today_repeats)
                else -> ""
            }
            return HeaderViewHolder(view)
        }
    }
}

/**
 * Allows the RecyclerView to determine which items have changed when the [List] of
 * [OverviewListItem] has been updated.
 */
class OverviewListItemDiffCallback : DiffUtil.ItemCallback<OverviewListItem>() {
    override fun areItemsTheSame(oldItem: OverviewListItem, newItem: OverviewListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: OverviewListItem, newItem: OverviewListItem): Boolean {
        // for data class RepeatItem this will check all the fields
        return oldItem == newItem
    }
}


class RepeatListener(val clickListener: (description: String) -> Unit) {
    fun onClick(repeat: Repeat) = clickListener(repeat.description)
}


sealed class OverviewListItem {
    abstract val id: String

    data class RepeatListItem(val repeat: Repeat): OverviewListItem()      {
        override val id = repeat.id
    }

    data class Header(override val id: String): OverviewListItem()
}
