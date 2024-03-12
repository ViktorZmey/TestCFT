package com.example.testcft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testcft.R
import com.example.testcft.databinding.ListItemDetailsBinding


class UserDetailAdapter(
    private val context: Context,
    private val listener: Listener? = null
) : ListAdapter<UserDetailsItem, UserDetailAdapter.HolderDetails>(ComparatorDetails()){

    class HolderDetails (
        private val view: View,
        private val context: Context
    ) : RecyclerView.ViewHolder(view){

        fun bind(item: UserDetailsItem, listener: View.OnClickListener?) {
            val title = getTitle(item.type)
            val subtitle = getSubtitle(item.type)

            when(item.type) {
                is UserDetailsItem.ContentType.Photo -> {
                    // val binding = ListItemDetailsPhotoBinding.bind(view)
                    // binding.title.text = title
                    // Picasso.get().load(item.type.url)... set
                }
                else -> {
                    val binding = ListItemDetailsBinding.bind(view)
                    binding.title.text = title
                    binding.subtitle.text = subtitle
                }
            }
        }

        private fun getTitle(type: UserDetailsItem.ContentType) : String = when(type){
            is UserDetailsItem.ContentType.FirstName -> "First Name"
            is UserDetailsItem.ContentType.LastName -> "Last Name"
            else -> ""
        }

        private fun getSubtitle(type: UserDetailsItem.ContentType) : String = when(type){
            is UserDetailsItem.ContentType.FirstName -> type.firstName
            is UserDetailsItem.ContentType.LastName -> type.lastName
            else -> ""
        }
    }

    class ComparatorDetails : DiffUtil.ItemCallback<UserDetailsItem>() {
        override fun areItemsTheSame(oldItem: UserDetailsItem, newItem: UserDetailsItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserDetailsItem, newItem: UserDetailsItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position).type) {
            is UserDetailsItem.ContentType.Photo -> 0
            else -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDetails {
        return when(viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_details, parent, false)
                HolderDetails(view, context)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_details, parent, false)
                HolderDetails(view, context)
            }
            else -> {
                throw Exception("Такого быть не должно!")
            }
        }
    }

    override fun onBindViewHolder(holder: HolderDetails, position: Int) {
        val item = getItem(position)

        holder.bind(item) {
            listener?.onClickItem(item, position)
        }
    }

    fun interface Listener {
        fun onClickItem(item: UserDetailsItem, atPosition: Int)
    }
}




