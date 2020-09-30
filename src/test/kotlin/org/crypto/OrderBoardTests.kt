package org.crypto

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmpty
import org.crypto.OrderType.Buy
import org.crypto.OrderType.Sell
import org.junit.jupiter.api.Test


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
