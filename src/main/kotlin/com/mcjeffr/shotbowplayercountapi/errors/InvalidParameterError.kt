package com.mcjeffr.shotbowplayercountapi.errors

import graphql.ErrorClassification
import graphql.ErrorType

open class InvalidParameterError(private val errorMessage: String) : GraphQLException(errorMessage) {

    override val message: String?
        get() = errorMessage

    override fun getErrorType(): ErrorClassification {
        return ErrorType.DataFetchingException
    }

    override fun getExtensions(): MutableMap<String, Any> {
        return mutableMapOf("test" to "data")
    }

}
