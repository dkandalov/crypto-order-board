package org.crypto

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.isEmpty
import org.crypto.OrderType.Buy
import org.crypto.OrderType.Sell
import org.junit.jupiter.api.Test


class OrderBoardTests {
    private val someCoin = CoinType("someCoin")
    private val someUser = UserId("someUserId")
    private val someQuantity = Quantity(123)

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

    @Test fun `cancelled orders are removed from summary`() {
        val orderBoard = OrderBoard()
        val order = Order(Sell, someUser, someCoin, Quantity(1), Price(2))

        orderBoard.place(order)
        orderBoard.cancel(order)

        assertThat(orderBoard.summary(Sell, someCoin), isEmpty)
    }

    @Test fun `orders for the same price and coin type are merged`() {
        val orderBoard = OrderBoard()
        orderBoard.place(Order(Sell, someUser, someCoin, Quantity(10), Price(123)))
        orderBoard.place(Order(Sell, UserId("anotherUserId"), someCoin, Quantity(2), Price(123)))

        assertThat(
            orderBoard.summary(Sell, someCoin),
            equalTo(listOf(SummaryRow(Quantity(12), Price(123))))
        )
    }

    @Test fun `orders for different price are not merged`() {
        val orderBoard = OrderBoard()
        orderBoard.place(Order(Sell, someUser, someCoin, Quantity(10), Price(1)))
        orderBoard.place(Order(Sell, someUser, someCoin, Quantity(20), Price(2)))

        val summary = orderBoard.summary(Sell, someCoin)
        assertThat(summary, hasElement(SummaryRow(Quantity(10), Price(1))))
        assertThat(summary, hasElement(SummaryRow(Quantity(20), Price(2))))
    }

    @Test fun `orders for different coin type are not merged`() {
        val orderBoard = OrderBoard()
        orderBoard.place(Order(Sell, someUser, CoinType("CoinA"), Quantity(1), Price(123)))
        orderBoard.place(Order(Sell, someUser, CoinType("CoinB"), Quantity(2), Price(123)))

        assertThat(
            orderBoard.summary(Sell, CoinType("CoinA")),
            equalTo(listOf(SummaryRow(Quantity(1), Price(123))))
        )
        assertThat(
            orderBoard.summary(Sell, CoinType("CoinB")),
            equalTo(listOf(SummaryRow(Quantity(2), Price(123))))
        )
    }

    @Test fun `sell orders are sorted from low to high price`() {
        val orderBoard = OrderBoard()
        orderBoard.place(Order(Sell, someUser, someCoin, someQuantity, Price(2)))
        orderBoard.place(Order(Sell, someUser, someCoin, someQuantity, Price(1)))
        orderBoard.place(Order(Sell, someUser, someCoin, someQuantity, Price(3)))

        assertThat(
            orderBoard.summary(Sell, someCoin), equalTo(listOf(
                SummaryRow(someQuantity, Price(1)),
                SummaryRow(someQuantity, Price(2)),
                SummaryRow(someQuantity, Price(3))
            ))
        )
    }

    @Test fun `buy orders are sorted from low to high price`() {
        val orderBoard = OrderBoard()
        orderBoard.place(Order(Buy, someUser, someCoin, someQuantity, Price(2)))
        orderBoard.place(Order(Buy, someUser, someCoin, someQuantity, Price(1)))
        orderBoard.place(Order(Buy, someUser, someCoin, someQuantity, Price(3)))

        assertThat(
            orderBoard.summary(Buy, someCoin), equalTo(listOf(
                SummaryRow(someQuantity, Price(3)),
                SummaryRow(someQuantity, Price(2)),
                SummaryRow(someQuantity, Price(1))
            ))
        )
    }

    @Test fun `summary has at most the specified amount of rows`() {
        val orderBoard = OrderBoard()
        orderBoard.place(Order(Sell, someUser, someCoin, someQuantity, Price(1)))
        orderBoard.place(Order(Sell, someUser, someCoin, someQuantity, Price(2)))
        orderBoard.place(Order(Sell, someUser, someCoin, someQuantity, Price(3)))
        orderBoard.place(Order(Sell, someUser, someCoin, someQuantity, Price(4)))

        assertThat(
            orderBoard.summary(Sell, someCoin, maxRows = 2), equalTo(listOf(
                SummaryRow(someQuantity, Price(1)),
                SummaryRow(someQuantity, Price(2))
            ))
        )
    }

    @Test fun `printable summary`() {
        val summary = listOf(
            SummaryRow(Quantity("123.45"), Price("234.5")),
            SummaryRow(Quantity("12"), Price("34")),
            SummaryRow(Quantity("0.1"), Price("0.2"))
        )
        assertThat(
            summary.toPrintableString(),
            equalTo("""
                |123.45 for ￡234.5
                |12 for ￡34
                |0.1 for ￡0.2
            """.trimMargin())
        )
    }
}
