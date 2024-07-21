package com.example.userapp.domain.user.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: LocalDateTime,
    val details: Map<String, String> = emptyMap()
)
