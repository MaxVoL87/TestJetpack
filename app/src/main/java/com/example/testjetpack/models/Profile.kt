package com.example.testjetpack.models

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("photo_uri") val photoUri: String,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    @SerializedName("driver_license") val driverLicense: String,
    @SerializedName("state_of_issue") val stateOfIssue: String
)