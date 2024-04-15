package com.example.asianfoodonlineshop

import android.app.Application
import android.util.Log
import com.example.asianfoodonlineshop.di.AppContainer
import com.example.asianfoodonlineshop.di.AppDataContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AsianFoodOnlineShopApplication : Application(){
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

//        Сохранка на будущее как получшить корутину в Application
//        CoroutineScope(Dispatchers.IO).launch {
//            Log.d("test","555555")
//            container.usersRepository.deleteAllCartItems()
//        }

    }
}