package org.cmp_arch.core.network

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.errors.IOException
import org.cmp_arch.core.AppError
import org.cmp_arch.core.AppResult

suspend fun <T> safeNetworkCall(block: suspend () -> T): AppResult<T> {
    return try {
        AppResult.Success(block())
    } catch (exception: ClientRequestException) {
        val code = exception.response.status.value
        AppResult.Failure(
            AppError.Network(
                message = when (code) {
                    401 -> "Unauthorized request. Check authentication."
                    403 -> "Forbidden request. You do not have access."
                    404 -> "Endpoint not found. Check base URL and path."
                    else -> "Request failed with status $code."
                },
                code = code,
            ),
        )
    } catch (exception: ServerResponseException) {
        val code = exception.response.status.value
        AppResult.Failure(
            AppError.Network(
                message = "Server error with status $code.",
                code = code,
            ),
        )
    } catch (exception: NoTransformationFoundException) {
        AppResult.Failure(
            AppError.Network(
                message = "Unexpected response format. Expected JSON payload.",
            ),
        )
    } catch (exception: IOException) {
        AppResult.Failure(AppError.Network(message = "Please check your internet connection"))
    } catch (exception: Throwable) {
        if (
            exception::class.simpleName == "NoTransformationFoundException" ||
            exception.message?.contains("Expected response body of the type") == true
        ) {
            return AppResult.Failure(
                AppError.Network(
                    message = "Unexpected response format. Expected JSON payload.",
                ),
            )
        }
        AppResult.Failure(AppError.Unknown(exception))
    }
}
