package com.example.asianfoodonlineshop.data.db

import com.example.asianfoodonlineshop.model.db.CartEntity
import com.example.asianfoodonlineshop.model.db.CartModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineUsersRepository(private val userDao: UserDao) : UsersRepository {
    /**
     * Для таблицы cart
     */
    override suspend fun insertToCart(catItem: CartModel) = userDao.insertToCart(catItem.toCartEntity())

    override suspend fun deleteFromCart(id: Int) = userDao.deleteFromCart(id)

    override fun getAllCartItems(): Flow<List<CartModel>> = userDao.getAllCartItems().map { cartEntityList ->
        cartEntityList.map { cartEntity ->
            cartEntity.toCartModel()
        }
    }

    override suspend fun getItem(id: Int): CartModel? = userDao.getItem(id)?.toCartModel()

    override suspend fun deleteAllCartItems() = userDao.deleteAllCartItems()


    //extension functions
    private fun CartEntity.toCartModel(): CartModel {
        return CartModel(id, name, priceCurrent, priceOld, image, quantity)
    }
    private fun CartModel.toCartEntity(): CartEntity {
        return CartEntity(id, name, priceCurrent, priceOld, image, quantity)
    }

}