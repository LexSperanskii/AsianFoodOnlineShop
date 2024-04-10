package com.example.asianfoodonlineshop.data.network

import com.example.asianfoodonlineshop.model.network.AttributesItem
import com.example.asianfoodonlineshop.model.network.CategoriesItem
import com.example.asianfoodonlineshop.model.network.ProductItem
import retrofit2.http.GET
interface OnlineShopApiService {
    @GET("WorkTestServer/Products.json")
    suspend fun getProductsItems(): List<ProductItem>

    @GET("WorkTestServer/Categories.json")
    suspend fun getCategoriesItems(): List<CategoriesItem>

    @GET("WorkTestServer/Tags.json")
    suspend fun getAttributesItem(): List<AttributesItem>
}