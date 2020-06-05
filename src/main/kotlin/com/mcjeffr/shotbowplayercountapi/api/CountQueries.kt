package com.mcjeffr.shotbowplayercountapi.api

import com.mcjeffr.shotbowplayercountapi.errors.InvalidParameterError
import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.repositories.CountRepository
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class CountQueries @Autowired constructor(private val countRepository: CountRepository): GraphQLQueryResolver {

    fun getLatest(): Count {
        return countRepository.getLatest()
    }

    fun getInterval(from: Date, to: Date): List<Count> {
        if (from.after(to)) {
            throw InvalidParameterError("'from' cannot be after 'to'")
        }
        return countRepository.getInterval(from, to)
    }

}
