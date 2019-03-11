package com.example.testjetpack.models

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("title") val title: String,
    @SerializedName("text") val text: String,
    @SerializedName("date_of_creation") val dateOfCreation: String,
    @SerializedName("date_of_read") val dateOfRead: String?
)