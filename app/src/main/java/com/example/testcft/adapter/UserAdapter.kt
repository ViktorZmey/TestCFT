package com.example.testcft.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testcft.R
import com.example.testcft.UsersItem
import com.example.testcft.databinding.ListItemBinding

class UserAdapter : ListAdapter<UsersItem, UserAdapter.Holder>(Comparator()) {
    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ListItemBinding.bind(view)
        fun bind(item: UsersItem) = with(binding){
            textViewName.text = item.firstName
            textViewLocation.text = item.streetName
            textViewCell.text = item.cell
        }
    }

    class Comparator : DiffUtil.ItemCallback<UsersItem>(){
        override fun areItemsTheSame(oldItem: UsersItem, newItem: UsersItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UsersItem, newItem: UsersItem): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}