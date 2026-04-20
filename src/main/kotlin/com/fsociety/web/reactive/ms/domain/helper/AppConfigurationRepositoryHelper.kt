package com.fsociety.web.reactive.ms.domain.helper

import com.fsociety.web.reactive.ms.common.execption.AppException
import com.fsociety.web.reactive.ms.common.request.AppConfigurationRequest
import com.fsociety.web.reactive.ms.common.request.AppConfigurationRequest.Companion.toEntity
import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import com.fsociety.web.reactive.ms.domain.repository.AppConfigurationRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Component
import java.util.*

private const val NOT_FOUND_MESSAGE = "Not found app configuration"
private const val UUID_ERROR_MESSAGE = "Invalid UUID"

@Component
class AppConfigurationRepositoryHelper(
    private val appConfigurationRepository: AppConfigurationRepository
) {

    suspend fun save(request: AppConfigurationRequest): AppConfiguration {
        return appConfigurationRepository.save(request.toEntity())
    }

    suspend fun update(id: String, request: AppConfigurationRequest): AppConfiguration {
        return findByIdOrThrow(id).apply {
            value = request.value
            key = request.key
            section = request.section
        }.let { appConfigurationRepository.save(it) }
    }

    suspend fun findAll(): Flow<AppConfiguration> {
        return appConfigurationRepository.findAll()
    }

    suspend fun deleteById(id: String) {
        val currentEntity = findByIdOrThrow(id)
        appConfigurationRepository.delete(currentEntity)
    }

    suspend fun findByKeyAndSection(key: String, section: String): AppConfiguration {
        return appConfigurationRepository.findByKeyAndSection(key, section) ?: throw AppException(
            NOT_FOUND_MESSAGE,
            NOT_FOUND
        )
    }

    private suspend fun findByIdOrThrow(id: String): AppConfiguration {
        return appConfigurationRepository.findById(id.toUUID()) ?: throw AppException(NOT_FOUND_MESSAGE, NOT_FOUND)
    }

    private fun String.toUUID() = runCatching {
        UUID.fromString(this)
    }.getOrElse {
        throw AppException(UUID_ERROR_MESSAGE)
    }
}