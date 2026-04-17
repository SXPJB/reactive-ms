package com.fsociety.web.reactive.ms.app.config

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

private const val REQUEST_RECEIVED_LOG = "Received request: {}, {}"
private const val REQUEST_COMPLETED_LOG = "Completed request: {}: {} {}, status: {}"
private const val RESPONSE_SUCCESSFUL = "Successful"
private const val RESPONSE_FAILURE = "Failure"

@Component
class LoggingWebFilter : WebFilter {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void> {
        logger.trace(REQUEST_RECEIVED_LOG, exchange.request.method, exchange.request.uri.path)
        return chain.filter(exchange)
            .doFinally {
                logger.trace(
                    REQUEST_COMPLETED_LOG,
                    exchange.response.statusCode?.getStatusDescription(),
                    exchange.request.method,
                    exchange.request.uri.path,
                    exchange.response.statusCode,
                )
            }
    }

    private fun HttpStatusCode.getStatusDescription(): String {
        return if (is2xxSuccessful) {
            RESPONSE_SUCCESSFUL
        } else {
            RESPONSE_FAILURE
        }
    }
}
