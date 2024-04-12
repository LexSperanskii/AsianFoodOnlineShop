package com.example.asianfoodonlineshop.ui.screens.cartScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.asianfoodonlineshop.data.db.UsersRepository
import com.example.asianfoodonlineshop.data.network.ProductsRepository
import com.example.asianfoodonlineshop.model.CommodityItem
import com.example.asianfoodonlineshop.model.db.CartModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CartScreenUiState(
    val cart: List<CartModel> = listOf(),
    val price: Int = 0
)
class CartScreenViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _cartScreenUiState = MutableStateFlow(CartScreenUiState())
    val cartScreenUiState : StateFlow<CartScreenUiState> = _cartScreenUiState
        .combine(usersRepository.getAllCartItems()) { localState, cart ->
            localState.copy(
                cart = cart,
                price = cart.sumOf { it.priceCurrent * it.quantity }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000), //Преобразование будет запущено, когда есть хотя бы один подписчик на StateFlow, и будет продолжаться до тех пор, пока есть подписчики. Время жизни этой операции составляет 5 секунд (5000 миллисекунд
            initialValue = CartScreenUiState()
        )
    fun increaseQuantity(item: CartModel) {
        viewModelScope.launch {
            usersRepository.insertToCart(item.copy(quantity = item.quantity +1 ))
        }
    }
    fun decreaseQuantity(item: CartModel) {
        viewModelScope.launch {
            if (item.quantity == 1){
                usersRepository.deleteFromCart(item.id)
            }else{
                usersRepository.insertToCart(item.copy(quantity = item.quantity -1 ))
            }
        }
    }
}