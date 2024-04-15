package com.example.asianfoodonlineshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.asianfoodonlineshop.di.AppDataContainer
import com.example.asianfoodonlineshop.ui.AsianFoodOnlineShopApp
import com.example.asianfoodonlineshop.ui.theme.AsianFoodOnlineShopTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Чтобы удалить данные
        lifecycleScope.launch(Dispatchers.IO) {
            AppDataContainer(this@MainActivity).usersRepository.deleteAllCartItems()
        }
        installSplashScreen()
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
    //не всегда работает поэтому удаляем через onCreate
//    override fun onDestroy() {
//        super.onDestroy()
//        lifecycleScope.launch(Dispatchers.IO) {
//            AppDataContainer(this@MainActivity).usersRepository.deleteAllCartItems()
//        }
//    }
}