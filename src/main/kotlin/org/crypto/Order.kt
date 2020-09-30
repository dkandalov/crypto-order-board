package org.crypto

import java.math.BigDecimal

data class Order(
    val type: OrderType,
    val userId: UserId,
    val coinType: CoinType,
    val quantity: Quantity,
    val pricePerCoin: Price
)

enum class OrderType {
    Buy, Sell
}

data class UserId(val value: String)

data class CoinType(val value: String)

data class Quantity(val value: BigDecimal) {
    constructor(value: Int) : this(value.toBigDecimal())
    constructor(value: String) : this(value.toBigDecimal())

    operator fun plus(that: Quantity) = Quantity(value + that.value)
    override fun toString() = value.toString()
}

data class Price(val value: BigDecimal) {
    constructor(value: Int) : this(value.toBigDecimal())
    constructor(value: String) : this(value.toBigDecimal())

    override fun toString() = value.toString()
}
