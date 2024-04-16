package com.example.asianfoodonlineshop.ui.screens.catalogAndProductScreen

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.asianfoodonlineshop.R
import com.example.asianfoodonlineshop.model.CommodityItem

@Composable
fun SearchResultScreen(
    navigateToProduct: () -> Unit,
    navigateToCatalog: () -> Unit,
    modifier: Modifier = Modifier,
    catalogScreenViewModel: CatalogScreenViewModel,
) {

    val searchUiState = catalogScreenViewModel.searchUiState.collectAsState().value

    // для того чтобы гарантировать фокус при переходе на экран поиска
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(searchUiState.searchField) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            SearchTextField(
                onEraseItemClick = {
                    catalogScreenViewModel.updateSearchField(TextFieldValue())
                },
                onBackItemClick = { navigateToCatalog() },
                searchFieldValue = searchUiState.searchField,
                onSearchFieldValueChange = {
                    catalogScreenViewModel.updateSearchField(it)
                },
                isFilled = searchUiState.searchField.text != "",
                onSearchFieldClick = {},
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.size_16))
                    .focusRequester(focusRequester) // для того чтобы гарантировать фокус при переходе на экран автоподсказок
                    .fillMaxWidth()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            SearchList(
                onProductItemClick = {
                    catalogScreenViewModel.chooseCommodityItem(it)
                    navigateToProduct()
                },
                productItemList = searchUiState.listOfSuitableProducts,
                modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.size_16),
                    end = dimensionResource(R.dimen.size_16),
                    bottom = dimensionResource(R.dimen.size_16)
                )
            )
        }
    }
}

@Composable
fun SearchList(
    onProductItemClick : (CommodityItem)->Unit,
    productItemList : List<CommodityItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ){
        items(productItemList){ productItem ->
            ProductCard(
                onProductItemClick = {onProductItemClick(productItem)},
                productItem = productItem,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun ProductCard(
    onProductItemClick : ()->Unit,
    productItem: CommodityItem,
    modifier: Modifier = Modifier
){
    Card(
        shape = MaterialTheme.shapes.small,
        onClick = onProductItemClick,
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.gray)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(id = R.dimen.size_4)
        ),
        modifier = modifier
            .padding(bottom = dimensionResource(id = R.dimen.size_8))
            .height(dimensionResource(id = R.dimen.size_40))
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
        ){
            Text(
                text = productItem.productItem.name,
                style = TextStyle(
                    fontWeight = FontWeight(integerResource(id = R.integer.weight_500)),
                    fontFamily = FontFamily(Font(R.font.roboto_medium)),
                    fontSize = dimensionResource(id = R.dimen.text_size_16).value.sp,
                    color = colorResource(id = R.color.black),
                ),
                maxLines = integerResource(id = R.integer.tag_2),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_12))
                )
        }
    }
}

@Composable
fun SearchTextField(
    onEraseItemClick : () -> Unit,
    onBackItemClick : () -> Unit,
    isFilled: Boolean = true,
    searchFieldValue : TextFieldValue,
    onSearchFieldClick: () -> Unit,
    onSearchFieldValueChange :  (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        shape =  MaterialTheme.shapes.small,
        placeholder = { Text(stringResource(id = R.string.search_field_stub)) },
        value = searchFieldValue,
        onValueChange = {onSearchFieldValueChange(it)},
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.search),
                tint = colorResource(id = R.color.black)
            )
        },
        trailingIcon = {
            if (isFilled) {
                IconButton(
                    onClick = onEraseItemClick,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_clear),
                        contentDescription = stringResource(id = R.string.clear),
                        tint = colorResource(id = R.color.black)
                    )
                }
            } else {
                IconButton(
                    onClick = onBackItemClick,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(id = R.string.back_to_catalog),
                        tint = colorResource(id = R.color.black)
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(id = R.color.gray),
            unfocusedContainerColor = colorResource(id = R.color.gray),
            focusedIndicatorColor = colorResource(id = R.color.orange),
            unfocusedIndicatorColor = Color.Transparent,
        ),
        singleLine = true,
        //для обработки нажатия на сам текстфилд - НА БУДУЩЕЕ
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onSearchFieldClick()
                        }
                    }
                }
            },
        modifier = modifier
    )
}