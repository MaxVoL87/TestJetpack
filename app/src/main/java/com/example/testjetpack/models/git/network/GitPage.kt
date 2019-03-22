package com.example.testjetpack.models.git.network

data class GitPage(
    var number: Int,
    val q: String,
    val perPage: Int,

    // from header
    val next: String? = null,
    val last: String? = null,
    val first: String? = null,
    val previous: String? = null
)