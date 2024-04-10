package com.example.asianfoodonlineshop.model.network

import kotlinx.serialization.Serializable

data class CategoriesItemModel(
    val id: Int,
    val name: String
)
@Serializable
data class CategoriesItem(
    val id: Int,
    val name: String
)