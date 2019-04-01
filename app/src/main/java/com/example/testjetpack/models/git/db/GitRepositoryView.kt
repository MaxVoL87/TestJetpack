package com.example.testjetpack.models.git.db

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    value = "SELECT repository_table.name, repository_table.language, repository_table.url, repository_table.html_url, repository_table.index_in_response, user_table.login AS ownerName " +
            "FROM repository_table " +
            "LEFT JOIN user_table ON repository_table.owner_id = user_table.id",
    viewName = "short_repository_view"
)
data class GitRepositoryView(
    val name: String,
    val language: String?,
    val url: String,
    @ColumnInfo(name = "html_url") val htmlUrl: String?,
    @ColumnInfo(name = "index_in_response") val index: Int,
    val ownerName: String?
)