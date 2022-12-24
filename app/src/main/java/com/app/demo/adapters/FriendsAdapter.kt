package com.app.demo.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.demo.R
import com.app.demo.databinding.ItemListBinding
import com.app.demo.interfaces.OnItemClickListener
import com.app.demo.model.Friends

private const val TAG = "FriendsAdapter"

class FriendsAdapter : ListAdapter<Friends, FriendsAdapter.FriendViewHolder>(DATA_COMPARATOR) {


    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding: ItemListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list,
            parent,
            false
        )
        return FriendViewHolder(binding, mListener!!)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val fileItem: Friends = getItem(position)
        holder.bind(fileItem)
    }

    class FriendViewHolder(private val binding: ItemListBinding, mListener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivDelete.setOnClickListener {
                Log.i(TAG, "delete clicked: ")
                try {
                    mListener.onItemClick(adapterPosition)
                } catch (_: Exception) {
                }
            }
        }

        fun bind(fileItem: Friends) {
            binding.apply {
                friendName.text = fileItem.name
                friendAge.text = fileItem.age.toString()
            }
        }

    }


    companion object {
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<Friends>() {
            override fun areItemsTheSame(oldItem: Friends, newItem: Friends): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Friends, newItem: Friends): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

