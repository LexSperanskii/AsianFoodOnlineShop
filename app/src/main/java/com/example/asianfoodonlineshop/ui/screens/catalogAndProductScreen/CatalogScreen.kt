package com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.asianfoodonlineshop.R
import com.example.asianfoodonlineshop.model.CommodityItem
import com.example.asianfoodonlineshop.model.network.AttributesItemModel
import com.example.asianfoodonlineshop.model.network.CategoriesItemModel
import com.example.asianfoodonlineshop.ui.screens.TopAppBarMenuLogoSearch


@Composable
fun CatalogScreen(
    navigateToCartButton: () -> Unit,
    navigateToProduct: () -> Unit,
    navigateToSearch: () -> Unit,
    catalogScreenViewModel: CatalogScreenViewModel,
    modifier: Modifier = Modifier
) {
    val catalogScreenUiState = catalogScreenViewModel.catalogScreenUiState.collectAsState().value
    val catalogScreenNetworkUiState = catalogScreenViewModel.catalogScreenNetworkUiState

    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBarMenuLogoSearch(
                onClickMenu = {
                    showBottomSheet = true
                },
                onCLickSearch = { navigateToSearch() },
                tagsCount = catalogScreenUiState.listOfChosenAttributes.size
            )
        },
        bottomBar = {
            if (catalogScreenUiState.price != 0){
                NavigateToCartButton(
                    price = priceFormat(catalogScreenUiState.price),
                    navigateToCartButton = navigateToCartButton,
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CategoryRow(
                listOfCategories = catalogScreenUiState.listOfCategories,
                currentCategory = catalogScreenUiState.currentCategory,
                onCategoryClick = { catalogScreenViewModel.onCategoryClick(it) },
                modifier = Modifier.fillMaxWidth()
            )
            when (catalogScreenNetworkUiState) {
                is CatalogScreenNetworkUiState.Loading ->
                    LoadingScreen(modifier = Modifier.fillMaxSize())
                is CatalogScreenNetworkUiState.Success ->
                    CommodityItemsGridScreen(
                        productItems = catalogScreenUiState.listOfProducts,
                        onCardClick = {
                            catalogScreenViewModel.chooseCommodityItem(it)
                            navigateToProduct()
                        },
                        onAddToCartButtonClick = {
                            catalogScreenViewModel.increaseQuantity(it)
                        },
                        onIncreaseQuantityClick = {
                            catalogScreenViewModel.increaseQuantity(it)
                        },
                        onDecreaseQuantityClick = {
                            catalogScreenViewModel.decreaseQuantity(it)
                        },
                        isShowBottomSheet = showBottomSheet,
                        onDismissRequestClick = { showBottomSheet = false },
                        listOfAttributes = catalogScreenUiState.listOfAttributes,
                        listOfChosenAttributes = catalogScreenUiState.listOfChosenAttributes,
                        onCheckedChangeClick = { attribute, isChecked ->
                            catalogScreenViewModel.onAttributeItemClick(attribute, isChecked)
                        },
                        onButtonTagsClick = { showBottomSheet = false },
                        modifier = Modifier.fillMaxSize()
                    )
                is CatalogScreenNetworkUiState.Error ->
                    ErrorScreen(
                        retryAction = { catalogScreenViewModel.getCommodityItemsInfo() },
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = null
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(dimensionResource(R.dimen.size_16))
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(dimensionResource(R.dimen.size_200)),
        painter = painterResource(R.drawable.ic_loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun CommodityElement(
    commodityItem: CommodityItem,
    onCardClick: () -> Unit,
    isOnSale: Boolean,
    isVegan: Boolean,
    isSpicy: Boolean,
    onAddToCartButtonClick: () -> Unit,
    onIncreaseQuantityClick: () -> Unit,
    onDecreaseQuantityClick: () -> Unit,
    modifier: Modifier = Modifier,
    ) {
    Card(
        modifier = modifier
            .clickable(onClick = onCardClick)
            .padding(dimensionResource(R.dimen.size_4))
            .height(dimensionResource(R.dimen.size_290))
            .width(dimensionResource(R.dimen.size_168)),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(colorResource(id = R.color.gray))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.size_170))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.size_8))
                    ){
                        if (isOnSale){
                            Image(
                                painter = painterResource(id = R.drawable.ic_sale_tag),
                                contentDescription = stringResource(R.string.reduced),
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.size_24))
                                    .padding(end = dimensionResource(id = R.dimen.size_2))
                            )
                        }
                        if (isVegan){
                            Image(
                                painter = painterResource(id = R.drawable.ic_vegan),
                                contentDescription = stringResource(R.string.reduced),
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.size_24))
                                    .padding(end = dimensionResource(id = R.dimen.size_2))
                            )
                        }
                        if (isSpicy){
                            Image(
                                painter = painterResource(id = R.drawable.ic_spicy),
                                contentDescription = stringResource(R.string.reduced),
                                modifier = Modifier
                                    .size(dimensionResource(R.dimen.size_24))
                                    .padding(end = dimensionResource(id = R.dimen.size_2))
                            )
                        }
                    }
                    Image(
                        painter = painterResource(id = commodityItem.image),
                        contentDescription = stringResource(R.string.product_image),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Column(modifier = Modifier
                .padding(dimensionResource(id = R.dimen.size_12))
                .fillMaxSize()
            ) {
                Text(
                    text = commodityItem.productItem.name,
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_400)),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = dimensionResource(id = R.dimen.text_size_14).value.sp,
                        color = colorResource(id = R.color.black),
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.size_4))
                )
                Text(
                    text = stringResource(id = R.string.measure, commodityItem.productItem.measure, commodityItem.productItem.measureUnit),
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_400)),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = dimensionResource(id = R.dimen.text_size_14).value.sp,
                        color = colorResource(id = R.color.dark_gray),
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                if (commodityItem.quantity == 0) {
                    PriceButtonDefault(
                        commodityItem = commodityItem,
                        onAddToCartButtonClick = onAddToCartButtonClick
                    )
                } else {
                    PriceButtonForQuantity(
                        commodityItem = commodityItem,
                        onIncreaseQuantityClick = onIncreaseQuantityClick,
                        onDecreaseQuantityClick = onDecreaseQuantityClick
                    )
                }
            }
        }
    }
}

