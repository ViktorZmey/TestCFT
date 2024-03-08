package com.example.testcft

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DB_NAME = "user.db"

@Database(entities = [UserDBEntity::class], version = 1, exportSchema = false)
abstract class UserDataBase : RoomDatabase() {
    abstract fun getDao(): UserDao

    companion object {
        fun getUserDB(appContext: Context) = Room
            .databaseBuilder(appContext, UserDataBase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }
}
