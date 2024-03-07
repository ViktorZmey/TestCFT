package com.example.testcft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainViewModel : ViewModel() {
    private val _liveDataList = MutableLiveData<List<UserItem>>()
    val liveDataList: LiveData<List<UserItem>> = _liveDataList

    init {
        requestUsersData()
    }

    private fun requestUsersData() {
        viewModelScope.launch {
            val resultJsonString = withContext(Dispatchers.IO) {
                URL("https://randomuser.me/api/?results=15").readText()
            }
            val userItems = _parsUsersData(resultJsonString).map {
                it.toUserItem()
            }
//            updateLiveDataList(parsUserData(resultJsonString))
            updateLiveDataList(userItems)
        }

    }

    private fun updateLiveDataList(newList: List<UserItem>) {
        _liveDataList.value = newList
    }

    private fun _parsUsersData(result: String) : List<UserDTO> {
        val jsonObject = JSONObject(result)
        val jsonObjectArray = jsonObject.getJSONArray("results")
        return (0 until jsonObjectArray.length()).map { idx ->
            UserDTO.fromJSON(jsonObjectArray.getJSONObject(idx))
        }
    }

    private fun parsUserData(result: String) : List<UserItem>  {
        val jsonObject = JSONObject(result)
        val jsonObjectArray = jsonObject.getJSONArray("results")
        val listUser = ArrayList<UserItem>()

        for(i in 0 until jsonObjectArray.length()) {
            val userItemArray = jsonObjectArray[i] as JSONObject
            val item = UserItem(
                userItemArray.getJSONObject("name").getString("first"),
                userItemArray.getJSONObject("location").getJSONObject("street").getString("name"),
                userItemArray.getString("cell"),
                userItemArray.getJSONObject("picture").getString("medium")
            )

            listUser.add(item)
        }
        return listUser
    }

    fun refreshListUsers() {
        requestUsersData()
    }
}

fun UserDTO.toUserItem() = UserItem(
    fullName = arrayOf(
        name.title,
        name.first,
        name.last
    ).joinToString( " "),
    address = "",
    phones = "",
    photoURL = "https://randomuser.me/api/?results=15"
)