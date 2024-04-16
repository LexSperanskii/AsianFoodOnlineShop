package com.example.asianfoodonlineshop.ui.navigation

import com.example.asianfoodonlineshop.R


interface NavigationDestination {
    val route: String
    val title: Int
}
object CatalogDestination : NavigationDestination {
    override val route = "catalog"
    override val title = R.string.catalog
}
object ProductDestination : NavigationDestination {
    override val route = "product"
    override val title = R.string.product
}
object CartDestination : NavigationDestination {
    override val route = "cart"
    override val title = R.string.cart
}

object SearchDestination : NavigationDestination {
    override val route = "search"
    override val title = R.string.search
}

