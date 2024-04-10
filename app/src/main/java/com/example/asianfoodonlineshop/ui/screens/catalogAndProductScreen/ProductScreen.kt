package com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import com.example.sushishop.R
import com.example.asianfoodonlineshop.model.CommodityItem

@Composable
fun ProductScreen(
    catalogProductScreenViewModel: CatalogProductScreenViewModel,
    navigateToCartButton: () -> Unit,
    navigateToCatalogButton: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val productScreenUiState = catalogProductScreenViewModel.productScreenUiState.collectAsState().value

    Scaffold(
        topBar = {},
        bottomBar = {}
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            ProductScreenBody(
                commodityItem = productScreenUiState.commodityItem,
                navigateToCartButton = navigateToCartButton,
                navigateToCatalogButton = navigateToCatalogButton
            )
        }
    }
}

@Composable
fun ProductScreenBody(
    commodityItem : CommodityItem,
    navigateToCartButton: () -> Unit,
    navigateToCatalogButton: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = dimensionResource(R.dimen.size_16),
                start = dimensionResource(R.dimen.size_16),
                end = dimensionResource(R.dimen.size_16),
                bottom = dimensionResource(R.dimen.size_8)
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(
                onClick = navigateToCatalogButton,
                modifier = Modifier.size(dimensionResource(id = R.dimen.size_44))
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.catalog),
                    tint = colorResource(id = R.color.white)
                )
            }
            Image(
                painter = painterResource(id = commodityItem.image),
                contentDescription = stringResource(R.string.product_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(enabled = true, state = rememberScrollState())
        ) {
            Text(
                text = commodityItem.productItem.name,
                style = TextStyle(
                    fontSize = dimensionResource(id = R.dimen.text_size_34).value.sp,
                    color = colorResource(id = R.color.black),
                )
            )
            Text(
                text = commodityItem.productItem.description,
                style = TextStyle(
                    fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                    color = colorResource(id = R.color.dark_gray),
                )
            )
            CompoundInfo(
                title = stringResource(id = R.string.weight),
                description = stringResource(id = R.string.measure, commodityItem.productItem.measure, commodityItem.productItem.measureUnit )
            )
            CompoundInfo(
                title = stringResource(id = R.string.energy_value),
                description = stringResource(id = R.string.energy_value_measure, commodityItem.productItem.energyPer100Grams)
            )
            CompoundInfo(
                title = stringResource(id = R.string.protein),
                description = stringResource(id = R.string.measure, commodityItem.productItem.proteinsPer100Grams, commodityItem.productItem.measureUnit )
            )
            CompoundInfo(
                title = stringResource(id = R.string.fats),
                description = stringResource(id = R.string.measure, commodityItem.productItem.fatsPer100Grams, commodityItem.productItem.measureUnit )
            )
            CompoundInfo(
                title = stringResource(id = R.string.carbs),
                description = stringResource(id = R.string.measure, commodityItem.productItem.carbohydratesPer100Grams, commodityItem.productItem.measureUnit )
            )
            HorizontalDivider(thickness = dimensionResource(id = R.dimen.size_6))
        }
        AddToCartProductScreenButton(
            price = commodityItem.productItem.priceCurrent,
            navigateToCartButton = navigateToCartButton
        )
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
        HorizontalDivider(
            color = colorResource(id = R.color.dark_gray),
            thickness = dimensionResource(R.dimen.size_1),
            modifier = Modifier
        )
        Text(
            text = title,
            style = TextStyle(
                fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                color = colorResource(id = R.color.dark_gray),
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = description,
            style = TextStyle(
                fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                color = colorResource(id = R.color.black),
            )
        )
    }
}

@Composable
fun AddToCartProductScreenButton(
    price: Int,
    navigateToCartButton: () -> Unit,
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
                .clickable { navigateToCartButton() }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.size_16))
            ) {
                Text(
                    text = stringResource(R.string.add_to_cart_for, price),
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