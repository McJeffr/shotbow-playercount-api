package com.mcjeffr.shotbowplayercountapi.models

/**
 * This class represents a composite count components. This is a collection of count components
 * added together. This can be useful for, for instance, calculating averages of multiple count
 * components.
 *
 * @author McJeffr
 */
open class CompositeCountComponent(
        name: String,
        value: Int,
        var amount: Int
) : CountComponent(name = name, value = value) {

    /**
     * Adds a count component to this composite count component, provided the value is greater than
     * or equal to 0.
     * @param count The count component to add.
     */
    fun add(count: CountComponent) {
        if (count.value >= 0) {
            this.value += count.value
            this.amount++
        }
    }

    /**
     * Adds a composite count component to this composite count component, provided the value is
     * greater than or equal to 0.
     * @param count The composite count component to add.
     */
    fun add(count: CompositeCountComponent) {
        if (count.value >= 0) {
            this.value += count.value
            this.amount += count.amount
        }
    }

    override fun toString(): String {
        return "CompositeCountComponent(name='$name', value=$value, amount=$amount)"
    }

}
