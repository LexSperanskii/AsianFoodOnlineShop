package com.example.asianfoodonlineshop.data.db

import androidx.room.Query
import com.example.asianfoodonlineshop.model.db.CartEntity
import com.example.asianfoodonlineshop.model.db.CartModel
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun insertToCart(catItem: CartModel)
    suspend fun deleteFromCart(id: Int)
    fun getAllCartItems(): Flow<List<CartModel>>
    suspend fun getItem(id: Int) : CartEntity?
    suspend fun deleteAllCartItems()
}