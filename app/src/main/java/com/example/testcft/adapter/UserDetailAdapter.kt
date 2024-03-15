package com.example.testcft.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testcft.R
import com.example.testcft.databinding.ItemPhotoBinding
import com.example.testcft.databinding.ListItemDetailsBinding
import com.squareup.picasso.Picasso


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
                    val binding = ItemPhotoBinding.bind(view)
                    item.type.url.takeIf { URLUtil.isValidUrl(it) }
                        ?.let { Picasso.get().load(it).into(binding.imageView2) }
                    binding.root.setOnClickListener(listener)
                }
                else -> {
                    val binding = ListItemDetailsBinding.bind(view)
                    binding.title.text = title
                    binding.subtitle.text = subtitle
                    binding.root.setOnClickListener(listener)
                    binding.accessoryImageView.isVisible = item.showAccessory
                }
            }
        }

        private fun getTitle(type: UserDetailsItem.ContentType) : String = when(type){
            is UserDetailsItem.ContentType.FirstName -> context.getString(R.string.firstName)
            is UserDetailsItem.ContentType.LastName -> context.getString(R.string.lastName)
            is UserDetailsItem.ContentType.Address -> context.getString(R.string.address)
            is UserDetailsItem.ContentType.Location -> context.getString(R.string.location)
            is UserDetailsItem.ContentType.Email -> context.getString(R.string.email)
            is UserDetailsItem.ContentType.DobDate -> context.getString(R.string.dobDate)
            is UserDetailsItem.ContentType.DobAge -> context.getString(R.string.dobAge)
            is UserDetailsItem.ContentType.Phone -> context.getString(R.string.phone)
            is UserDetailsItem.ContentType.Cell -> context.getString(R.string.cell)
            is UserDetailsItem.ContentType.Gender -> context.getString(R.string.gender)
            is UserDetailsItem.ContentType.Nat -> context.getString(R.string.nat)
            else -> ""
        }

        private fun getSubtitle(type: UserDetailsItem.ContentType) : String = when(type){
            is UserDetailsItem.ContentType.FirstName -> type.firstName
            is UserDetailsItem.ContentType.LastName -> type.lastName
            is UserDetailsItem.ContentType.Address -> type.address
            is UserDetailsItem.ContentType.Location -> type.location
            is UserDetailsItem.ContentType.Email -> type.email
            is UserDetailsItem.ContentType.DobDate -> type.dobDate
            is UserDetailsItem.ContentType.DobAge -> type.dobAge
            is UserDetailsItem.ContentType.Phone -> type.phone
            is UserDetailsItem.ContentType.Cell -> type.cell
            is UserDetailsItem.ContentType.Gender -> type.gender
            is UserDetailsItem.ContentType.Nat -> type.nat
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
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
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




