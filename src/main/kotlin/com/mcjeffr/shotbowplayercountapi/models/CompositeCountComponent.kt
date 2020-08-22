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
     * Secondary constructor that constructs a composite count component using a count component.
     *
     * @param component A count component object.
     */
    constructor(component: CountComponent) : this(component.name, component.value, 1)

    /**
     * Adds a count component to this composite count component.
     *
     * @param count The count component to add.
     */
    fun add(count: CountComponent) {
        this.value += count.value
        this.amount++
    }

    /**
     * Adds a composite count component to this composite count component.
     *
     * @param count The composite count component to add.
     */
    fun add(count: CompositeCountComponent) {
        this.value += count.value
        this.amount += count.amount
    }

    /**
     * Converts this composite count component to a regular count component by dividing the value
     * with the amount, essentially averaging the value.
     *
     * @return A count component object that is "averaged".
     */
    fun toCount(): CountComponent {
        return CountComponent(name = this.name, value = this.value / this.amount)
    }

    override fun toString(): String {
        return "CompositeCountComponent(name='$name', value=$value, amount=$amount)"
    }

}