@Composable
fun CommodityItemsGridScreen(
    productItems : List<CommodityItem>,
    onCardClick: (CommodityItem) -> Unit,
    onAddToCartButtonClick: (CommodityItem) -> Unit,
    onIncreaseQuantityClick: (CommodityItem) -> Unit,
    onDecreaseQuantityClick: (CommodityItem) -> Unit,
    isShowBottomSheet: Boolean,
    onDismissRequestClick: () -> Unit,
    listOfAttributes: List<AttributesItemModel>,
    listOfChosenAttributes: List<AttributesItemModel>,
    onCheckedChangeClick: (AttributesItemModel, Boolean)->Unit,
    onButtonTagsClick: () -> Unit,
    modifier: Modifier = Modifier,
    ) {
    if (productItems.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(id = R.string.catalog_stub),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }else{
        LazyVerticalGrid(
            columns = GridCells.Adaptive(dimensionResource(R.dimen.size_168)),
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(dimensionResource(R.dimen.size_12))
        ) {
            items(items = productItems) { item ->
                CommodityElement(
                    commodityItem = item,
                    onCardClick = { onCardClick(item) },
                    isOnSale = item.productItem.priceOld != null,
                    isVegan = item.productItem.tagIds.contains(integerResource(id = R.integer.tag_2)),
                    isSpicy = item.productItem.tagIds.contains(integerResource(id = R.integer.tag_4)),
                    onAddToCartButtonClick = {onAddToCartButtonClick(item)},
                    onIncreaseQuantityClick = {onIncreaseQuantityClick(item)},
                    onDecreaseQuantityClick = {onDecreaseQuantityClick(item)}
                )
            }
        }
    }
    TagsBottomSheet(
        isShowBottomSheet = isShowBottomSheet,
        onDismissRequestClick = onDismissRequestClick,
        listOfAttributes = listOfAttributes,
        listOfChosenAttributes = listOfChosenAttributes,
        onCheckedChangeClick = onCheckedChangeClick,
        onButtonTagsClick = onButtonTagsClick,
    )
}

