package com.mcjeffr.shotbowplayercountapi.errors

import graphql.ErrorClassification
import graphql.ErrorType

open class InvalidParameterError(
        private val errorMessage: String,
        private val parameters: Map<String, Any>? = mutableMapOf()
) : GraphQLException(errorMessage) {

    override val message: String?
        get() = errorMessage

    override fun getErrorType(): ErrorClassification {
        return ErrorType.ValidationError
    }

    override fun getExtensions(): MutableMap<String, Any> {
        return mutableMapOf("parameters" to (parameters ?: mutableMapOf()))
    }

}
