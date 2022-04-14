package com.bignerdranch.android.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.bignerdranch.android.currencyconverter.views.LoadCurrencyDataFragment

class MainActivity : AppCompatActivity() {//LoadCurrencyDataFragment.Callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MainActivity", "MainActivity init")
    //Почему фрагмент может уже находиться в списке? Вызов MainActivity.onCreate (Bundle?) может быть выполнен в ответ на воссоздание объекта MainActivity после его уничтожения из-за поворота устройства или освобождения памяти. При уничтожении activity ее экземпляр FragmentManager сохраняет список фрагментов. При воссоздании activity новый экземпляр FragmentManager загружает список и воссоздает хранящиеся в нем фрагменты, чтобы все работало как прежде.
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)//Доступ к менеджеру фрагментов осуществляется с помощью свойства supportFragmentManager.
        if (currentFragment == null) {
            val fragment = LoadCurrencyDataFragment.newInstance()//CurrencyExchangeFragment.newInstance()
            //Этот код создает и закрепляет транзакцию фрагмента. Транзакции фрагментов используются для добавления, удаления, присоединения, отсоединения и замены фрагментов в списке фрагментов
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }
//    override fun onDataRecieved() {//обратный вызов из LoadCurrencyDataFragment
//        val fragment = CurrencyExchangeFragment.newInstance()
//        //Этот код создает и закрепляет транзакцию фрагмента. Транзакции фрагментов используются для добавления, удаления, присоединения, отсоединения и замены фрагментов в списке фрагментов
//        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)//FragmentTransaction.replace(Int,Fragment) заменяет фрагмент, размещенный в activity . Если фрагмент еще не размещен в указанном контейнере, то добавляется новый фрагмент, как если бы вы вызвали FragmentTransaction.add(Int,Fragment).
//            .addToBackStack(null).commit()//Пользователи будут ожидать, что нажатие кнопки «Назад» на экране подробностей преступления вернет их обратно к списку преступлений. Чтобы реализовать это поведение, добавьте транзакцию замены в обратный стек. Теперь при нажатии пользователем кнопки «Назад» транзакция будет обращена. Таким образом, LoadCurrencyDataFragment будет заменен на CurrencyExchangeFragment
//    }

    fun loadNewFragment(fragment: Fragment){//МОЙ обход реализации интерфейсов обратных вызовов
        //Этот код создает и закрепляет транзакцию фрагмента. Транзакции фрагментов используются для добавления, удаления, присоединения, отсоединения и замены фрагментов в списке фрагментов
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)//FragmentTransaction.replace(Int,Fragment) заменяет фрагмент, размещенный в activity . Если фрагмент еще не размещен в указанном контейнере, то добавляется новый фрагмент, как если бы вы вызвали FragmentTransaction.add(Int,Fragment).
            .addToBackStack(null).commit()//Пользователи будут ожидать, что нажатие кнопки «Назад» на экране подробностей преступления вернет их обратно к списку преступлений. Чтобы реализовать это поведение, добавьте транзакцию замены в обратный стек. Теперь при нажатии пользователем кнопки «Назад» транзакция будет обращена. Таким образом, LoadCurrencyDataFragment будет заменен на CurrencyExchangeFragment

    }

}