@Composable
fun CategoryRow(
    listOfCategories : List<CategoriesItemModel>,
    currentCategory : CategoriesItemModel,
    onCategoryClick: (CategoriesItemModel)->Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.size_8)),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(R.dimen.size_16)
        )
    ) {
        items(items = listOfCategories) { category ->
            if (category == currentCategory){
                Button(
                    onClick = {},
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.orange),
                    )
                ) {
                    Text(
                        text = category.name,
                        style = TextStyle(
                            fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                            fontFamily = FontFamily(Font(R.font.roboto_medium)),
                            fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                            color = colorResource(id = R.color.white),
                        )
                    )
                }
            }else{
                Button(
                    onClick = { onCategoryClick(category) },
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.white),
                    )
                ) {
                    Text(
                        text = category.name,
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
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsBottomSheet(
    isShowBottomSheet: Boolean,
    onDismissRequestClick: () -> Unit,
    listOfAttributes: List<AttributesItemModel>,
    listOfChosenAttributes: List<AttributesItemModel>,
    onCheckedChangeClick: (AttributesItemModel,Boolean)->Unit,
    onButtonTagsClick: () -> Unit,
) {
    if (isShowBottomSheet){
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(),
            shape = MaterialTheme.shapes.large,
            onDismissRequest = onDismissRequestClick,
            containerColor = colorResource(id = R.color.white),
            dragHandle = {}//показывает значек чтобы тянуть за него
        ) {
            Column(
                modifier = Modifier.padding(
                    vertical =  dimensionResource(id = R.dimen.size_32),
                    horizontal = dimensionResource(id = R.dimen.size_24)
                )
            ) {
                Text(
                    text = stringResource(id = R.string.choose_tags),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.text_size_20).value.sp,
                        color = colorResource(id = R.color.black),
                    ),
                    modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.size_16))
                )
                LazyColumn() {
                    items(items = listOfAttributes) { attribute ->
                        TagCheckBox(
                            text = attribute.name,
                            isChecked = listOfChosenAttributes.contains(attribute),
                            onCheckedChangeClick = {onCheckedChangeClick(attribute,it)}
                        )
                    }
                }
                Button(
                    onClick = onButtonTagsClick,
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.orange)
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(dimensionResource(R.dimen.size_48))
                        .width(dimensionResource(R.dimen.size_327))

                ) {
                    Text(
                        text = stringResource(R.string.apply),
                        style = TextStyle(
                            fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                            color = colorResource(id = R.color.white),
                        )
                    )
                }

            }
        }
    }
}

@Composable
fun TagCheckBox(
    text: String,
    isChecked:Boolean,
    onCheckedChangeClick: (Boolean) -> Unit
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                    color = colorResource(id = R.color.black),
                )
            )
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = isChecked,
                onCheckedChange = {onCheckedChangeClick(it)}
            )
        }
        HorizontalDivider(modifier = Modifier.padding(all = dimensionResource(id = R.dimen.size_6)))
    }
}
@Composable
fun PriceButtonDefault(
    commodityItem: CommodityItem,
    onAddToCartButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center ,
        modifier = modifier
            .clickable { onAddToCartButtonClick() }
            .clip(MaterialTheme.shapes.small)
            .background(colorResource(id = R.color.white))
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.size_40))
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(commodityItem.productItem.priceOld == null){
                Text(
                    text = stringResource(R.string.price, priceFormat(commodityItem.productItem.priceCurrent)),
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                        color = colorResource(id = R.color.black),
                    )
                )
            }
            else{
                Text(
                    text = stringResource(R.string.price, priceFormat(commodityItem.productItem.priceCurrent)),
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                        fontFamily = FontFamily(Font(R.font.roboto_medium)),
                        fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                        color = colorResource(id = R.color.black),
                    ),
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.size_8))
                )
                Text(
                    text = stringResource(R.string.price, priceFormat(commodityItem.productItem.priceOld)),
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_400)),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = dimensionResource(id = R.dimen.text_size_14).value.sp,
                        color = colorResource(id = R.color.dark_gray),
                        textDecoration = TextDecoration.LineThrough

                    )
                )
            }
        }
    }
}
@Composable
fun PriceButtonForQuantity(
    commodityItem: CommodityItem,
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
                .background(colorResource(id = R.color.white))
                .size(dimensionResource(R.dimen.size_40))
                .clickable { onDecreaseQuantityClick() }
        ){
            Icon(
                painter = painterResource(id = R.drawable.ic_minus) ,
                contentDescription = stringResource(id = R.string.add_to_cart),
                tint = colorResource(id = R.color.orange)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = commodityItem.quantity.toString(),
            style = TextStyle(
                fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                fontFamily = FontFamily(Font(R.font.roboto_medium)),
                fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                color = colorResource(id = R.color.black),
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            contentAlignment = Alignment.Center ,
            modifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .background(colorResource(id = R.color.white))
                .size(dimensionResource(R.dimen.size_40))
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
fun NavigateToCartButton(
    price: String,
    navigateToCartButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(
        vertical = dimensionResource(id = R.dimen.size_12),
        horizontal = dimensionResource(id = R.dimen.size_16)
    )) {
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.ShoppingCart,
                    contentDescription = stringResource(id = R.string.navigate_to_cart),
                    tint = colorResource(id = R.color.white),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_20))
                    )
                Text(
                    text = stringResource(R.string.price, price),
                    style = TextStyle(
                        fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                        fontFamily = FontFamily(Font(R.font.roboto_regular)),
                        fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                        color = colorResource(id = R.color.white),
                    ),
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_8))
                )
            }
        }
    }
}

fun priceFormat(price: Int): String {
    val priceDouble = price.toDouble()
    val newPrice = priceDouble / 100
    return if (newPrice % 1 == 0.00)
        newPrice.toInt().toString()
    else
        newPrice.toString()
}