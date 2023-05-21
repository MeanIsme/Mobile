package app.mbl.hcmute.chatApp.data.network.common

data class ErrorBody(
    val statusCode: Int,
    val error: String,
    val message: String,
)