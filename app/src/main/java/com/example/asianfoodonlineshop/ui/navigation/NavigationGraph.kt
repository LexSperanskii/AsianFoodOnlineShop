package com.example.asianfoodonlineshop.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asianfoodonlineshop.ui.AppViewModelProvider
import com.example.asianfoodonlineshop.ui.screens.cartScreen.CartScreen
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.CatalogProductScreenViewModel
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.CatalogScreen
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.ProductScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun SushiShopNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val catalogProductScreenViewModel: CatalogProductScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(
        navController = navController,
        startDestination = CatalogDestination.route,
        modifier = modifier
    ) {
        composable(route = CatalogDestination.route) {
            CatalogScreen(
                navigateToCartButton = { navController.navigate(CartDestination.route) },
                navigateToProduct = {navController.navigate(ProductDestination.route)},
                catalogProductScreenViewModel = catalogProductScreenViewModel
                )
        }
        composable(route = ProductDestination.route) {
            ProductScreen(
                navigateToCartButton = { navController.navigate(CartDestination.route) },
                navigateToCatalogButton = { navController.popBackStack() },
                catalogProductScreenViewModel = catalogProductScreenViewModel
            )
        }
        composable(route = CartDestination.route) {
            CartScreen(
                currentDestinationTitle = CartDestination.title,
                onClickNavigateBack = { navController.popBackStack() }
            )
        }
    }
}