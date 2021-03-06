package com.example.testjetpack.models.own

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "notification_table", indices = [Index(value = ["id"])])
data class Notification(
    @PrimaryKey @SerializedName("id") @ColumnInfo(name = "id") val id: String,
    @SerializedName("title") @ColumnInfo(name = "title") val title: String,
    @SerializedName("text") @ColumnInfo(name = "text") val text: String,
    @SerializedName("date_of_creation") @ColumnInfo(name = "date_of_creation") val dateOfCreation: String,
    @SerializedName("date_of_read") @ColumnInfo(name = "date_of_read") val dateOfRead: String?
)