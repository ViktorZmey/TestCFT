package com.example.testcft

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    fun insertUsers(usersDB: List<UserDBEntity>)

    @Query("SELECT * FROM UsersDBEntity")
    fun getAllUsers() : List<UserDBEntity>

    @Query("DELETE FROM UsersDBEntity")
    fun deleteAll()

    @Query("SELECT * FROM UsersDBEntity WHERE id=:id")
    fun findUser(id: Long) : UserDBEntity?
}