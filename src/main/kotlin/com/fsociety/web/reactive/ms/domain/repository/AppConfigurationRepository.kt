package com.fsociety.web.reactive.ms.domain.repository


import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface AppConfigurationRepository : CoroutineCrudRepository<AppConfiguration, UUID> {
    suspend fun findByKeyAndSection(key: String, section: String): AppConfiguration?
}