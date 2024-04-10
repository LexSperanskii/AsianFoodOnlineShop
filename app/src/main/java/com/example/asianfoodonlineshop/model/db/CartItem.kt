package com.example.asianfoodonlineshop.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
data class CartModel(
    val id: Int ,
    val name: String,
    val priceCurrent: Int ,
    val priceOld : Int ?,
    val image: Int,
    val quantity: Int
)
@Entity(tableName = "cart")
data class  CartEntity(
    @PrimaryKey
    val id: Int ,
    val name: String,
    val priceCurrent: Int ,
    val priceOld : Int ?,
    val image: Int,
    val quantity: Int
)