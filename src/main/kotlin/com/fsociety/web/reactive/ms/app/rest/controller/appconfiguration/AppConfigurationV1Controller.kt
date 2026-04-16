package com.fsociety.web.reactive.ms.app.rest.controller.appconfiguration

import com.fsociety.web.reactive.ms.common.request.AppConfigurationRequest
import com.fsociety.web.reactive.ms.common.response.AppConfigurationResponse
import com.fsociety.web.reactive.ms.core.service.AppConfigurationService
import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/app-configuration")
class AppConfigurationV1Controller(
    private val appConfigurationService: AppConfigurationService
) {

    @PostMapping
    suspend fun save(
        @RequestBody request: AppConfigurationRequest
    ): ResponseEntity<AppConfigurationResponse> {
        return ResponseEntity.ok(appConfigurationService.save(request))
    }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable
        id: String,
        @RequestBody
        request: AppConfigurationRequest
    ): ResponseEntity<AppConfigurationResponse> {
        return ResponseEntity.ok(appConfigurationService.update(id, request))
    }

    @GetMapping("/all")
    suspend fun findAll(): ResponseEntity<List<AppConfigurationResponse>> {
        return ResponseEntity.ok(appConfigurationService.findAll())
    }

    @DeleteMapping("/{id}")
    suspend fun delete(
        @PathVariable
        id: String,
    ): ResponseEntity<Void> {
        appConfigurationService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{key}/{section}")
    suspend fun getByKeyAndSection(
        @PathVariable
        key: String,
        @PathVariable
        section: String
    ): ResponseEntity<AppConfigurationResponse> {
        return ResponseEntity.ok(appConfigurationService.findByKeyAndSection(key, section))
    }

}