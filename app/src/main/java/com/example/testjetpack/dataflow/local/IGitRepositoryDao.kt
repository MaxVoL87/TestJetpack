package com.example.testjetpack.dataflow.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.testjetpack.models.git.GitRepository

@Dao
interface IGitRepositoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg repo: GitRepository)

    @Query("DELETE FROM repository_table")
    fun clearAll()

    @Query("SELECT * FROM repository_table")
    fun getAll(): DataSource.Factory<Int, GitRepository>

    @Query("SELECT * FROM repository_table ORDER BY indexInResponse ASC")
    fun getAllSortedByRespIndex() : DataSource.Factory<Int, GitRepository>

    @Query("SELECT MAX(indexInResponse) + 1 FROM repository_table")
    fun getNextIndex() : Int
}