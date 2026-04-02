package org.cmp_arch.core

sealed interface AppError {
    data class Network(val message: String, val code: Int? = null) : AppError
    data class Local(val message: String) : AppError
    data class Validation(val message: String) : AppError
    data class Unknown(val throwable: Throwable) : AppError
}

fun AppError.asUiMessage(): String = when (this) {
    is AppError.Network -> message
    is AppError.Local -> message
    is AppError.Validation -> message
    is AppError.Unknown -> throwable.message ?: "Unexpected error"
}
