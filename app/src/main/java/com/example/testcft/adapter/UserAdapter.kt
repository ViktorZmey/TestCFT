package com.example.testcft.adapter

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import androidx.core.text.bold
import androidx.core.text.color
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testcft.R
import com.example.testcft.databinding.ListItemBinding
import com.squareup.picasso.Picasso

class UserAdapter(
    private val context: Context,
    private val listener: Listener? = null
) : ListAdapter<UserItem, UserAdapter.Holder>(Comparator()) {

    class Holder(
        view: View,
        private val context: Context
    ) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemBinding.bind(view)

        fun bind(item: UserItem, listener: View.OnClickListener?) = with(binding) {
            textViewName.text = SpannableStringBuilder().bold {
                color(Color.BLACK){
                    append("${context.getString(R.string.fullNameShort)}: ")
                }
            }.append(item.fullName)

            textViewLocation.text = SpannableStringBuilder().bold {
                color(Color.BLACK){
                    append("${context.getString(R.string.address)}: ")
                }
            }.append(item.address)

            textViewCell.text = SpannableStringBuilder().bold {
                color(Color.BLACK){
                    append("${context.getString(R.string.cell)}: ")
                }
            }.append(item.phones)

            root.setOnClickListener(listener)

            item.photoURL
                .takeIf { URLUtil.isValidUrl(it) }
                ?.let { Picasso.get().load(it).into(imageView) }
        }
    }

    class Comparator : DiffUtil.ItemCallback<UserItem>() {
        override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view, context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)

        holder.bind(item) {
            listener?.onClickItem(item, position)
        }
    }

    fun interface Listener {
        fun onClickItem(item: UserItem, atPosition: Int)
    }
}