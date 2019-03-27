package com.example.testjetpack.dataflow.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testjetpack.models.git.User

@Dao
interface IGitUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: User)

    @Query("DELETE FROM user_table")
    fun clearAll()

    @Query("SELECT * FROM user_table")
    fun getAll(): List<User>

    @Query("SELECT * FROM user_table WHERE id = :id")
    fun getLicenseById(id: Long): User
}