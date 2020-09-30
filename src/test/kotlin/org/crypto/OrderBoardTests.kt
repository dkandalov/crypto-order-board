package org.crypto

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import org.crypto.OrderType.Buy
import org.crypto.OrderType.Sell
import org.junit.jupiter.api.Test
import java.math.BigDecimal


class OrderBoardTests {
    private val someCoin = CoinType("someCoin")
    private val someUser = UserId("someUserId")

    @Test fun `summary for empty order board`() {
        val emptyOrderBoard = OrderBoard()
        assertThat(emptyOrderBoard.summary(Buy, someCoin), isEmpty)
        assertThat(emptyOrderBoard.summary(Sell, someCoin), isEmpty)
    }

    @Test fun `summary for a single sell order`() {
        val orderBoard = OrderBoard()
        orderBoard.place(Order(Sell, someUser, someCoin, Quantity(1), Price(2)))

        assertThat(
            orderBoard.summary(Sell, someCoin),
            equalTo(listOf(SummaryRow(Quantity(1), Price(2))))
        )
        assertThat(orderBoard.summary(Buy, someCoin), isEmpty)
    }

    @Test fun `summary for a single buy order`() {
        val orderBoard = OrderBoard()
        orderBoard.place(Order(Buy, someUser, someCoin, Quantity(3), Price(4)))

        assertThat(
            orderBoard.summary(Buy, someCoin),
            equalTo(listOf(SummaryRow(Quantity(3), Price(4))))
        )
        assertThat(orderBoard.summary(Sell, someCoin), isEmpty)
    }

}

class OrderBoard {
    private val orders = ArrayList<Order>()

    fun place(order: Order) {
        orders.add(order)
    }

    fun summary(orderType: OrderType, coinType: CoinType): List<SummaryRow> {
        return orders
            .filter { it.type == orderType }
            .map { SummaryRow(it.quantity, it.pricePerCoin) }
    }
}

data class SummaryRow(
    val quantity: Quantity,
    val pricePerCoin: Price
)

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

    override fun toString() = value.toString()
}

data class Price(val value: BigDecimal) {
    constructor(value: Int) : this(value.toBigDecimal())

    override fun toString() = value.toString()
}
