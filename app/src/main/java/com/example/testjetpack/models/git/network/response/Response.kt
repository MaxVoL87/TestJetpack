package com.example.testjetpack.models.git.network.response

import com.example.testjetpack.models.git.network.request.GitPage
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
) : SuccessGitResponse<T>() {

    private var _links: MutableMap<String, String>? = null

    fun initLinks(response: Response<*>) {
        val links = mutableMapOf<String, String>()

        links[LinkOfPage.CUR_LINK.regValue] = response.raw().request().url().toString()
        response.headers().get("link")?.let {
            val matcher = LINK_PATTERN.matcher(it)
            while (matcher.find()) {
                if (matcher.groupCount() == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
        }
        _links = links
    }

    fun getLink(type: LinkOfPage): String? {
        val links = _links
        return if (links != null) links[type.regValue] else null
    }

    fun getPage(): GitPage? {
        val links = _links ?: return null
        val url = links[LinkOfPage.CUR_LINK.regValue] ?: return null
        val matcher = Pattern.compile("$Q_PATTERN&$PAGE_PATTERN&$PER_PAGE_PATTERN").matcher(url)
        if (!matcher.find() || matcher.groupCount() != 3) return null
        return try {
            val lastLink = links[LinkOfPage.LAST_LINK.regValue]
            GitPage(
                q = matcher.group(1),
                page = Integer.parseInt(matcher.group(2)),
                perPage = Integer.parseInt(matcher.group(3)),
                previous = links[LinkOfPage.PREV_LINK.regValue],
                next = links[LinkOfPage.NEXT_LINK.regValue],
                first = links[LinkOfPage.FIRST_LINK.regValue],
                last = lastLink,
                isLast = lastLink == null
            )
        } catch (ex: NumberFormatException) {
            Timber.w("cannot parse page from %s", url)
            null
        }
    }

    enum class LinkOfPage(val regValue: String) {
        CUR_LINK("cur"),
        PREV_LINK("prev"),
        NEXT_LINK("next"),
        LAST_LINK("last"),
        FIRST_LINK("first")
    }

    private fun getPageNum(link: String?): Int? {
        val matcher = PAGE_PATTERN.matcher(link)
        if (!matcher.find() || matcher.groupCount() != 1) return null
        return try {
            Integer.parseInt(matcher.group(1))
        } catch (ex: NumberFormatException) {
            Timber.w("cannot parse page from %s", link)
            null
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val Q_PATTERN = Pattern.compile("\\bq=(\\w+)")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private val PER_PAGE_PATTERN = Pattern.compile("\\bper_page=(\\d+)")
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