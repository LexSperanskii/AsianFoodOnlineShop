package com.example.asianfoodonlineshop.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.asianfoodonlineshop.ui.navigation.SushiShopNavHost

@Composable
fun AsianFoodOnlineShopApp(navController: NavHostController = rememberNavController()) {
    SushiShopNavHost(navController = navController)
}
