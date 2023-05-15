package com.example.docmanager

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.text.SimpleDateFormat

@BindingAdapter("listData")
fun bindRecyclerViewData(recyclerView: RecyclerView, data: LiveData<List<FileInfo>>?) {
    if (data == null) return
    val adapter = recyclerView.adapter as FilesInfoAdapter
    adapter.submitList(data.value?.toList())
}

@BindingAdapter("imgType")
fun bindImageByFile(imgView: ImageView, file: FileInfo?) {

    if (file == null) return

    if (file.isDirectory) {
        Glide.with(imgView.context)
            .load(R.drawable.ic_folder)
            .apply(RequestOptions()
                .placeholder(R.drawable.ic_placeholder))
            .into(imgView)
        return
    }

    val imgUrl = R.drawable.ic_file
    //TODO parse img by type
    Glide.with(imgView.context)
        .load(imgUrl)
        .apply(RequestOptions()
            .placeholder(R.drawable.ic_placeholder))
        .into(imgView)

}

@SuppressLint("SimpleDateFormat")
@BindingAdapter("updDate")
fun bindDateByFile(textView: TextView, file: FileInfo?) {

    if (file == null) return

    if (file.isDirectory) {
        textView.visibility = View.GONE
        return
    }

    textView.visibility = View.VISIBLE
    textView.text = SimpleDateFormat("dd/MM").format(file.update_date)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("fileSize")
fun bindSizeByFile(textView: TextView, file: FileInfo?) {

    if (file == null) return

    if (file.isDirectory) {
        textView.visibility = View.GONE
        return
    }

    textView.visibility = View.VISIBLE


    val sizeInKb = (file.size / 1024).toFloat()
    val sizeInMb = sizeInKb / 1024
    val sizeInGb = sizeInMb / 1024

    if (sizeInGb >= 1) {
        textView.text = String.format("%.2f Gb", sizeInGb)
        return
    }
    if (sizeInMb >= 1) {
        textView.text = String.format("%.2f Mb", sizeInMb)
        return
    }
    if (sizeInKb >= 1) {
        textView.text = String.format("%.2f Kb", sizeInKb)
        return
    }


    textView.text = "${file.size} byte"
}

@BindingAdapter("fileType")
fun bindTypeByFile(textView: TextView, file: FileInfo?) {

    if(file == null) return

    if (file.isDirectory) {
        textView.visibility = View.GONE
        return
    }

    textView.visibility = View.VISIBLE
    textView.text = file.path.substringAfterLast('.', "")
}