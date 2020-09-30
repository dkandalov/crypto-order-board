package org.crypto

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isEmpty
import org.crypto.OrderType.Buy
import org.crypto.OrderType.Sell
import org.junit.jupiter.api.Test
import java.math.BigDecimal


class OrderBoardTests {
    @Test fun `summary for empty order board`() {
        val emptyOrderBoard = OrderBoard()
        val someCoin = CoinType("someCoin")
        assertThat(emptyOrderBoard.summary(Buy, someCoin), isEmpty)
        assertThat(emptyOrderBoard.summary(Sell, someCoin), isEmpty)
    }
}

class OrderBoard {
    fun summary(orderType: OrderType, coinType: CoinType): List<SummaryRow> {
        return emptyList()
    }
}

data class SummaryRow(
    val quantity: Quantity,
    val pricePerCoin: Price
)

enum class OrderType {
    Buy, Sell
}

data class CoinType(val value: String)

data class Quantity(val value: BigDecimal)

data class Price(val value: BigDecimal)
