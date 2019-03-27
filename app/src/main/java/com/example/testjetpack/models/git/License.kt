package com.example.testjetpack.models.git

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "license_table", indices = [Index(value = ["name"])])
data class License(
        @SerializedName("name") @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "name") val name: String,
        @SerializedName("spdx_id") @ColumnInfo(name = "spdx_id") val spdxId: String,
        @SerializedName("key") @ColumnInfo(name = "key") val key: String,
        @SerializedName("url") @ColumnInfo(name = "url") val url: String?,
        @SerializedName("node_id") @ColumnInfo(name = "node_id") val nodeId: String
)