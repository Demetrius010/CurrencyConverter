package com.bignerdranch.android.currencyconverter.models

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bignerdranch.android.currencyconverter.CurrencyConverterApplication
import java.lang.IllegalStateException

//Класс репозитория инкапсулирует логику для доступа к данным из одного источника или совокупности источников. Ваш код UI будет запрашивать все данные из репозитория,
class DataRepository private constructor(context: Context){//это одноэлементный класс (синглтон). Это означает, что в вашем процессе приложения единовременно существует только один его экземпляр. Синглтон существует до тех пор, пока приложение находится в памяти, поэтому хранение в нем любых свойств позволяет получить к ним доступ в течение жизненного цикла вашей activity и фрагмента. Синглтонамы уничтожаются, когда Android удаляет приложение из памяти.
// Вы также можете пометить конструктор как приватный, чтобы убедиться в отсутствии компонентов, которые могут пойти против системы и создать собственный экземпляр.
//val app = context.applicationContext as CurrencyConverterApplication//val fileDir = context.applicationContext Напрямую хранить контекст нельзя, но им можно пользоваться как входящим параметром. Do not place Android context classes in static fields (static reference to DataRepository which has field fileDir pointing to Context); this is a memory leak
    companion object{
        private var instance: DataRepository? = null
        fun initialize(context: Context){
            if(instance == null){
                instance = DataRepository(context.applicationContext)
            }
        }
        fun get(): DataRepository {
            return instance ?: throw IllegalStateException("DataRepository must be initialized")
        }
    }

    var currencyData: List<Valute> = listOf()//TODO: ПРОВЕРЬ КАК В КОТЛИН РАБОТАЮТ МЕТОДЫ ДОСТУПА. currencyData должно только предоставлять доступ ТОЛЬКО НА ЧТЕНИЕ!
    var oldValute: Valute? = null
    var newValute: Valute? = null
    var isLeft: Boolean? = null
}