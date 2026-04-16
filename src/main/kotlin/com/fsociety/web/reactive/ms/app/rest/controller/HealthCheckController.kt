package com.fsociety.web.reactive.ms.app.rest.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/healthcheck")
class HealthCheckController {

    @GetMapping
    suspend fun health(): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.ok(mapOf("status" to "UP"))
    }
}