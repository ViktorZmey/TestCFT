package com.example.testcft

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<UsersItem>()
    val liveDataList = MutableLiveData<List<String>>()
}