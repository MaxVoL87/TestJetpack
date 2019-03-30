package com.example.testjetpack.models.git.network.request

import com.google.gson.annotations.SerializedName

data class GitPage(
    @SerializedName("q") val q: String,
    @SerializedName("page") val page: Int,
    @SerializedName("perPage") val perPage: Int,

    // from response header
    val previous: String? = null,
    val next: String? = null,
    val first: String? = null,
    val last: String? = null,

    // manual set
    val isLast: Boolean? = null
)