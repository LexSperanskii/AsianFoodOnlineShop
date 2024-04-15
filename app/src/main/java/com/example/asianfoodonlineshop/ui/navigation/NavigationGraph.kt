package com.example.asianfoodonlineshop.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asianfoodonlineshop.ui.screens.cartScreen.CartScreen
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.CatalogScreen
import com.example.asianfoodonlineshop.ui.screens.productScreen.ProductScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun SushiShopNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    NavHost(
        navController = navController,
        startDestination = CatalogDestination.route,
        modifier = modifier
    ) {
        composable(route = CatalogDestination.route) {
            CatalogScreen(
                navigateToCartButton = { navController.navigate(CartDestination.route) },
                navigateToProduct = {navController.navigate(ProductDestination.route)}
                )
        }
        composable(route = ProductDestination.route) {
            ProductScreen(
                navigateToCartButton = { navController.navigate(CartDestination.route) },
                navigateToCatalogButton = { navController.popBackStack() }
            )
        }
        composable(route = CartDestination.route) {
            CartScreen(
                currentDestinationTitle = CartDestination.title,
                navigateToCatalog = {  navController.popBackStack(navController.graph.startDestinationId, false) },
                navigateToProduct = { navController.navigate(ProductDestination.route)},
            )
        }
    }
}