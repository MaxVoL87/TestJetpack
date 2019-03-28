package com.example.testjetpack.models.git.network.response

import com.google.gson.annotations.SerializedName

sealed class GitResponse<T>

sealed class SuccessGitResponse<T> : GitResponse<T>()
sealed class ErrorGitResponse<T> : GitResponse<T>() {
    abstract val message: String
}

//region Success

data class PagedGitResponse<T>(
    @SerializedName("total_count") val totalCount: Long,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<T>
) : SuccessGitResponse<T>()

//endregion Success


//region Error

data class ErrorGitListing<T>(
    @SerializedName("message") override val message: String,
    @SerializedName("errors") val errors: List<T>
) : ErrorGitResponse<T>()

data class ErrorGitListingWithDoc<T>(
    @SerializedName("message") override val message: String,
    @SerializedName("errors") val errors: List<T>,
    @SerializedName("documentation_url") val documentation_url: String
) : ErrorGitResponse<T>()

data class GitFieldError(
    @SerializedName("resource") val resource: String,
    @SerializedName("field") val field: String,
    @SerializedName("code") val code: String
)

//endregion Error