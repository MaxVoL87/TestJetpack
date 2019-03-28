package com.example.testjetpack.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.testjetpack.models.git.License
import com.example.testjetpack.models.git.User
import com.example.testjetpack.models.git.db.GitRepository

data class GitRepositoryComplexView(
    @Embedded val gitRepository: GitRepository,
    @Relation(parentColumn = "license_name", entityColumn = "name", entity = License::class)
    var licenses: List<License>? = null,
    @Relation(parentColumn = "owner_id", entityColumn = "id", entity = User::class)
    var owners: List<User>? = null
) {
    val name: String
        get() = gitRepository.name

    val url: String
        get() = gitRepository.url

    val index: Int
        get() = gitRepository.indexInResponse

    val ownerName: String?
        get() = owners?.firstOrNull()?.login
}