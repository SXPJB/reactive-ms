package com.fsociety.web.reactive.ms.common.api

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ErrorApi(
    val timestamp: LocalDateTime,
    val message: String,
    val reason: HttpStatus,
)