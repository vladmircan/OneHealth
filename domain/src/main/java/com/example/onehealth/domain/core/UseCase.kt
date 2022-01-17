package com.example.onehealth.domain.core

abstract class UseCase<in Params: UseCase.Params, out Type> {

    protected abstract suspend fun execute(params: Params): Type

    open suspend operator fun invoke(params: Params): Result<Type> {
        return try {
            Result.success(execute(params))
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    interface Params
    object NoParams: Params
}