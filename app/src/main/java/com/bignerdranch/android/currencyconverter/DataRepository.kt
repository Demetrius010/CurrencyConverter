package com.bignerdranch.android.currencyconverter

import android.content.Context
import java.lang.IllegalStateException

//Класс репозитория инкапсулирует логику для доступа к данным из одного источника или совокупности источников. Ваш код UI будет запрашивать все данные из репозитория,
class DataRepository private constructor(context: Context){//это одноэлементный класс (синглтон). Это означает, что в вашем процессе приложения единовременно существует только один его экземпляр. Синглтон существует до тех пор, пока приложение находится в памяти, поэтому хранение в нем любых свойств позволяет получить к ним доступ в течение жизненного цикла вашей activity и фрагмента. Синглтонамы уничтожаются, когда Android удаляет приложение из памяти.
// Вы также можете пометить конструктор как приватный, чтобы убедиться в отсутствии компонентов, которые могут пойти против системы и создать собственный экземпляр.
    companion object{
        private var INSTANCE: DataRepository? = null
        fun initialize(context: Context){
            if(INSTANCE == null){
                INSTANCE = DataRepository(context)
            }
        }
        fun get(): DataRepository{
            return INSTANCE ?: throw IllegalStateException("DataRepository must be initialized")
        }
    }

    var currencyData: List<Valute> = listOf()
    var oldValute: Valute? = null
    var newValute: Valute? = null
    var isLeft: Boolean? = null
}