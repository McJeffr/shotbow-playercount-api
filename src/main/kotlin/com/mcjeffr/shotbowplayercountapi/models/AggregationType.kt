package com.mcjeffr.shotbowplayercountapi.models

/**
 * This enum represents all possible aggregration types.
 *
 * @author McJeffr
 */
enum class AggregationType(val fnName: String) {

    AVG("mean"), MIN("min"), MAX("max")

}
