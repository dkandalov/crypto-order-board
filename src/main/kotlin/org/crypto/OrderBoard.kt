package org.crypto

import java.util.*
import kotlin.collections.ArrayList

class OrderBoard {
    private val orders = ArrayList<Order>()

    fun place(order: Order) {
        orders.add(order)
    }

    fun summary(orderType: OrderType, coinType: CoinType): List<SummaryRow> {
        return orders
            .filter { it.type == orderType }
            .map { SummaryRow(it.quantity, it.pricePerCoin) }
            .groupingBy { it.pricePerCoin }
            .reduce { _, accumulator, summaryRow ->
                accumulator.copy(quantity = accumulator.quantity + summaryRow.quantity)
            }
            .values.toList()
    }

    fun cancel(order: Order) {
        orders.remove(order)
    }
}

data class SummaryRow(
    val quantity: Quantity,
    val pricePerCoin: Price
)
