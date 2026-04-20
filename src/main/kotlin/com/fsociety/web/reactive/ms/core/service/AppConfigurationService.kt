package com.fsociety.web.reactive.ms.core.service

import com.fsociety.web.reactive.ms.common.request.AppConfigurationRequest
import com.fsociety.web.reactive.ms.common.response.AppConfigurationResponse
import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration.Companion.toResponse
import com.fsociety.web.reactive.ms.domain.helper.AppConfigurationRepositoryHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class AppConfigurationService(
    private val appConfigurationRepositoryHelper: AppConfigurationRepositoryHelper
) {

    suspend fun save(request: AppConfigurationRequest): AppConfigurationResponse {
        return appConfigurationRepositoryHelper.save(request).toResponse()
    }

    suspend fun update(id: String, request: AppConfigurationRequest): AppConfigurationResponse {
        return appConfigurationRepositoryHelper.update(id, request).toResponse()
    }

    suspend fun findAll(): Flow<AppConfigurationResponse> {
        return appConfigurationRepositoryHelper.findAll().toResponseList()
    }

    suspend fun findByKeyAndSection(key: String, section: String): AppConfigurationResponse {
        return appConfigurationRepositoryHelper.findByKeyAndSection(key, section).toResponse()
    }

    suspend fun delete(id: String) {
        appConfigurationRepositoryHelper.deleteById(id)
    }

    private fun Flow<AppConfiguration>.toResponseList(): Flow<AppConfigurationResponse> {
        return map { it.toResponse() }
    }
}