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

class MainViewModel(
    private val userDao: UserDao = MyApplication.userDataBase.getDao()
) : ViewModel() {
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

    private suspend fun databaseEntry(result: String) : List<UserDTO> = withContext(Dispatchers.IO) {
        val jsonObject = JSONObject(result)
        val jsonObjectArray = jsonObject.getJSONArray("results")
        val userEntities = (0 until jsonObjectArray.length()).map { idx ->
            UserDBEntity(jsonString = jsonObjectArray.getJSONObject(idx).toString())
        }
        userDao.deleteAll()
        userDao.insertUsers(userEntities)
        userDao.getAllUsers().map { UserDTO.fromJSON(JSONObject(it.jsonString)) }
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
    address = arrayOf(
        location.name,
        location.number,
        location.city,
        location.state,
        location.country
    ).joinToString(", "),
    phones = cell,
    photoURL = picture.medium

)