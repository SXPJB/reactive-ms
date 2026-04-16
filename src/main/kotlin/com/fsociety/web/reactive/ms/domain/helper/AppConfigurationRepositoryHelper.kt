package com.fsociety.web.reactive.ms.domain.helper

import com.fsociety.web.reactive.ms.common.execption.AppException
import com.fsociety.web.reactive.ms.common.request.AppConfigurationRequest
import com.fsociety.web.reactive.ms.common.request.AppConfigurationRequest.Companion.toEntity
import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import com.fsociety.web.reactive.ms.domain.repository.AppConfigurationRepository
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Component

private val NOT_FOUND_MESSAGE = "Not found app configuration"

@Component
class AppConfigurationRepositoryHelper(
    private val appConfigurationRepository: AppConfigurationRepository
) {

    suspend fun save(request: AppConfigurationRequest): AppConfiguration {
        return appConfigurationRepository.save(request.toEntity()).awaitFirst()
    }

    suspend fun update(id: String, request: AppConfigurationRequest): AppConfiguration {
        return findByIdOrThrow(id).copy(
            key = request.key,
            value = request.value,
            section = request.section,
        ).let(appConfigurationRepository::save).awaitFirst()
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
        return appConfigurationRepository.findById(id).awaitFirstOrElse {
            throw AppException(NOT_FOUND_MESSAGE, NOT_FOUND)
        }
    }
}