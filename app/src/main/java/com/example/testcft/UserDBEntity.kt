package com.example.testcft

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "UsersDBEntity")
data class UserDBEntity (
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,
    var jsonString : String
)