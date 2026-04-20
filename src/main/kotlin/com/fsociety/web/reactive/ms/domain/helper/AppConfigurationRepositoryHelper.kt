package com.fsociety.web.reactive.ms.domain.helper

import com.fsociety.web.reactive.ms.common.execption.AppException
import com.fsociety.web.reactive.ms.common.request.AppConfigurationRequest
import com.fsociety.web.reactive.ms.common.request.AppConfigurationRequest.Companion.toEntity
import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import com.fsociety.web.reactive.ms.domain.repository.AppConfigurationRepository
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
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
        val entity = request.toEntity()
        return appConfigurationRepository.save(entity).awaitSingle()
    }

    suspend fun update(id: String, request: AppConfigurationRequest): AppConfiguration {
        return findByIdOrThrow(id).apply {
            value = request.value
            key = request.key
            section = request.section
        }.let(appConfigurationRepository::save).awaitSingle()
    }

    suspend fun findAll(): List<AppConfiguration> {
        return appConfigurationRepository.findAll().collectList().awaitSingle()
    }

    suspend fun deleteById(id: String) {
        val current = findByIdOrThrow(id)
        appConfigurationRepository.delete(current)
            .awaitSingleOrNull()
    }

    suspend fun findByKeyAndSection(key: String, section: String): AppConfiguration {
        return appConfigurationRepository.findByKeyAndSection(key, section).awaitFirstOrElse {
            throw AppException(NOT_FOUND_MESSAGE, NOT_FOUND)
        }
    }

    private suspend fun findByIdOrThrow(id: String): AppConfiguration {
        return appConfigurationRepository.findById(id.toUUID()).awaitFirstOrElse {
            throw AppException(NOT_FOUND_MESSAGE, NOT_FOUND)
        }
    }

    private fun String.toUUID() = runCatching {
        UUID.fromString(this)
    }.getOrElse {
        throw AppException(UUID_ERROR_MESSAGE)
    }
}