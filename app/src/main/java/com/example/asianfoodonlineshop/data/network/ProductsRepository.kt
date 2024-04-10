package com.example.asianfoodonlineshop.data.network

import com.example.asianfoodonlineshop.model.network.AttributesItem
import com.example.asianfoodonlineshop.model.network.AttributesItemModel
import com.example.asianfoodonlineshop.model.network.CategoriesItem
import com.example.asianfoodonlineshop.model.network.CategoriesItemModel
import com.example.asianfoodonlineshop.model.network.ProductItem
import com.example.asianfoodonlineshop.model.network.ProductModel
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


interface ProductsRepository {
    suspend fun getProductsItems(): List<ProductModel>
    suspend fun getCategoriesItems(): List<CategoriesItemModel>
    suspend fun getAttributesItem(): List<AttributesItemModel>
}

class NetworkProductsRepository() : ProductsRepository {

    private val baseUrl = "https://anika1d.github.io"
    /**
     * Use the Retrofit builder to build a retrofit object using a kotlinx.serialization converter
     */
    private val json = Json { ignoreUnknownKeys = true } //Если много ненужных ключей
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService : OnlineShopApiService by lazy {
        retrofit.create(OnlineShopApiService::class.java)
    }
    override suspend fun getProductsItems(): List<ProductModel> = retrofitService.getProductsItems().map { it.toProductModel() }

    override suspend fun getCategoriesItems(): List<CategoriesItemModel> = retrofitService.getCategoriesItems().map { it.toCategoriesItemModel() }

    override suspend fun getAttributesItem(): List<AttributesItemModel> = retrofitService.getAttributesItem().map { it.toAttributesItemModel() }

    //extension functions
    private fun ProductItem.toProductModel(): ProductModel {
        return ProductModel(
            carbohydratesPer100Grams,
            categoryId,
            description,
            energyPer100Grams,
            fatsPer100Grams,
            id,
            image,
            measure,
            measureUnit,
            name,
            priceCurrent,
            priceOld,
            proteinsPer100Grams,
            tagIds
        )
    }
    private fun CategoriesItem.toCategoriesItemModel(): CategoriesItemModel {
        return CategoriesItemModel(id, name)
    }
    private fun AttributesItem.toAttributesItemModel(): AttributesItemModel {
        return AttributesItemModel(id, name)
    }
}