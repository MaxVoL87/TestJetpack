package com.example.testjetpack.models.git

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_table", indices = [Index(value = ["id"])])
data class User(
        @SerializedName("gists_url") @ColumnInfo(name = "gists_url") val gistsUrl: String?,
        @SerializedName("repos_url") @ColumnInfo(name = "repos_url") val reposUrl: String?,
        @SerializedName("following_url") @ColumnInfo(name = "following_url") val followingUrl: String?,
        @SerializedName("starred_url") @ColumnInfo(name = "starred_url") val starredUrl: String?,
        @SerializedName("login") val login: String,
        @SerializedName("followers_url") @ColumnInfo(name = "followers_url") val followersUrl: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("url") val url: String?,
        @SerializedName("subscriptions_url") @ColumnInfo(name = "subscriptions_url") val subscriptionsUrl: String?,
        @SerializedName("received_events_url") @ColumnInfo(name = "received_events_url") val receivedEventsUrl: String?,
        @SerializedName("avatar_url") @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
        @SerializedName("events_url") @ColumnInfo(name = "events_url") val eventsUrl: String?,
        @SerializedName("html_url") @ColumnInfo(name = "html_url") val htmlUrl: String?,
        @SerializedName("site_admin") @ColumnInfo(name = "site_admin") val siteAdmin: Boolean,
        @SerializedName("id") @PrimaryKey(autoGenerate = false) val id: Long,
        @SerializedName("gravatar_id") @ColumnInfo(name = "gravatar_id") val gravatarId: String?,
        @SerializedName("node_id") @ColumnInfo(name = "node_id") val nodeId: String?,
        @SerializedName("organizations_url") @ColumnInfo(name = "organizations_url") val organizationsUrl: String?
)