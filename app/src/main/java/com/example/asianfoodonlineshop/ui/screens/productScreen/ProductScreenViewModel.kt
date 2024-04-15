package com.example.asianfoodonlineshop.ui.screens.productScreen

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
import com.example.asianfoodonlineshop.model.network.ProductModel
import com.example.asianfoodonlineshop.model.toCartModel
import com.example.asianfoodonlineshop.ui.screens.SharedViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface ProductScreenNetworkUiState {
    object Success : ProductScreenNetworkUiState
    object Error : ProductScreenNetworkUiState
    object Loading : ProductScreenNetworkUiState
}
data class ProductScreenUiState(
    val commodityItem: CommodityItem = CommodityItem(
        ProductModel(0.0,0,"",0.0,0.0,
            0,"",0,"","",0,0,0.0, listOf()
        ),
        0,
        0
    )
)
class ProductScreenViewModel(
    private val usersRepository: UsersRepository,
    private val productsRepository: ProductsRepository,
    private val sharedViewModel: SharedViewModel // Передача общего значения для id
) : ViewModel() {

    /**
     * State для состояния Сети
     */
    var productScreenNetworkUiState: ProductScreenNetworkUiState by mutableStateOf(
        ProductScreenNetworkUiState.Loading
    )
        private set

    /**
     * State для выбранного товара
     */
    private val _productScreenUiState = MutableStateFlow(ProductScreenUiState())
    val productScreenUiState: StateFlow<ProductScreenUiState> = _productScreenUiState.asStateFlow()

    init {
        getCommodityItem()
    }
    /**
     * Gets Items information from the API Retrofit
     */
    fun getCommodityItem() {
        viewModelScope.launch {
            productScreenNetworkUiState = try {
                //получаем List с описанием товарных элементов из сети
                val listOfProductsItems : List<ProductModel> = productsRepository.getProductsItems()
                val productItemFromDB : CartModel? = usersRepository.getItem(sharedViewModel.sharedViewModelUiState.value.id)
                _productScreenUiState.update {it ->
                    it.copy(
                        commodityItem = it.commodityItem.copy(
                            productItem = listOfProductsItems.find { it.id == sharedViewModel.sharedViewModelUiState.value.id } ?: it.commodityItem.productItem,
                            image = productItemFromDB?.image ?: R.drawable.commodity_image,
                            quantity = productItemFromDB?.quantity ?: 0
                        )
                    )
                }
                //состояние загрузки
                ProductScreenNetworkUiState.Success
            } catch (e: IOException) {
                ProductScreenNetworkUiState.Error
            }
        }
    }

    fun addToCartFromProductScreen(commodityItem: CommodityItem){
        viewModelScope.launch {
            usersRepository.insertToCart(commodityItem.copy(quantity = commodityItem.quantity +1 ).toCartModel())
        }
        _productScreenUiState.update {
            it.copy(
                commodityItem = it.commodityItem.copy(quantity = it.commodityItem.quantity+1)
            )
        }
    }
}
