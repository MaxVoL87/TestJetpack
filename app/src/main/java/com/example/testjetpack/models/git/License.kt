package com.example.testjetpack.models.git

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "license_table")
data class License(
        @SerializedName("name") @PrimaryKey(autoGenerate = false) val name: String,
        @SerializedName("spdx_id") val spdxId: String,
        @SerializedName("key") val key: String,
        @SerializedName("url") val url: String?,
        @SerializedName("node_id") val nodeId: String
)