package com.example.testjetpack.models.git.network

import java.util.concurrent.atomic.AtomicInteger

data class GitPage(
    val number: AtomicInteger,
    val q: String,
    val perPage: Int,

    // from header
    val next: String? = null,
    val last: String? = null,
    val first: String? = null,
    val previous: String? = null
)