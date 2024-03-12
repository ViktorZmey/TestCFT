package com.example.testcft

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testcft.adapter.UserDetailAdapter
import com.example.testcft.databinding.ActivityUserDetailsBinding


class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserDetailsBinding
    private lateinit var adapter: UserDetailAdapter
    private val model: UserDetailsModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindViewModel()
        adapter = UserDetailAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        val id = intent.getLongExtra("userId", -1)
        model.setUserId(id)
    }

    private fun bindViewModel() {
        model.userDetailItemsLiveData.observe(this){
            adapter.submitList(it)
        }
    }
}