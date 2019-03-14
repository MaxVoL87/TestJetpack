package com.example.testjetpack.models.git.network

import com.google.gson.annotations.SerializedName

sealed class ErrorResponse : ServerResponse()


data class ErrorMessageResponse(
    @SerializedName("message") override val message: String
) : ErrorResponse(), IErrorMessage

data class ErrorInvalidFieldsResponse(
    @SerializedName("message") override val message: String,
    @SerializedName("errors") val errors: List<FieldError>
) : ErrorResponse(), IErrorMessage

data class ErrorInvalidFieldsWithDocsResponse(
    @SerializedName("message") override val message: String,
    @SerializedName("errors") val errors: List<FieldError>,
    @SerializedName("documentation_url") val documentation_url: String
) : ErrorResponse(), IErrorMessage



data class FieldError (
    @SerializedName("resource") val resource: String,
    @SerializedName("field") val field: String,
    @SerializedName("code") val code: String
)

interface IErrorMessage {
    val message: String
}