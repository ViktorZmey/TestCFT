package com.example.testcft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcft.adapter.UserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import kotlin.random.Random

class MainViewModel(
    private val userDao: UserDao = MyApplication.userDataBase.getDao()
) : ViewModel() {
    private val _UserItemLiveData = MutableLiveData<List<UserItem>>()
    val UserItemLiveData: LiveData<List<UserItem>> = _UserItemLiveData

    init {
        viewModelScope.launch {
            val users = getValueDB()
            if (users.isEmpty()) {
                requestUsersData()
            } else {
                updateLiveDataList(users)
            }
        }
    }

    private fun requestUsersData() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    if (Random.nextInt(100) >= 0) {
                        URL("https://randomuser.me/api/?results=15").readText()
                    } else {
                        delay(500)
                        throw Exception("Load users failed")
                    }
                }
            }
            result
                .onSuccess { updateLiveDataList(databaseEntry(it)) }
                .onFailure { println(it) }
        }
    }

    private fun updateLiveDataList(newList: List<UserDTO>) {
        _UserItemLiveData.value = newList.map { it.toUserItem() }
    }

    private suspend fun databaseEntry(result: String) : List<UserDTO> = withContext(Dispatchers.IO) {
        val jsonObject = JSONObject(result)
        val jsonObjectArray = jsonObject.getJSONArray("results")
        val userEntities = (0 until jsonObjectArray.length()).map { idx ->
            UserDBEntity(jsonString = jsonObjectArray.getJSONObject(idx).toString())
        }
        userDao.deleteAll()
        userDao.insertUsers(userEntities)
        userDao.getAllUsers().map { it.toUserDTO() }
    }

    private suspend fun getValueDB() : List<UserDTO> = withContext(Dispatchers.IO) {
        userDao.getAllUsers().map { it.toUserDTO() }
    }

    fun refreshListUsers() {
        requestUsersData()
    }
}

fun UserDBEntity.toUserDTO() = UserDTO.fromJSON(JSONObject(jsonString)).apply { _id = id }

fun UserDTO.toUserItem() = UserItem(
    id = _id,
    fullName = arrayOf(
        name.title,
        name.first,
        name.last
    ).joinToString( " "),
    address = arrayOf(
        address.name,
        address.number,
        address.city,
        address.state,
        address.country
    ).joinToString(", "),
    phones = cell,
    photoURL = picture.medium
)