package com.example.testcft.fragment

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testcft.MainViewModel
import com.example.testcft.adapter.UserAdapter
import com.example.testcft.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: UserAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = UserAdapter(requireContext()) { _, position ->
            println("MyPrint: $position")
//        model....(position)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initRcView()
        bindWithViewModel()
    }

    private fun bindWithViewModel() {
        model.liveDataList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    private fun permissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity, "Permition is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            model.refreshListUsers()
            refreshLayout.isRefreshing = false
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.INTERNET)){
            permissionListener()
            pLauncher.launch(Manifest.permission.INTERNET)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}