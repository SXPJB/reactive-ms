package com.fsociety.web.reactive.ms.domain.repository


import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface AppConfigurationRepository : ReactiveCrudRepository<AppConfiguration, UUID> {
    fun findByKeyAndSection(key: String, section: String): Mono<AppConfiguration>
}