package com.example.testjetpack.dataflow.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.testjetpack.models.git.db.GitRepositoryComplexView
import com.example.testjetpack.models.git.db.GitRepositoryView

@Dao
interface IGitDao {

    // Project_Default
    // <inspection_tool class="AndroidUnresolvedRoomSqlReference" enabled="false" level="ERROR" enabled_by_default="false" />
    @Query("SELECT * FROM short_repository_view ORDER BY index_in_response ASC")
    fun getAllGitRepositoriesSortedByRespIndex(): DataSource.Factory<Int, GitRepositoryView>

    @Transaction
    @Query("SELECT * FROM repository_table ORDER BY index_in_response ASC")
    fun getAllComplexGitRepositoriesSortedByRespIndex(): DataSource.Factory<Int, GitRepositoryComplexView>

}