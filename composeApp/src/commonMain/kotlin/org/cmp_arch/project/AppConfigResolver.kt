package org.cmp_arch.project

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.cmp_arch.core.PlatformContext

private val parser = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun resolveAppConfig(platformContext: PlatformContext): AppConfig {
    val raw = readConfigJson(platformContext) ?: return AppConfig()
    val root = runCatching { parser.parseToJsonElement(raw).jsonObject }.getOrNull() ?: return AppConfig()

    val activeEnvironment = root["activeEnvironment"]?.jsonPrimitive?.contentOrNull ?: "dev"
    val environments = root["environments"]?.jsonObject ?: return AppConfig(environmentName = activeEnvironment)
    val selected = environments[activeEnvironment]?.jsonObject ?: environments.values.firstOrNull()?.jsonObject

    if (selected == null) return AppConfig(environmentName = activeEnvironment)

    return AppConfig(
        environmentName = activeEnvironment,
        baseUrl = selected["baseUrl"]?.jsonPrimitive?.contentOrNull ?: "https://mock.local/",
        enableNetworkLogs = selected["enableNetworkLogs"]?.jsonPrimitive?.contentOrNull?.toBooleanStrictOrNull() ?: true,
        useMockData = selected["useMockData"]?.jsonPrimitive?.contentOrNull?.toBooleanStrictOrNull() ?: true,
        useMockEngine = selected["useMockEngine"]?.jsonPrimitive?.contentOrNull?.toBooleanStrictOrNull() ?: true,
        enableAuth = selected["enableAuth"]?.jsonPrimitive?.contentOrNull?.toBooleanStrictOrNull() ?: false,
    )
}

internal expect fun readConfigJson(platformContext: PlatformContext): String?
