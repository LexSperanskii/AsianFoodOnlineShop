package com.example.asianfoodonlineshop.di

import android.content.Context
import com.example.asianfoodonlineshop.data.db.OfflineUsersRepository
import com.example.asianfoodonlineshop.data.db.CartDatabase
import com.example.asianfoodonlineshop.data.db.UsersRepository
import com.example.asianfoodonlineshop.data.network.NetworkProductsRepository
import com.example.asianfoodonlineshop.data.network.ProductsRepository
import com.example.asianfoodonlineshop.ui.screens.SharedViewModel

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val usersRepository: UsersRepository
    val productsInfoRepository : ProductsRepository
    val sharedViewModel: SharedViewModel
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

    /**
     * Implementation for SharedViewModel
     */
    override val sharedViewModel: SharedViewModel by lazy {
        SharedViewModel()
    }

}