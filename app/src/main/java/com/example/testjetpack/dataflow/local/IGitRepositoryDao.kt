package com.example.testjetpack.dataflow.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testjetpack.models.git.db.GitRepository

@Dao
interface IGitRepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg repo: GitRepository)

    @Query("DELETE FROM repository_table")
    fun clearAll()

    @Query("SELECT * FROM repository_table")
    fun getAll(): List<GitRepository>

    @Query("SELECT MAX(index_in_response) + 1 FROM repository_table")
    fun getNextIndex(): Int
}