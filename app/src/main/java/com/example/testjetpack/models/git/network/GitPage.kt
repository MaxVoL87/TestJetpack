package com.example.testjetpack.models.git.network

data class GitPage(
    val number: Int,
    val q: String,
    val perPage: Int,

    // from header
    val next: String?,
    val last: String?,
    val first: String?,
    val previous: String?
)