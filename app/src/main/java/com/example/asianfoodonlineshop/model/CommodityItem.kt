package com.example.asianfoodonlineshop.model

import com.example.asianfoodonlineshop.model.network.ProductModel

/**
 * Дата Класс для отображения его во View
 */
data class CommodityItem(
    val productItem : ProductModel,
    val image: Int,
    val quantity: Int,
)