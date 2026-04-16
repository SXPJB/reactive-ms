package com.fsociety.web.reactive.ms.common.response

data class AppConfigurationResponse(
    val id: String,
    val key: String,
    val value: String,
    val section: String,
)
