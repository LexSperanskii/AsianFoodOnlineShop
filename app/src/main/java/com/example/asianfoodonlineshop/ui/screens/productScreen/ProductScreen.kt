package com.example.asianfoodonlineshop.ui.screens.productScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.asianfoodonlineshop.R
import com.example.asianfoodonlineshop.model.CommodityItem
import com.example.asianfoodonlineshop.ui.AppViewModelProvider
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.ErrorScreen
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.LoadingScreen
import com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen.priceFormat

@Composable
fun ProductScreen(
    navigateToCartButton: () -> Unit,
    navigateToCatalogButton: () -> Unit,
    modifier: Modifier = Modifier,
    productScreenViewModel: ProductScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {

    val productScreenUiState = productScreenViewModel.productScreenUiState.collectAsState().value
    val productScreenNetworkUiState = productScreenViewModel.productScreenNetworkUiState

    Scaffold(
        topBar = {},
        bottomBar = {
            AddToCartProductScreenButton(
                quantity = productScreenUiState.commodityItem.quantity,
                price = priceFormat(productScreenUiState.commodityItem.productItem.priceCurrent),
                addToCartButton = {
                    productScreenViewModel.addToCartFromProductScreen(
                        productScreenUiState.commodityItem
                    )
                },
                navigateToCartButton = navigateToCartButton
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (productScreenNetworkUiState) {
                is ProductScreenNetworkUiState.Loading ->
                    LoadingScreen(modifier = Modifier.fillMaxSize())

                is ProductScreenNetworkUiState.Success ->
                    ProductScreenBody(
                        commodityItem = productScreenUiState.commodityItem,
                        navigateToCatalogButton = navigateToCatalogButton
                    )

                is ProductScreenNetworkUiState.Error ->
                    ErrorScreen(
                        retryAction = { productScreenViewModel.getCommodityItem() },
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }
    }
}

@Composable
fun ProductScreenBody(
    commodityItem : CommodityItem,
    navigateToCatalogButton: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(enabled = true, state = rememberScrollState())
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.wrapContentHeight()
        ) {
            Box(
                modifier = Modifier
                    .background(colorResource(id = R.color.white))
                    .aspectRatio(1f)
            ) {
                IconButton(
                    onClick = navigateToCatalogButton,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.size_16))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(id = R.string.catalog),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_24))
                    )
                }
                Image(
                    painter = painterResource(id = commodityItem.image),
                    contentDescription = stringResource(R.string.product_image),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = commodityItem.productItem.name,
                style = TextStyle(
                    fontWeight = FontWeight(integerResource(id = R.integer.weight_400)),
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    fontSize = dimensionResource(id = R.dimen.text_size_34).value.sp,
                    color = colorResource(id = R.color.black),
                ),
                modifier = Modifier.padding(
                    top = dimensionResource(id = R.dimen.size_24),
                    start = dimensionResource(id = R.dimen.size_16),
                    end = dimensionResource(id = R.dimen.size_16),
                    bottom =dimensionResource(id = R.dimen.size_8)
                )
            )
            Text(
                text = commodityItem.productItem.description,
                style = TextStyle(
                    fontWeight = FontWeight(integerResource(id = R.integer.weight_400)),
                    fontFamily = FontFamily(Font(R.font.roboto_regular)),
                    fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                    color = colorResource(id = R.color.dark_gray),
                ),
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.size_16),
                    end = dimensionResource(id = R.dimen.size_16),
                    bottom =dimensionResource(id = R.dimen.size_24)
                )
            )
            CompoundInfo(
                title = stringResource(id = R.string.weight),
                description = stringResource(id = R.string.measure, commodityItem.productItem.measure, commodityItem.productItem.measureUnit )
            )
            CompoundInfo(
                title = stringResource(id = R.string.energy_value),
                description = stringResource(id = R.string.energy_value_measure, measureFormat(commodityItem.productItem.energyPer100Grams))
            )
            CompoundInfo(
                title = stringResource(id = R.string.protein),
                description = stringResource(id = R.string.measure_for_double, measureFormat(commodityItem.productItem.proteinsPer100Grams), commodityItem.productItem.measureUnit )
            )
            CompoundInfo(
                title = stringResource(id = R.string.fats),
                description = stringResource(id = R.string.measure_for_double, measureFormat(commodityItem.productItem.fatsPer100Grams), commodityItem.productItem.measureUnit )
            )
            CompoundInfo(
                title = stringResource(id = R.string.carbs),
                description = stringResource(id = R.string.measure_for_double, measureFormat(commodityItem.productItem.carbohydratesPer100Grams), commodityItem.productItem.measureUnit )
            )
            HorizontalDivider(
                color = colorResource(id = R.color.dark_gray),
                thickness = dimensionResource(R.dimen.size_1)
            )
        }
    }
}

@Composable
fun CompoundInfo(
    title: String,
    description: String,
    modifier: Modifier = Modifier
    ){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.size_50))
    ) {
        Column() {
            HorizontalDivider(
                color = colorResource(id = R.color.dark_gray),
                thickness = dimensionResource(R.dimen.size_1)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.size_16),
                        vertical = dimensionResource(id = R.dimen.size_13),
                    )
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_400)),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                        color = colorResource(id = R.color.dark_gray),
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = description,
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_400)),
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                        color = colorResource(id = R.color.black),
                    )
                )
            }
        }
    }
}

@Composable
fun AddToCartProductScreenButton(
    quantity:Int,
    price: String,
    navigateToCartButton: () -> Unit,
    addToCartButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(
        vertical = dimensionResource(id = R.dimen.size_12),
        horizontal = dimensionResource(id = R.dimen.size_16)
    )) {
        if (quantity == 0){
            Box(
                contentAlignment = Alignment.Center ,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(colorResource(id = R.color.orange))
                    .height(dimensionResource(R.dimen.size_48))
                    .fillMaxWidth()
                    .clickable { addToCartButton() }
            ){
                Text(
                    text = stringResource(R.string.add_to_cart_for, price),
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                        color = colorResource(id = R.color.white),
                    )
                )
            }
        }else{
            Box(
                contentAlignment = Alignment.Center ,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(colorResource(id = R.color.orange))
                    .height(dimensionResource(R.dimen.size_48))
                    .fillMaxWidth()
                    .clickable { navigateToCartButton() }
            ){
                Text(
                    text = stringResource(R.string.navigate_to_cart_from_product, price),
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                        color = colorResource(id = R.color.white),
                    )
                )
            }
        }
    }
}

fun measureFormat(price: Double): String {
    return if (price % 1 == 0.00)
        price.toInt().toString()
    else
       String.format("%.1f",price)
}