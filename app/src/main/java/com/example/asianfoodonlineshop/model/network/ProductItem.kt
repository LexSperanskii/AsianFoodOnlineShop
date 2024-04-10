package com.example.asianfoodonlineshop.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ProductModel(
    val carbohydratesPer100Grams: Double,
    val categoryId: Int,
    val description: String,
    val energyPer100Grams: Double,
    val fatsPer100Grams: Double,
    val id: Int,
    val image: String,
    val measure: Int,
    val measureUnit: String,
    val name: String,
    val priceCurrent: Int,
    val priceOld: Int?,
    val proteinsPer100Grams: Double,
    val tagIds: List<Int>
)
@Serializable
data class ProductItem(
    @SerialName(value = "carbohydrates_per_100_grams")
    val carbohydratesPer100Grams: Double,
    @SerialName(value = "category_id")
    val categoryId: Int,
    val description: String,
    @SerialName(value = "energy_per_100_grams")
    val energyPer100Grams: Double,
    @SerialName(value = "fats_per_100_grams")
    val fatsPer100Grams: Double,
    val id: Int,
    val image: String,
    val measure: Int,
    @SerialName(value = "measure_unit")
    val measureUnit: String,
    val name: String,
    @SerialName(value = "price_current")
    val priceCurrent: Int,
    @SerialName(value = "price_old")
    val priceOld: Int?,
    @SerialName(value = "proteins_per_100_grams")
    val proteinsPer100Grams: Double,
    @SerialName(value = "tag_ids")
    val tagIds: List<Int>
)