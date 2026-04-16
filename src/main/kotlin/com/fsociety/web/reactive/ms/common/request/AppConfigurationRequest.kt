package com.fsociety.web.reactive.ms.common.request

import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration

data class AppConfigurationRequest(
    val key: String,
    val value: String,
    val section: String,
) {
    companion object {
        fun AppConfigurationRequest.toEntity(): AppConfiguration {
            return AppConfiguration(
                key = key,
                value = value,
                section = section
            )
        }
    }
}