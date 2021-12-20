package com.gamesofni.memoriarty

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gamesofni.memoriarty.overview.RepeatsGridAdapter
import com.gamesofni.memoriarty.network.RepeatItem
import com.gamesofni.memoriarty.overview.MemoriartyApiStatus


//@BindingAdapter("imageUrl")
//fun bindImage(imgView: ImageView, imgUrl: String?) {
//    imgUrl?.let {
//        // Load the image in the background using Coil.
//        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
//        imgView.load(imgUri){
//            placeholder(R.drawable.loading_animation)
//            error(R.drawable.ic_broken_image)
//        }
//
//    }
//}

@BindingAdapter("listData")
fun bindRecyclerView(
        recyclerView: RecyclerView,
        data: List<RepeatItem>?) {

    val adapter = recyclerView.adapter as RepeatsGridAdapter
    adapter.submitList(data)

}


// setting up a custom view to show the status of fetch
@BindingAdapter("marsApiStatus")
fun bindStatus(statusImageView: ImageView,
               status: MemoriartyApiStatus?) {
    when (status) {
        MemoriartyApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        MemoriartyApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        MemoriartyApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }

}
