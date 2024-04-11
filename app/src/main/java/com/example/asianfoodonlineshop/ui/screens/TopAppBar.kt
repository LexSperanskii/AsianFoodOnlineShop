package com.example.asianfoodonlineshop.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.asianfoodonlineshop.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarMenuLogoSearch(
    onClickMenu: () -> Unit,
    onCLickSearch: () -> Unit,
    tagsCount: String,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        navigationIcon = {
            BadgedBox(
                badge = {
                    Badge {
                        Text(
                            text = tagsCount
                        )
                    }
                }) {
                IconButton(onClick = onClickMenu) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(R.string.menu_button),
                        tint = colorResource(id = R.color.black),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.size_24))
                    )
                }
            }
        },
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_logo),
                    tint = colorResource(id = R.color.orange),
                    contentDescription = stringResource(R.string.logo),
                    modifier = Modifier.height(dimensionResource(id = R.dimen.size_44))
                )
            }
        },
        actions = {
            IconButton(onClick = onCLickSearch) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(R.string.menu_button),
                    tint = colorResource(id = R.color.black),
                    modifier = Modifier.size(dimensionResource(id = R.dimen.size_24))
                )
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarBackAndName(
    currentDestinationTitle: Int,
    onClickNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    text = stringResource(id = currentDestinationTitle),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = dimensionResource(id = R.dimen.text_size_18).value.sp,
                        color = colorResource(id = R.color.black),
                    ),
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.size_16))
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onClickNavigateBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = colorResource(id = R.color.orange),
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        modifier = modifier
    )
}