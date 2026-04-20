package com.fsociety.web.reactive.ms.domain.callback

import com.fsociety.web.reactive.ms.domain.entity.AppConfiguration
import org.reactivestreams.Publisher
import org.springframework.data.r2dbc.mapping.event.AfterConvertCallback
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback
import org.springframework.data.relational.core.sql.SqlIdentifier
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AppConfigurationAfterConvertCallback : BeforeConvertCallback<AppConfiguration>,
    AfterConvertCallback<AppConfiguration> {

    override fun onBeforeConvert(
        entity: AppConfiguration,
        table: SqlIdentifier
    ): Publisher<AppConfiguration> {
        entity.prepareForInsert()
        entity.isNewEntity = true
        return Mono.just(entity)
    }

    override fun onAfterConvert(
        entity: AppConfiguration,
        table: SqlIdentifier
    ): Publisher<AppConfiguration> {
        entity.isNewEntity = false
        return Mono.just(entity)
    }
}