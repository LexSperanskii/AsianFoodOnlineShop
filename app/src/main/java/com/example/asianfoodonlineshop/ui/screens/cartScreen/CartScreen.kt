package com.example.asianfoodonlineshop.ui.screens.cartScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sushishop.R
import com.example.asianfoodonlineshop.model.db.CartModel
import com.example.asianfoodonlineshop.ui.AppViewModelProvider
import com.example.asianfoodonlineshop.ui.screens.TopAppBarBackAndName

@Composable
fun CartScreen(
    currentDestinationTitle: Int,
    onClickNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    cartScreenViewModel: CartScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val cartScreenViewModel = cartScreenViewModel.cartScreenUiState.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBarBackAndName(
                currentDestinationTitle = currentDestinationTitle,
                onClickNavigateBack = onClickNavigateBack,
            )
        },
        bottomBar = {}
    ) { innerPadding ->
        if (cartScreenViewModel.cart.isEmpty()){
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(
                    text = stringResource(id = R.string.cart_stub),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }else{
            CartScreenBody(
                cartItems = cartScreenViewModel.cart,
                onIncreaseQuantityClick = {},
                onDecreaseQuantityClick = {},
                price = cartScreenViewModel.price,
                navigateToPlaceOrder = {}
            )
        }
    }
}

@Composable
fun CartScreenBody(
    cartItems : List<CartModel>,
    onIncreaseQuantityClick: () -> Unit,
    onDecreaseQuantityClick: () -> Unit,
    price: Int,
    navigateToPlaceOrder: () -> Unit,
    modifier : Modifier = Modifier
){
    Column(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(dimensionResource(R.dimen.size_12))
        ) {
            items(items = cartItems) { cartItem ->
                CartElement(
                    cartElement = cartItem,
                    onIncreaseQuantityClick = onIncreaseQuantityClick,
                    onDecreaseQuantityClick = onDecreaseQuantityClick,
                )
            }
        }
        PlaceOrderButton(
            price = price,
            navigateToPlaceOrder = navigateToPlaceOrder,
        )
    }
}
@Composable
fun CartElement(
    cartElement: CartModel,
    onIncreaseQuantityClick: () -> Unit,
    onDecreaseQuantityClick: () -> Unit,
    modifier : Modifier = Modifier
){
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = cartElement.image),
                contentDescription = stringResource(R.string.product_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = cartElement.name,
                    style = TextStyle(
                        fontSize = dimensionResource(id = R.dimen.text_size_14).value.sp,
                        color = colorResource(id = R.color.black),
                    )
                )
                Row(
                    modifier = Modifier
                ) {
                    QuantityButtonForCart(
                        cartElement = cartElement,
                        onIncreaseQuantityClick = onIncreaseQuantityClick,
                        onDecreaseQuantityClick = onDecreaseQuantityClick,
                    )
                    if (cartElement.priceOld!=null){
                        Column {
                            Text(
                                text = stringResource(id = R.string.price, cartElement.priceCurrent),
                                style = TextStyle(
                                    fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                                    color = colorResource(id = R.color.black),
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.price, cartElement.priceOld),
                                style = TextStyle(
                                    fontSize = dimensionResource(id = R.dimen.text_size_14).value.sp,
                                    color = colorResource(id = R.color.dark_gray),
                                )
                            )
                        }
                    }else{
                        Text(
                            text = stringResource(id = R.string.price, cartElement.priceCurrent),
                            style = TextStyle(
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
            thickness = dimensionResource(R.dimen.size_1),
            modifier = Modifier
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
    Column(modifier = modifier) {
        Box(
            contentAlignment = Alignment.Center ,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(colorResource(id = R.color.white))
                .height(dimensionResource(R.dimen.size_44))
                .fillMaxWidth()
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.size_16))
            ) {
                Box(
                    contentAlignment = Alignment.Center ,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(colorResource(id = R.color.white))
                        .size(dimensionResource(R.dimen.size_40))
                        .clickable { onIncreaseQuantityClick() }
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_minus) ,
                        contentDescription = stringResource(id = R.string.add_to_cart),
                        tint = colorResource(id = R.color.orange),
                        modifier = Modifier
                            .width(dimensionResource(R.dimen.size_16))
                            .height(dimensionResource(R.dimen.size_2))
                    )
                }
                Text(
                    text = cartElement.quantity.toString(),
                    style = TextStyle(
                        fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                        color = colorResource(id = R.color.black),
                    )
                )
                Box(
                    contentAlignment = Alignment.Center ,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(colorResource(id = R.color.white))
                        .size(dimensionResource(R.dimen.size_40))
                        .clickable { onDecreaseQuantityClick() }
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus) ,
                        contentDescription = stringResource(id = R.string.add_to_cart),
                        tint = colorResource(id = R.color.orange),
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.size_16))
                    )
                }
            }
        }
    }
}

@Composable
fun PlaceOrderButton(
    price: Int,
    navigateToPlaceOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            contentAlignment = Alignment.Center ,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(colorResource(id = R.color.orange))
                .height(dimensionResource(R.dimen.size_48))
                .fillMaxWidth()
                .clickable { navigateToPlaceOrder() }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.size_16))
            ) {
                Text(
                    text = stringResource(R.string.place_order, price),
                    style = TextStyle(
                        fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                        color = colorResource(id = R.color.white),
                    ),
                    modifier = Modifier
                )
            }
        }
    }
}
