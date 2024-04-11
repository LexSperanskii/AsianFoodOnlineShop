package com.example.asianfoodonlineshop.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.asianfoodonlineshop.model.db.CartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToCart(cartItem: CartEntity)
    @Query("DELETE FROM cart WHERE id=:id")
    suspend fun deleteFromCart(id: Int)
    @Query("SELECT * FROM cart")
    fun getAllCartItems(): Flow<List<CartEntity>>
    @Query("SELECT * FROM cart WHERE id=:id")
    suspend fun getItem(id: Int) : CartEntity?
    @Query("DELETE FROM cart")
    suspend fun deleteAllCartItems()
}