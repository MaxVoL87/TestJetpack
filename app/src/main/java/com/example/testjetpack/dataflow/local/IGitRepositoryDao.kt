package com.example.testjetpack.dataflow.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testjetpack.models.git.GitRepository

@Dao
interface IGitRepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg repo: GitRepository)

    @Query("DELETE FROM repository_table")
    fun clearAll()

    @Query("SELECT * FROM repository_table")
    fun getAll(): Array<GitRepository>
}