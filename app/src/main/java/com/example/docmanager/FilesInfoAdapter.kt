package com.example.docmanager

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.docmanager.databinding.FileInfoItemBinding

class FilesInfoAdapter(private val onItemClickListener: OnClickListener
) :
    ListAdapter<FileInfo, FilesInfoAdapter.FileInfoViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileInfoViewHolder {
        val binding = FileInfoItemBinding.inflate(LayoutInflater.from(parent.context))
        binding.layout.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

        return FileInfoViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun submitList(list: List<FileInfo>?) {
        this.notifyDataSetChanged()
        super.submitList(list)
    }

    override fun onBindViewHolder(holder: FileInfoViewHolder, position: Int) {
        val fileInfo = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClickListener.onClick(fileInfo)
        }
        holder.bind(fileInfo)
    }

    class OnClickListener(val clickListener: (fileInfo: FileInfo) -> Unit) {
        fun onClick(fileInfo: FileInfo) = clickListener(fileInfo)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FileInfo>() {
        override fun areItemsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FileInfo, newItem: FileInfo): Boolean {
            return oldItem.path == newItem.path
        }
    }

    class FileInfoViewHolder(private var binding: FileInfoItemBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fileInfo: FileInfo) {
            binding.fileInfo = fileInfo
            binding.executePendingBindings()
        }
    }
}