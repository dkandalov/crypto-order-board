package org.crypto

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
