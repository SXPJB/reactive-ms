package com.fsociety.web.reactive.ms.app.rest.controller.appconfiguration

import com.fsociety.web.reactive.ms.Application
import com.fsociety.web.reactive.ms.common.request.AppConfigurationRequest
import com.fsociety.web.reactive.ms.common.response.AppConfigurationResponse
import com.fsociety.web.reactive.ms.domain.repository.AppConfigurationRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(
    classes = [Application::class],
    webEnvironment = RANDOM_PORT
)
class AppConfigurationV1ControllerTest(
    @LocalServerPort
    private val port: Int,
    @Autowired
    private val appConfigurationRepository: AppConfigurationRepository,
) {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson()
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    }

    @AfterEach
    suspend fun tearDown() {
        client.close()
    }

    @BeforeEach
    suspend fun setupDb() {
        appConfigurationRepository.deleteAll()
    }

    private fun url(path: String) = "http://localhost:$port$path"

    @Test
    fun `save happyPath shouldSaveCorrectly`() = runTest {
        val request = AppConfigurationRequest(
            key = "testKey",
            value = "testValue",
            section = "testSection",
        )

        val response = client.post(url("/api/v1/app-configuration")) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.body<AppConfigurationResponse>()
        assertNotNull(body.id)
        assertEquals("testKey", body.key)
        assertEquals("testValue", body.value)
        assertEquals("testSection", body.section)
    }

    @Test
    fun `findAll happyPath shouldReturnAppConfigurations`() = runTest {
        val request = AppConfigurationRequest(
            key = "list.key",
            value = "list-value",
            section = "list-section",
        )

        client.post(url("/api/v1/app-configuration")) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        val response = client.get(url("/api/v1/app-configuration/all")) {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.body<List<AppConfigurationResponse>>()
        assertTrue(body.size == 1)
        assertEquals(true, body.any { it.key == request.key && it.section == request.section })
    }

    @Test
    fun `getByKeyAndSection happyPath shouldReturnMatchingAppConfiguration`() = runTest {
        val request = AppConfigurationRequest(
            key = "lookup.key",
            value = "lookup-value",
            section = "lookup-section",
        )

        client.post(url("/api/v1/app-configuration")) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        val response = client.get(url("/api/v1/app-configuration/${request.key}/${request.section}")) {
            accept(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.body<AppConfigurationResponse>()
        assertEquals(request.key, body.key)
        assertEquals(request.value, body.value)
        assertEquals(request.section, body.section)
    }

    @Test
    fun `update happyPath shouldChangeAppConfiguration`() = runTest {
        val created = client.post(url("/api/v1/app-configuration")) {
            contentType(ContentType.Application.Json)
            setBody(
                AppConfigurationRequest(
                    key = "update.key",
                    value = "old-value",
                    section = "update-section",
                )
            )
        }.body<AppConfigurationResponse>()

        val updatedRequest = AppConfigurationRequest(
            key = "update.key",
            value = "new-value",
            section = "update-section",
        )

        val response = client.put(url("/api/v1/app-configuration/${created.id}")) {
            contentType(ContentType.Application.Json)
            setBody(updatedRequest)
        }

        assertEquals(HttpStatusCode.OK, response.status)

        val body = response.body<AppConfigurationResponse>()
        assertEquals(created.id, body.id)
        assertEquals(updatedRequest.key, body.key)
        assertEquals(updatedRequest.value, body.value)
        assertEquals(updatedRequest.section, body.section)
    }

    @Test
    fun `delete happyPath shouldRemoveAppConfiguration`() = runTest {
        val created = client.post(url("/api/v1/app-configuration")) {
            contentType(ContentType.Application.Json)
            setBody(
                AppConfigurationRequest(
                    key = "delete.key",
                    value = "delete-value",
                    section = "delete-section",
                )
            )
        }.body<AppConfigurationResponse>()

        val deleteResponse = client.delete(url("/api/v1/app-configuration/${created.id}"))

        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val lookupResponse = client.get(url("/api/v1/app-configuration/${created.key}/${created.section}"))
        assertEquals(HttpStatusCode.NotFound, lookupResponse.status)
    }
}