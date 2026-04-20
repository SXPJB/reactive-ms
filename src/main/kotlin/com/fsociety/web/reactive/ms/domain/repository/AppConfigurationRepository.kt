package com.fsociety.web.reactive.ms.domain.repository


import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface AppConfigurationRepository : CoroutineCrudRepository<AppConfiguration, String> {
    suspend fun findByKeyAndSection(key: String, section: String): AppConfiguration?
}