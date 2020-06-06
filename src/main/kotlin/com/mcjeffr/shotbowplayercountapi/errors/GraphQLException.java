package com.mcjeffr.shotbowplayercountapi.errors;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;

/**
 * This class contains a GraphQLException. It is implemented in Java due to Kotlin limitations, as RuntimeException and
 * GraphQLError have clashing properties / functions relating to the 'message'-attribute. This class mainly serves as
 * an entry point for all other GraphQL errors that are implemented in Kotlin.
 */
public class GraphQLException extends RuntimeException implements GraphQLError {

    String customMessage;

    public GraphQLException(String customMessage) {
        this.customMessage = customMessage;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return null;
    }

}
