package com.example.asianfoodonlineshop.di

import android.content.Context
import com.example.asianfoodonlineshop.data.db.OfflineUsersRepository
import com.example.asianfoodonlineshop.data.db.CartDatabase
import com.example.asianfoodonlineshop.data.db.UsersRepository
import com.example.asianfoodonlineshop.data.network.NetworkProductsRepository
import com.example.asianfoodonlineshop.data.network.ProductsRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val usersRepository: UsersRepository
    val productsInfoRepository : ProductsRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineUsersRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for retrofit
     */
    override val productsInfoRepository: ProductsRepository by lazy {
        NetworkProductsRepository()
    }

    /**
     * Implementation for [UsersRepository]
     */
    override val usersRepository: UsersRepository by lazy {
        OfflineUsersRepository(CartDatabase.getDatabase(context).userDao()) }
}