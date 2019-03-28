package com.example.testjetpack.models

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation
import com.example.testjetpack.models.git.db.GitRepository
import com.example.testjetpack.models.git.License
import com.example.testjetpack.models.git.User

@DatabaseView(
    "SELECT repository_table.name, repository_table.url, repository_table.index_in_response, user_table.login AS ownerName " +
            "FROM repository_table " +
            "LEFT JOIN user_table ON repository_table.owner_id = user_table.id"
)
data class GitRepositoryView(
    val name: String,
    val url: String,
    @ColumnInfo(name = "index_in_response") val index:  Int,
    val ownerName: String?
)