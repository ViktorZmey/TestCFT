package com.example.testcft.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testcft.UserDetailsActivity
import com.example.testcft.MainViewModel
import com.example.testcft.adapter.UserAdapter
import com.example.testcft.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: UserAdapter

    private val model: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = UserAdapter(requireContext()) { item, _ ->
            showUserDetails(item.id)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        bindWithViewModel()
    }

    private fun bindWithViewModel() {
        model.UserItemLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            model.refreshListUsers()
        }
    }

    private fun showUserDetails(userId: Long) {
        val intent = Intent(context, UserDetailsActivity::class.java)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}