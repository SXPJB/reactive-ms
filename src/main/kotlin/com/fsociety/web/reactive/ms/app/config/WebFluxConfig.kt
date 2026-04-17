package com.fsociety.web.reactive.ms.app.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.ApiVersionConfigurer
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebFluxConfig : WebFluxConfigurer {

    override fun configureApiVersioning(configurer: ApiVersionConfigurer) {
        configurer
            .addSupportedVersions("v1")
            .setDefaultVersion("v1")
            .usePathSegment(1)
    }
}