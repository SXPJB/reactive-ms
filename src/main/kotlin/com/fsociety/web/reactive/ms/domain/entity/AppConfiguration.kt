package com.fsociety.web.reactive.ms.domain.entity

import com.fsociety.web.reactive.ms.common.response.AppConfigurationResponse
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "app_configurations")
data class AppConfiguration(
    @Id
    val id: String = ObjectId().toHexString(),
    val key: String,
    val value: String,
    val section: String,
) {
    companion object {
        fun AppConfiguration.toResponse(): AppConfigurationResponse {
            return AppConfigurationResponse(
                id,
                key,
                value,
                section,
            )
        }
    }
}
