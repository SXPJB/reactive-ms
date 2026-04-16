package com.fsociety.web.reactive.ms.common.execption

import org.springframework.http.HttpStatus

class AppException(
    override val message: String,
    val status: HttpStatus = HttpStatus.BAD_REQUEST,
) : RuntimeException()