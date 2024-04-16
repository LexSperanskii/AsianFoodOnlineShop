package com.example.asianfoodonlineshop.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.asianfoodonlineshop.ui.AppViewModelProvider
import com.example.asianfoodonlineshop.ui.screens.cartScreen.CartScreen
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.CatalogScreen
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.CatalogScreenViewModel
import com.example.asianfoodonlineshop.ui.screens.productScreen.ProductScreen
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.SearchResultScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun SushiShopNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val catalogScreenViewModel: CatalogScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)

    NavHost(
        navController = navController,
        startDestination = CatalogDestination.route,
        modifier = modifier
    ) {
        composable(route = CatalogDestination.route) {
            CatalogScreen(
                navigateToCartButton = { navController.navigate(CartDestination.route) },
                navigateToProduct = { navController.navigate(ProductDestination.route) },
                navigateToSearch = {navController.navigate(SearchDestination.route)},
                catalogScreenViewModel = catalogScreenViewModel
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
        composable(route = SearchDestination.route) {
            SearchResultScreen(
                navigateToProduct = { navController.navigate(ProductDestination.route) },
                navigateToCatalog = { navController.popBackStack(navController.graph.startDestinationId, false) },
                catalogScreenViewModel = catalogScreenViewModel,
            )
        }
    }
}