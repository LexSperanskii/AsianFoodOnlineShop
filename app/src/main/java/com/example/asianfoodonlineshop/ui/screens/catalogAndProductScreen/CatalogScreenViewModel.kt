package com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asianfoodonlineshop.R
import com.example.asianfoodonlineshop.data.db.UsersRepository
import com.example.asianfoodonlineshop.data.network.ProductsRepository
import com.example.asianfoodonlineshop.model.CommodityItem
import com.example.asianfoodonlineshop.model.db.CartModel
import com.example.asianfoodonlineshop.model.network.AttributesItemModel
import com.example.asianfoodonlineshop.model.network.CategoriesItemModel
import com.example.asianfoodonlineshop.model.network.ProductModel
import com.example.asianfoodonlineshop.ui.screens.SharedViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val listOfProductsForAttributes: List<CommodityItem> = listOf(),
    val listOfCategories: List<CategoriesItemModel> = listOf(),
    val currentCategory : CategoriesItemModel = CategoriesItemModel(0,""),
    val listOfAttributes: List<AttributesItemModel> = listOf(),
    val listOfChosenAttributes: List<AttributesItemModel> = listOf(),
    val price: Int = 0
)


class CatalogProductScreenViewModel(
    private val usersRepository: UsersRepository,
    private val productsRepository: ProductsRepository,
    private val sharedViewModel: SharedViewModel // Передача общего значения для id
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

    fun increaseQuantity(commodityItem: CommodityItem) {
        viewModelScope.launch {
            usersRepository.insertToCart(commodityItem.copy(quantity = commodityItem.quantity +1 ).toCartModel())
        }
    }
    fun decreaseQuantity(commodityItem: CommodityItem) {
        viewModelScope.launch {
            if (commodityItem.quantity == 1){
                usersRepository.deleteFromCart(commodityItem.productItem.id)
            }else{
                usersRepository.insertToCart(commodityItem.copy(quantity = commodityItem.quantity -1 ).toCartModel())
            }
        }
    }
    fun chooseCommodityItem(item: CommodityItem){
        sharedViewModel.chooseCommodityItem(item.productItem.id)
    }
    fun onAttributeItemClick(attribute: AttributesItemModel, isChecked: Boolean){
        _catalogScreenUiState.update { currentState ->
            if (isChecked){
                // Добавить элемент к списку
                val updatedList = currentState.listOfChosenAttributes.toMutableList().apply {
                    add(attribute)
                }
                currentState.copy(
                    listOfChosenAttributes = updatedList
                )
            }else{
                // Удалить элемент из списка
                val updatedList = currentState.listOfChosenAttributes.toMutableList().apply {
                    remove(attribute)
                }
                currentState.copy(
                    listOfChosenAttributes = updatedList
                )
            }
        }
        //Фильтруем
        sortAttributesItems(catalogScreenUiState.value.listOfChosenAttributes)
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
                listOfProducts = it.listOfProductsOriginal.filter { it.productItem.categoryId == category.id },
                listOfProductsForAttributes = it.listOfProductsOriginal.filter { it.productItem.categoryId == category.id }
            )
        }
        sortAttributesItems(currentState.listOfChosenAttributes)
    }
    private fun sortAttributesItems(sortType: List<AttributesItemModel>) {
        _catalogScreenUiState.update { it ->
            it.copy(
                listOfProducts = it.listOfProductsForAttributes.filter { productItem ->
                    // Проверяем, что все элементы из sortType содержатся в productItem.tagIds
                    sortType.all { attributeItem ->
                        productItem.productItem.tagIds.contains(attributeItem.id)
                    }
                }
            )
        }
    }
}

fun CommodityItem.toCartModel(): CartModel {
    return CartModel(productItem.id, productItem.name, productItem.priceCurrent, productItem.priceOld, image, quantity)
}