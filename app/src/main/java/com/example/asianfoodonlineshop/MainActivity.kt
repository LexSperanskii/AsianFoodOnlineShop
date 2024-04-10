package com.example.asianfoodonlineshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.asianfoodonlineshop.ui.AsianFoodOnlineShopApp
import com.example.asianfoodonlineshop.ui.theme.AsianFoodOnlineShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsianFoodOnlineShopTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    AsianFoodOnlineShopApp()
                }
            }
        }
    }
}