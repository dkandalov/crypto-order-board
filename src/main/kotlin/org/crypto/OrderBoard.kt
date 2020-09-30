package org.crypto

import org.crypto.OrderType.*
import java.util.*
import kotlin.collections.ArrayList

class OrderBoard {
    private val orders = ArrayList<Order>()

    fun place(order: Order) {
        orders.add(order)
    }

    fun summary(orderType: OrderType, coinType: CoinType): List<SummaryRow> {
        val priceComparator =
            if (orderType == Sell) compareBy { it.value }
            else compareBy<Price> { -it.value }

        return orders
            .filter { it.type == orderType && it.coinType == coinType }
            .map { SummaryRow(it.quantity, it.pricePerCoin) }
            .groupingBy { it.pricePerCoin }
            .reduceTo(TreeMap(priceComparator)) { _, accumulator, summaryRow ->
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
