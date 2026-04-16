package com.fsociety.web.reactive.ms.app.handler

import com.fsociety.web.reactive.ms.common.api.ErrorApi
import com.fsociety.web.reactive.ms.common.execption.AppException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebInputException
import tools.jackson.databind.exc.InvalidFormatException
import tools.jackson.databind.exc.InvalidTypeIdException
import tools.jackson.databind.exc.MismatchedInputException
import java.time.LocalDateTime

private const val UNEXPECTED_ERROR_MESSAGE = "Unexpected error"
private const val UNHANDLED_ERROR_MESSAGE = "Unhandled error"
private const val ERROR_MESSAGE_PREFIX = "Invalid request: "

private const val COMMA_SEPARATOR = ", "
private const val DOT_SEPARATOR = "."

@RestControllerAdvice
class BaseErrorController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(WebExchangeBindException::class)
    fun webExchangeBindException(ex: WebExchangeBindException): ResponseEntity<ErrorApi> {
        val message = if (ex.fieldErrors.isNotEmpty()) {
            ex.fieldErrors.joinToString(
                transform = { "'${it.field}' ${it.defaultMessage}" },
                prefix = ERROR_MESSAGE_PREFIX,
                separator = COMMA_SEPARATOR,
            )
        } else {
            ex.allErrors.joinToString(
                transform = { it.defaultMessage?.toString() ?: UNEXPECTED_ERROR_MESSAGE },
                prefix = ERROR_MESSAGE_PREFIX,
                separator = COMMA_SEPARATOR,
            )
        }

        logger.debug(message)
        return buildErrorResponse(BAD_REQUEST, message)
    }

    @ExceptionHandler(ServerWebInputException::class)
    fun serverWebInputException(ex: ServerWebInputException): ResponseEntity<ErrorApi> {
        val errorMessage = when (val cause = ex.cause) {
            is InvalidTypeIdException -> cause.path.joinToString(
                transform = { it.propertyName },
                postfix = "' subtype is invalid",
                prefix = "Invalid request: '",
                separator = DOT_SEPARATOR,
            )

            is InvalidFormatException -> "Invalid request: ${cause.originalMessage}"

            is MismatchedInputException ->
                cause.path
                    .filterNot { it.propertyName.isNullOrBlank() }
                    .joinToString(
                        transform = { it.propertyName },
                        prefix = "Invalid request: '",
                        postfix = "' is mandatory",
                        separator = DOT_SEPARATOR,
                    )

            else -> {
                logger.debug("Message not readable: ${ex.message}")
                UNHANDLED_ERROR_MESSAGE
            }
        }

        logger.debug(errorMessage)
        return buildErrorResponse(BAD_REQUEST, errorMessage)
    }

    @ExceptionHandler(AppException::class)
    fun appException(ex: AppException): ResponseEntity<ErrorApi> {
        logger.debug(ex.message)
        return buildErrorResponse(ex.status, ex.message)
    }

    private fun buildErrorResponse(
        status: HttpStatus,
        message: String,
    ): ResponseEntity<ErrorApi> {
        val errorApi = ErrorApi(
            timestamp = LocalDateTime.now(),
            reason = status,
            message = message,
        )
        return ResponseEntity.status(status).body(errorApi)
    }
}