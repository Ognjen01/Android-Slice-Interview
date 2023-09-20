package com.slicelife.tastyrecipes

import kotlinx.serialization.Serializable

@Serializable
data class ItemList(val count: Int = 0, val results: List<Item> = listOf())
