package com.bignerdranch.android.currencyconverter

import android.app.Application
import android.util.Log
import com.bignerdranch.android.currencyconverter.models.DataRepository

//Чтобы класс приложения можно было использовать в системе, необходимо зарегистрировать его в манифесте.
//<application
//    android:name=".CriminalIntentApplication"
class CurrencyConverterApplication: Application() {//Чтобы выполнить работу, как только приложение будет готово, вы можете создать подкласс Application. Он позволит вам получить информацию о жизненном цикле самого приложения
    //Когда класс приложения будет зарегистрирован в манифесте, операционная система создаст экземпляр CriminalIntentApplication при запуске вашего приложения. ОС будет вызывать функцию OnCreate() на экземпляре CurrencyConverterApplication
    override fun onCreate() {//функция Application.onCreate() вызывается системой, когда приложение впервые загружается в память. Это как раз подходящее место для разовой инициализации. Экземпляр приложения не будет постоянно уничтожаться и создаваться вновь, в отличие от activity или фрагментов классов. Он создается, когда приложение запускается, и уничтожается, когда завершается процесс приложения
        super.onCreate()
        Log.d("CurrencyConverterApplication", "app init")
        DataRepository.initialize(this)//Здесь передается экземпляр приложения в репозиторий в качестве объекта Context. Этот объект действителен до тех пор, пока процесс приложения находится в памяти, поэтому можно безопасно хранить ссылку на него в классе репозитория.
    }
}