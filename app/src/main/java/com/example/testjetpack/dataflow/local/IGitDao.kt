package com.example.testjetpack.dataflow.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.testjetpack.models.GitRepositoryView

@Dao
interface IGitDao {

    @Transaction
    @Query("SELECT * FROM repository_table ORDER BY index_in_response ASC")
    fun getAllGitRepositoriesSortedByRespIndex(): DataSource.Factory<Int, GitRepositoryView>

}