package com.example.testjetpack.models.git.network

import com.example.testjetpack.models.git.GitRepository
import com.google.gson.annotations.SerializedName

data class ServerResponse(
        @SerializedName("total_count") val totalCount: Long = 0,
        @SerializedName("incomplete_results") val incompleteResults: Boolean = false,
        @SerializedName("items") val items: List<GitRepository>
)