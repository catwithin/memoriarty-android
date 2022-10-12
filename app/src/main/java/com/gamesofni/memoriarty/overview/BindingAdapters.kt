package com.gamesofni.memoriarty

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.gamesofni.memoriarty.overview.MemoriartyApiStatus
import com.gamesofni.memoriarty.repeat.Repeat


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

//@BindingAdapter("listData")
//fun bindRecyclerView(
//        recyclerView: RecyclerView,
//        data: List<RepeatItem>?) {
//
//    val adapter = recyclerView.adapter as RepeatsGridAdapter
//    adapter.submitList(data)
//
//}


// setting up a custom view to show the status of fetch
@BindingAdapter("memoriartyApiStatus")
fun memoriartyApiStatus(statusImageView: ImageView,
               status: MemoriartyApiStatus) {
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
        else -> {
            statusImageView.visibility = View.VISIBLE
            // TODO: show different error
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
    }
}

// extension functions
@BindingAdapter("setProjectTextView")
fun TextView.setProject(repeat: Repeat) {
//    text = repeat.projectId[0].uppercase().plus(repeat.projectId[1].uppercase())
    val len = repeat.project.length
    text = repeat.project.subSequence(len-2,len)
}
