package com.example.testcft.fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.testcft.MainViewModel
import com.example.testcft.UsersItem
import com.example.testcft.adapter.UserAdapter
import com.example.testcft.databinding.FragmentMainBinding
import org.json.JSONArray
import org.json.JSONObject

const val countUsers = 3

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initRcView()
        requestUserData()
        updateCurrentCard()

    }

    private fun updateCurrentCard() = with(binding){
        model.liveDataCurrent.observe(viewLifecycleOwner){
            rcView
        }
    }
    private fun permissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity, "Permition is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = UserAdapter()
        rcView.adapter = adapter
        // временный список
        val list = listOf(
            UsersItem("aaa", "RRR", "4543", "d"),
            UsersItem("bbb", "EEE", "5424", "f"),
            UsersItem("ccc", "WWW", "737", "s"),
            UsersItem("ddd", "QQQ", "7375", "e")
        )
        adapter.submitList(list)

    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.INTERNET)){
            permissionListener()
            pLauncher.launch(Manifest.permission.INTERNET)
        }
    }

    private fun requestUserData() {
        val url = "https://randomuser.me/api/?results="+ countUsers
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                result -> parsUserData(result)
            },
            {
                error -> println("ERROR: $error")
            }

        )
        queue.add(request)
    }

    private fun parsUserData(result: String){
        val jsonObgect = JSONObject(result)
        val jsonObjectArray = jsonObgect.getJSONArray("results")
        for(i in 0 until jsonObjectArray.length()){
            val userItemArray = jsonObjectArray[i] as JSONObject
            val item = UsersItem(
                userItemArray.getJSONObject("name").getString("first"),
                userItemArray.getJSONObject("location").getJSONObject("street").getString("name"),
                userItemArray.getString("cell"),
                userItemArray.getJSONObject("picture").getString("medium")
            )
            println("Myprint name: ${item.firstName}")
            println("Myprint street: ${item.streetName}")
            println("Myprint cell: ${item.cell}")
            println("Myprint url: ${item.pictureURL}")
            model.liveDataCurrent.value = item
        }


    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}