package ru.netology.nmedia.error

sealed class AppError(message: String) : Exception(message)

class ApiError(val code: Int, message: String) : AppError(message)

class NetworkError : AppError("network_error")

class UnknownError : AppError("unknown_error")