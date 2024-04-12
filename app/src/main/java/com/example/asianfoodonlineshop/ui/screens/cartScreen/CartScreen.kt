package com.example.asianfoodonlineshop.ui.screens.cartScreen

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asianfoodonlineshop.R
import com.example.asianfoodonlineshop.model.db.CartModel
import com.example.asianfoodonlineshop.ui.AppViewModelProvider
import com.example.asianfoodonlineshop.ui.screens.TopAppBarBackAndName
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.priceFormat
import kotlinx.coroutines.launch

@Composable
fun CartScreen(
    currentDestinationTitle: Int,
    onClickNavigateBack: () -> Unit,
    navigateToProduct: () -> Unit,
    cartScreenViewModel: CartScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val cartUiState = cartScreenViewModel.cartScreenUiState.collectAsState().value
    //Для вызова снекбара после нажатия оформить заказ
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBarBackAndName(
                currentDestinationTitle = currentDestinationTitle,
                onClickNavigateBack = onClickNavigateBack,
            )
        },
        bottomBar = {
            PlaceOrderButton(
                price = cartUiState.price,
                navigateToPlaceOrder = {
                    scope.launch {
                        snackbarHostState.showSnackbar(context.getString(R.string.send_order))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (cartUiState.cart.isEmpty()){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(id = R.string.cart_stub),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }else{
                CartScreenBody(
                    cartItems = cartUiState.cart,
                    onIncreaseQuantityClick = {cartScreenViewModel.increaseQuantity(it)},
                    onDecreaseQuantityClick = {cartScreenViewModel.decreaseQuantity(it)},
                    navigateToProduct = navigateToProduct
                )
            }
        }
    }
}

@Composable
fun CartScreenBody(
    cartItems : List<CartModel>,
    onIncreaseQuantityClick: (CartModel) -> Unit,
    onDecreaseQuantityClick: (CartModel) -> Unit,
    navigateToProduct: () -> Unit,
    modifier : Modifier = Modifier
){
    LazyColumn(
        contentPadding = PaddingValues( vertical = dimensionResource(R.dimen.size_12)),
        modifier = modifier.fillMaxWidth()
    ) {
        items(items = cartItems) { cartItem ->
            CartElement(
                cartElement = cartItem,
                onIncreaseQuantityClick = {onIncreaseQuantityClick(cartItem)},
                onDecreaseQuantityClick = {onDecreaseQuantityClick(cartItem)},
                navigateToProduct = navigateToProduct
            )
        }
    }
}
@Composable
fun CartElement(
    cartElement: CartModel,
    onIncreaseQuantityClick: () -> Unit,
    onDecreaseQuantityClick: () -> Unit,
    navigateToProduct: () -> Unit,
    modifier : Modifier = Modifier
){
    Column( modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_128))
                .clickable { navigateToProduct() }
        ) {
            Image(
                painter = painterResource(id = cartElement.image),
                contentDescription = stringResource(R.string.product_image),
                modifier = Modifier
                    .padding(
                        start = dimensionResource(id = R.dimen.size_16),
                        top = dimensionResource(id = R.dimen.size_16),
                        bottom = dimensionResource(id = R.dimen.size_16)
                    )
                    .size(dimensionResource(id = R.dimen.size_96)),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.size_16))
            ) {
                Text(
                    text = cartElement.name,
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_400)),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = dimensionResource(id = R.dimen.text_size_14).value.sp,
                        color = colorResource(id = R.color.black),
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    QuantityButtonForCart(
                        cartElement = cartElement,
                        onIncreaseQuantityClick = onIncreaseQuantityClick,
                        onDecreaseQuantityClick = onDecreaseQuantityClick,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (cartElement.priceOld!=null){
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = stringResource(id = R.string.price, priceFormat(cartElement.priceCurrent)),
                                style = TextStyle(
                                    fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                    fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                                    color = colorResource(id = R.color.black),
                                ),
                                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.size_2))
                            )
                            Text(
                                text = stringResource(id = R.string.price, priceFormat(cartElement.priceOld)),
                                style = TextStyle(
                                    fontWeight = FontWeight(integerResource(id = R.integer.weight_400)),
                                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                                    fontSize = dimensionResource(id = R.dimen.text_size_14).value.sp,
                                    color = colorResource(id = R.color.dark_gray),
                                ),
                                textDecoration = TextDecoration.LineThrough
                            )
                        }
                    }else{
                        Text(
                            text = stringResource(id = R.string.price, priceFormat(cartElement.priceCurrent)),
                            style = TextStyle(
                                fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                                fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                                color = colorResource(id = R.color.black),
                            )
                        )
                    }
                }
            }
        }
        HorizontalDivider(
            color = colorResource(id = R.color.dark_gray),
            thickness = dimensionResource(R.dimen.size_1)
        )
    }
}

@Composable
fun QuantityButtonForCart(
    cartElement: CartModel,
    onIncreaseQuantityClick: () -> Unit,
    onDecreaseQuantityClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center ,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(colorResource(id = R.color.gray))
                .size(dimensionResource(R.dimen.size_44))
                .clickable { onDecreaseQuantityClick() }
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_minus) ,
                contentDescription = stringResource(id = R.string.add_to_cart),
                tint = colorResource(id = R.color.orange)
            )
        }
        Text(
            text = cartElement.quantity.toString(),
            style = TextStyle(
                fontWeight = FontWeight(integerResource(R.integer.weight_500)),
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                color = colorResource(id = R.color.black),
            ),
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.size_22))
        )
        Box(
            contentAlignment = Alignment.Center ,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(colorResource(id = R.color.gray))
                .size(dimensionResource(R.dimen.size_44))
                .clickable { onIncreaseQuantityClick() }
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_plus) ,
                contentDescription = stringResource(id = R.string.add_to_cart),
                tint = colorResource(id = R.color.orange)
            )
        }
    }
}

@Composable
fun PlaceOrderButton(
    price: Int,
    navigateToPlaceOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(
        vertical = dimensionResource(id = R.dimen.size_12),
        horizontal = dimensionResource(id = R.dimen.size_16)
    )) {
        Box(
            contentAlignment = Alignment.Center ,
            modifier = Modifier
                .padding(
                    vertical = dimensionResource(id = R.dimen.size_12),
                    horizontal = dimensionResource(id = R.dimen.size_16)
                )
                .clip(MaterialTheme.shapes.small)
                .background(colorResource(id = R.color.orange))
                .height(dimensionResource(R.dimen.size_48))
                .fillMaxWidth()
                .clickable { navigateToPlaceOrder() }
        ){
            Text(
                text = stringResource(R.string.place_order, priceFormat(price)),
                style = TextStyle(
                    fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                    color = colorResource(id = R.color.white),
                )
            )
        }
    }
}
