package com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sushishop.R
import com.example.asianfoodonlineshop.data.db.UsersRepository
import com.example.asianfoodonlineshop.data.network.ProductsRepository
import com.example.asianfoodonlineshop.model.CommodityItem
import com.example.asianfoodonlineshop.model.network.AttributesItemModel
import com.example.asianfoodonlineshop.model.network.CategoriesItemModel
import com.example.asianfoodonlineshop.model.network.ProductModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface CatalogScreenNetworkUiState {
    object Success : CatalogScreenNetworkUiState
    object Error : CatalogScreenNetworkUiState
    object Loading : CatalogScreenNetworkUiState
}
data class CatalogScreenUiState(
    val listOfProductsOriginal: List<CommodityItem> = listOf(),
    val listOfProducts: List<CommodityItem> = listOf(),
    val listOfCategories: List<CategoriesItemModel> = listOf(),
    val currentCategory : CategoriesItemModel = CategoriesItemModel(0,""),
    val listOfAttributes: List<AttributesItemModel> = listOf(),
    val listOfChosenAttributes: List<AttributesItemModel> = listOf(),
    val price: Int = 0
)
data class ProductScreenUiState(
    val commodityItem: CommodityItem = CommodityItem(
        ProductModel(0.0,0,"",0.0,0.0,
            0,"",0,"","",0,0,0.0, listOf()
        ),
        0,
        0
    )
)

class CatalogProductScreenViewModel(
    private val usersRepository: UsersRepository,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    /**
     * State для состояния Сети
     */
    var catalogScreenNetworkUiState: CatalogScreenNetworkUiState by mutableStateOf(
        CatalogScreenNetworkUiState.Loading
    )
        private set

    /**
     * State для экарана с товарами
     */
    private val _catalogScreenUiState = MutableStateFlow(CatalogScreenUiState())
    val catalogScreenUiState: StateFlow<CatalogScreenUiState> = _catalogScreenUiState
        .combine(usersRepository.getAllCartItems()) { localState, cart ->
            localState.copy(
                listOfProductsOriginal = localState.listOfProductsOriginal.map { item ->
                    val cartItem = cart.find { it.id == item.productItem.id }
                    if (cartItem != null) {
                        // Найден элемент в корзине с таким же id, устанавливаем quantity
                        item.copy(quantity = cartItem.quantity)
                    } else {
                        // Не найден элемент в корзине, оставляем quantity без изменений
                        item
                    }
                },
                listOfProducts = localState.listOfProducts.map { item ->
                    val cartItem = cart.find { it.id == item.productItem.id }
                    if (cartItem != null) {
                        // Найден элемент в корзине с таким же id, устанавливаем quantity
                        item.copy(quantity = cartItem.quantity)
                    } else {
                        // Не найден элемент в корзине, оставляем quantity без изменений
                        item
                    }
                },
                price = cart.sumOf { it.priceCurrent * it.quantity}
            )
        } //для преобразования Flow в StateFlow.
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000), //Преобразование будет запущено, когда есть хотя бы один подписчик на StateFlow, и будет продолжаться до тех пор, пока есть подписчики. Время жизни этой операции составляет 5 секунд (5000 миллисекунд
            initialValue = CatalogScreenUiState()
        )

    /**
     * State для выбранного товара
     */
    private val _productScreenUiState = MutableStateFlow(ProductScreenUiState())
    val productScreenUiState: StateFlow<ProductScreenUiState> = _productScreenUiState
        .combine(usersRepository.getAllCartItems()) { localState, cart ->
            val cartItem = cart.find { it.id == localState.commodityItem.productItem.id }
            if (cartItem != null) {
                // Найден элемент в корзине с таким же id, устанавливаем quantity
                localState.copy(commodityItem = localState.commodityItem.copy(quantity = cartItem.quantity) )
            } else {
                // Не найден элемент в корзине, оставляем quantity без изменений
                localState
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProductScreenUiState()
        )

    init {
        getCommodityItemsInfo()
    }
    /**
     * Gets Items information from the API Retrofit
     */
    fun getCommodityItemsInfo() {
        viewModelScope.launch {
            catalogScreenNetworkUiState = try {
                //получаем List с описанием товарных элементов из сети
                val listOfProductsItems : List<ProductModel> = productsRepository.getProductsItems()
                val listOfCategories : List<CategoriesItemModel> = productsRepository.getCategoriesItems()
                val listOfAttributes : List<AttributesItemModel> = productsRepository.getAttributesItem()
                //Создаем List Продуктов в котором будут вся инфа из сети, картика-заглушка и количество товара
                val listOfProducts = mutableListOf<CommodityItem>()
                //Совединяем все в один List
                for (i in listOfProductsItems.indices) {
                    val item = listOfProductsItems[i]
                    listOfProducts.add(CommodityItem(productItem = item, image = R.drawable.commodity_image, quantity = 0))
                }
                _catalogScreenUiState.update {
                    it.copy(
                        listOfProductsOriginal = listOfProducts,
                        listOfProducts = listOfProducts,
                        listOfCategories = listOfCategories,
                        currentCategory = listOfCategories.first(),
                        listOfAttributes = listOfAttributes
                    )
                }
                //фильтруем по дефолтным значениям
                val currentState = catalogScreenUiState.value
                sortCategoryItems(currentState.currentCategory)
                sortAttributesItems(currentState.listOfChosenAttributes)
                //состояние загрузки
                CatalogScreenNetworkUiState.Success
            } catch (e: IOException) {
                CatalogScreenNetworkUiState.Error
            }
        }
    }
//    fun saveOrDeleteFromFavorites(commodityItem: CommodityItem){
//        viewModelScope.launch {
//            if (commodityItem.isFavourite){
//                //Удаляем из БД
//                usersRepository.deleteFromFavorites(commodityItem.productDescription.id)
//            }else{
//                // Записываем в БД
//                usersRepository.insertInFavorite(commodityItem.productDescription.id)
//            }
//        }
//    }
    fun chooseCommodityItem(item: CommodityItem) {
        _productScreenUiState.update {
            it.copy(
                commodityItem = item
            )
        }
    }
    fun onAttributeItemClick(attributes: AttributesItemModel){
        val listOfChosenAttributes = catalogScreenUiState.value.listOfChosenAttributes
        val listOfNewChosenAttributes = mutableListOf<AttributesItemModel>()
        listOfNewChosenAttributes.addAll(listOfChosenAttributes)
        listOfNewChosenAttributes.add(attributes)
        _catalogScreenUiState.update {
            it.copy(
                listOfChosenAttributes = listOfNewChosenAttributes
            )
        }
        sortAttributesItems(listOfNewChosenAttributes)
    }
    fun onCategoryClick(category: CategoriesItemModel){
        _catalogScreenUiState.update {
            it.copy(
                currentCategory = category
            )
        }
        sortCategoryItems(category)
    }
    private fun sortCategoryItems(category : CategoriesItemModel){
        val currentState = catalogScreenUiState.value
        _catalogScreenUiState.update { it ->
            it.copy(
                listOfProducts = it.listOfProductsOriginal.filter { it.productItem.categoryId == category.id }
            )
        }
        sortAttributesItems(currentState.listOfChosenAttributes)
    }
    private fun sortAttributesItems(sortType: List<AttributesItemModel>) {
        _catalogScreenUiState.update { it ->
            it.copy(
                listOfProducts = it.listOfProducts.filter { productItem ->
                    // Проверяем, что все элементы из sortType содержатся в productItem.tagIds
                    sortType.all { attributeItem ->
                        productItem.productItem.tagIds.contains(attributeItem.id)
                    }
                }
            )
        }
    }
}