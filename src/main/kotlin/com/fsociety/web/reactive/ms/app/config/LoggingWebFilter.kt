package com.fsociety.web.reactive.ms.app.config

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class LoggingWebFilter : WebFilter {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void> {
        logger.debug("Income request: {}:{}", exchange.request.method, exchange.request.uri.path)
        return chain.filter(exchange)
            .doOnSuccess {
                logger.debug("Request complete: {}:{}", exchange.request.method, exchange.request.uri.path)
            }
    }
}