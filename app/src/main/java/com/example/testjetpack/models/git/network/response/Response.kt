package com.example.testjetpack.models.git.network.response

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import timber.log.Timber
import java.util.regex.Pattern

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
) : SuccessGitResponse<T>(){

    private var links: MutableMap<String, String> = mutableMapOf()

    val nextPage: Int?
        get() {
            val next = links[NEXT_LINK] ?: return null
            val matcher = PAGE_PATTERN.matcher(next)
            if (!matcher.find() || matcher.groupCount() != 1) {
                return null
            }
            try {
                return Integer.parseInt(matcher.group(1))
            } catch (ex: NumberFormatException) {
                Timber.w("cannot parse next page from %s", next)
                return null
            }
        }

    val lastPage: Int?
        get() {
            val last = links[LAST_LINK] ?: return null
            val matcher = PAGE_PATTERN.matcher(last)
            if (!matcher.find() || matcher.groupCount() != 1) {
                return null
            }
            try {
                return Integer.parseInt(matcher.group(1))
            } catch (ex: NumberFormatException) {
                Timber.w("cannot parse last page from %s", last)
                return null
            }
        }

    fun initLinks(response: Response<*>) {
        links = mutableMapOf()

        response.headers().get("link")?.let {
            val matcher = LINK_PATTERN.matcher(it)
            while (matcher.find()) {
                if (matcher.groupCount() == 2) {
                    links.put(matcher.group(2), matcher.group(1))
                }
            }
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        val NEXT_LINK = "next"
        val LAST_LINK = "last"
    }
}

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