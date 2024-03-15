package com.example.testcft

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcft.adapter.UserDetailsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class UserDetailsModel(
    private val userDao: UserDao = MyApplication.userDataBase.getDao()
) : ViewModel() {
    private val _userDetailItemsLiveData = MutableLiveData<Result<List<UserDetailsItem>>>()
    val userDetailItemsLiveData: LiveData<Result<List<UserDetailsItem>>> = _userDetailItemsLiveData
    private var user: UserDTO? = null

    private suspend fun getUser(id: Long) : Result<UserDTO> = withContext(Dispatchers.IO) {
        userDao.findUser(id)?.run {
            Result.success(UserDTO.fromJSON(JSONObject(jsonString)))
        } ?: Result.failure(Exception("User not found"))
    }

    fun setUserId(id: Long) {
        viewModelScope.launch {
            updateLiveDataList(getUser(id))
        }
    }

    private fun updateLiveDataList(result: Result<UserDTO>) {
        user = result.getOrNull()
        _userDetailItemsLiveData.value = result.map { it.toUserDetailItems() }
    }

    fun getLocation() : Pair<Double, Double>? {
        return user?.location?.run { Pair(latitude, longitude) }
    }
}

fun UserDTO.toUserDetailItems() : List<UserDetailsItem> {
    val list = mutableListOf<UserDetailsItem>() // ArrayList<UserDetailsItem>()
    list.add(UserDetailsItem(UserDetailsItem.ContentType.Photo(picture.large)))
    list.add(UserDetailsItem(UserDetailsItem.ContentType.Gender(gender.name)))
    list.add(UserDetailsItem(UserDetailsItem.ContentType.FirstName(name.first)))
    list.add(UserDetailsItem(UserDetailsItem.ContentType.LastName(name.last)))
    list.add(address.run {
        listOf(country, state, city, streetName, houseNumber, postcode)
            .joinToString(", ")
            .let { UserDetailsItem(UserDetailsItem.ContentType.Address(it), true) }
    })
    list.add(location.run { 
        listOf(latitude, longitude)
            .joinToString(", ")
            .let { UserDetailsItem(UserDetailsItem.ContentType.Location(it), true)  }
    })
    list.add(UserDetailsItem(UserDetailsItem.ContentType.Email(email), true))
    list.add(UserDetailsItem(UserDetailsItem.ContentType.DobDate(
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(dob.date)
    )))
    list.add(UserDetailsItem(UserDetailsItem.ContentType.DobAge(dob.age)))
    list.add(UserDetailsItem(UserDetailsItem.ContentType.Phone(phone),true))
    list.add(UserDetailsItem(UserDetailsItem.ContentType.Cell(cell), true))
    list.add(UserDetailsItem(UserDetailsItem.ContentType.Nat(nat)))
    return list
}


