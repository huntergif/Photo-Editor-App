package com.aqchen.filterfiesta.domain.util

// Ways to order filter groups
sealed class FilterGroupsOrder(val orderType: OrderType) {
    class DateCreated(orderType: OrderType): FilterGroupsOrder(orderType)
    class Name(orderType: OrderType): FilterGroupsOrder(orderType)
}
