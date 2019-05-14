package com.example.testjetpack.models.own

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("photo_uri") val photoUri: String,
    @SerializedName("name") val name: String,
    @SerializedName("surname") val surname: String,
    @SerializedName("date_of_birth") val dateOfBirth: String,
    @SerializedName("driver_license") val driverLicense: String,
    @SerializedName("state_of_issue") val stateOfIssue: String,
    @SerializedName("heart_rate") val heartRate: Int,
    @SerializedName("sleep_time") val sleepTime: Int,
    @SerializedName("exercises_time") val exercisesTime: Int,
    @SerializedName("respiratory_rate") val respiratoryRate: Int,
    @SerializedName("rest_time") val restTime: Int,
    @SerializedName("eat_calories") val eatCalories: Int
)