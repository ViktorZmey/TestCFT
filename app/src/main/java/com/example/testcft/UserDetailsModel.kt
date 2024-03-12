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

class UserDetailsModel(
    private val userDao: UserDao = MyApplication.userDataBase.getDao()
) : ViewModel() {
    private val _userDetailItemsLiveData = MutableLiveData<List<UserDetailsItem>>()
    val userDetailItemsLiveData: LiveData<List<UserDetailsItem>> = _userDetailItemsLiveData

    private suspend fun getUser(id: Long) : Result<UserDTO> = withContext(Dispatchers.IO) {
        userDao.findUser(id)?.run {
            Result.success(UserDTO.fromJSON(JSONObject(jsonString)))
        } ?: Result.failure(Exception("User not found"))
    }

    fun setUserId(id: Long) {
        viewModelScope.launch {
            getUser(id)
                .onSuccess {
                    updateLiveDataList(it)
                }.onFailure {

                }
        }
    }

    private fun updateLiveDataList(user: UserDTO) {
        _userDetailItemsLiveData.value = user.toUserDetailItems()
    }
}

fun UserDTO.toUserDetailItems() : List<UserDetailsItem> {
    val list = mutableListOf<UserDetailsItem>() // ArrayList<UserDetailsItem>()
//    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.Photo(picture.large)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.FirstName(name.first)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.LastName(name.last)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.AddressName(address.name)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.AddressNumber(address.number)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.AddressCity(address.city)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.AddressState(address.state)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.AddressCountry(address.country)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.Email(email)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.DobDate(dob.date)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.DobAge(dob.age)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.Phone(phone)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.Cell(cell)))
    list.add(UserDetailsItem(type = UserDetailsItem.ContentType.Nat(nat)))

    return list
}
