package com.example.asianfoodonlineshop

import android.app.Application
import com.example.asianfoodonlineshop.di.AppContainer
import com.example.asianfoodonlineshop.di.AppDataContainer

class AsianFoodOnlineShopApplication : Application(){
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}