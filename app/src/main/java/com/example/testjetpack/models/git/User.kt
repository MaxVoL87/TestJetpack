package com.example.testjetpack.models.git

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class User(
        @SerializedName("gists_url") val gistsUrl: String?,
        @SerializedName("repos_url") val reposUrl: String?,
        @SerializedName("following_url") val followingUrl: String?,
        @SerializedName("starred_url") val starredUrl: String?,
        @SerializedName("login") val login: String,
        @SerializedName("followers_url") val followersUrl: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("url") val url: String?,
        @SerializedName("subscriptions_url") val subscriptionsUrl: String?,
        @SerializedName("received_events_url") val receivedEventsUrl: String?,
        @SerializedName("avatar_url") val avatarUrl: String?,
        @SerializedName("events_url") val eventsUrl: String?,
        @SerializedName("html_url") val htmlUrl: String?,
        @SerializedName("site_admin") val siteAdmin: Boolean,
        @SerializedName("id") @PrimaryKey(autoGenerate = false) val id: Long,
        @SerializedName("gravatar_id") val gravatarId: String?,
        @SerializedName("node_id") val nodeId: String?,
        @SerializedName("organizations_url") val organizationsUrl: String?
)