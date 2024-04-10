package com.example.asianfoodonlineshop.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.asianfoodonlineshop.AsianFoodOnlineShopApplication
import com.example.asianfoodonlineshop.ui.screens.cartScreen.CartScreenViewModel
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.CatalogProductScreenViewModel


/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            CatalogProductScreenViewModel(
                usersRepository = onlineShopApplication().container.usersRepository,
                productsRepository = onlineShopApplication().container.productsInfoRepository
            )
        }
        initializer {
            CartScreenViewModel(
                usersRepository = onlineShopApplication().container.usersRepository,
                productsRepository = onlineShopApplication().container.productsInfoRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [AsianFoodOnlineShopApplication].
 */
fun CreationExtras.onlineShopApplication(): AsianFoodOnlineShopApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AsianFoodOnlineShopApplication)
