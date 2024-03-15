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
    private val _userItemLiveData = MutableLiveData<Result<List<UserItem>>>()
    val userItemLiveData: LiveData<Result<List<UserItem>>> = _userItemLiveData

    init {
        viewModelScope.launch {
            val users = getValueDB()
            if (users.isEmpty()) {
                requestUsersData()
            } else {
                updateLiveDataList(Result.success(users))
            }
        }
    }

    private fun requestUsersData() {
        viewModelScope.launch {
            val result: Result<String> = withContext(Dispatchers.IO) {
                runCatching {
                    URL("https://randomuser.me/api/?results=15").readText()
                }
            }
            val result2: Result<List<UserDTO>> = result.map { databaseEntry(it) }
            updateLiveDataList(result2)
        }
    }

    private fun updateLiveDataList(result: Result<List<UserDTO>>) {
        _userItemLiveData.value = result.map {
            it.map { user ->
                user.toUserItem()
            }
        }
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
        address.streetName,
        address.houseNumber,
        address.city,
        address.state,
        address.country,
        address.postcode
    ).joinToString(", "),
    phones = cell,
    photoURL = picture.medium
)


