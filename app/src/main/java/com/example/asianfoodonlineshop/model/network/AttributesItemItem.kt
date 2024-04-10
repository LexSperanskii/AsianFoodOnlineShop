package com.example.asianfoodonlineshop.model.network

import kotlinx.serialization.Serializable

data class AttributesItemModel(
    val id: Int,
    val name: String
)
@Serializable
data class AttributesItem(
    val id: Int,
    val name: String
)