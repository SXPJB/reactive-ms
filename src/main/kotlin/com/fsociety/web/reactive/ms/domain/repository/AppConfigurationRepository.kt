package com.fsociety.web.reactive.ms.domain.repository


import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface AppConfigurationRepository : ReactiveMongoRepository<AppConfiguration, String> {
    fun findByKeyAndSection(key: String, section: String): Mono<AppConfiguration>
}