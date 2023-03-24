package com.aqchen.filterfiesta.domain.util

// List order
sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
