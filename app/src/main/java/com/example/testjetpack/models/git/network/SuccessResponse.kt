package com.example.testjetpack.models.git.network

import com.google.gson.annotations.SerializedName

sealed class SuccessResponse : ServerResponse()


data class SearchRepositoriesResponse(
    @SerializedName("total_count") val totalCount: Long,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<GitRepository>
) : SuccessResponse()