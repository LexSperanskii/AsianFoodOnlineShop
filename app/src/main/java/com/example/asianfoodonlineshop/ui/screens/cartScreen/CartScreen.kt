package com.example.asianfoodonlineshop.ui.screens.cartScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asianfoodonlineshop.R
import com.example.asianfoodonlineshop.model.db.CartModel
import com.example.asianfoodonlineshop.ui.AppViewModelProvider
import com.example.asianfoodonlineshop.ui.screens.TopAppBarBackAndName
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.priceFormat

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
        bottomBar = {
            PlaceOrderButton(
                price = cartScreenViewModel.price,
                navigateToPlaceOrder = {}
            )
        }
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
                onDecreaseQuantityClick = {}
            )
        }
    }
}

@Composable
fun CartScreenBody(
    cartItems : List<CartModel>,
    onIncreaseQuantityClick: () -> Unit,
    onDecreaseQuantityClick: () -> Unit,
    modifier : Modifier = Modifier
){
    LazyColumn(
        contentPadding = PaddingValues( vertical = dimensionResource(R.dimen.size_12)),
        modifier = modifier.fillMaxWidth()
    ) {
        items(items = cartItems) { cartItem ->
            CartElement(
                cartElement = cartItem,
                onIncreaseQuantityClick = onIncreaseQuantityClick,
                onDecreaseQuantityClick = onDecreaseQuantityClick,
            )
        }
    }
}
@Composable
fun CartElement(
    cartElement: CartModel,
    onIncreaseQuantityClick: () -> Unit,
    onDecreaseQuantityClick: () -> Unit,
    modifier : Modifier = Modifier
){
    Column( modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.size_128))
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
                                    fontWeight = FontWeight.Bold,
                                    fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                                    color = colorResource(id = R.color.black),
                                ),
                                modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.size_2))
                            )
                            Text(
                                text = stringResource(id = R.string.price, priceFormat(cartElement.priceOld)),
                                style = TextStyle(
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
                                fontWeight = FontWeight.Bold,
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
                fontWeight = FontWeight.Bold,
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
