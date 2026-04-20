package com.fsociety.web.reactive.ms.domain.entity

import com.fsociety.web.reactive.ms.common.response.AppConfigurationResponse
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.util.*


@Table(name = "app_configuration")
class AppConfiguration(
    @Id
    @Column("id")
    private var entityId: UUID? = null,
    var key: String,
    var value: String,
    var section: String,
) : Persistable<UUID> {

    @Transient
    var isNewEntity: Boolean = true

    override fun getId(): UUID? = entityId

    override fun isNew(): Boolean = entityId == null

    fun prepareForInsert(): AppConfiguration {
        if (entityId == null) {
            entityId = UUID.randomUUID()
        }
        return this
    }

    override fun toString(): String {
        return "AppConfiguration{" +
                "entityId='$entityId', " +
                "key='$key', " +
                "value='$value', " +
                "section='$section', " +
                "isNewEntity=$isNewEntity" +
                "}"
    }

    companion object {
        fun AppConfiguration.toResponse(): AppConfigurationResponse {
            return AppConfigurationResponse(
                id = entityId.toString(),
                key,
                value,
                section,
            )
        }
    }
}
