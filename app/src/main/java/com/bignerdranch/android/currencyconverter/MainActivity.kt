package com.bignerdranch.android.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bignerdranch.android.currencyconverter.databinding.ActivityMainBinding
import com.bignerdranch.android.currencyconverter.views.LoadCurrencyDataFragment

class MainActivity : AppCompatActivity() {//LoadCurrencyDataFragment.Callbacks

    private  lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        Log.d("MainActivity", "MainActivity init")

    /* Ручная навигация */
    //Почему фрагмент может уже находиться в списке? Вызов MainActivity.onCreate (Bundle?) может быть выполнен в ответ на воссоздание объекта MainActivity после его уничтожения из-за поворота устройства или освобождения памяти. При уничтожении activity ее экземпляр FragmentManager сохраняет список фрагментов. При воссоздании activity новый экземпляр FragmentManager загружает список и воссоздает хранящиеся в нем фрагменты, чтобы все работало как прежде.
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)//Доступ к менеджеру фрагментов осуществляется с помощью свойства supportFragmentManager.
//        if (currentFragment == null) {//если контейнер пустой то добавляем новый фракмент, но если активити была пересоздана то фрагмент менеджер заполняется предыдущим списком фрагментов
//            val fragment = LoadCurrencyDataFragment.newInstance()//CurrencyExchangeFragment.newInstance()
//            //Этот код создает и закрепляет транзакцию фрагмента. Транзакции фрагментов используются для добавления, удаления, присоединения, отсоединения и замены фрагментов в списке фрагментов
//            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit()
//        }

    //setupNavigation()

    }
    /* Ручная навигация через обратный вызов из интерфеса */
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

/*
    private fun setupNavigation(){// Это нужно только для МЕНЮ
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController//или так     val navController: NavController = Navigation.findNavController(this, R.id.nav_host_fragment_container)
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)//Метод setupWithNavController вешает листенер на BottomNavigationView и выполняет навигацию при нажатии на его элементы. При этом выполняет setChecked для нажатого элемента.
        //NavigationUI.setupActionBarWithNavController(this, navController)//Можно добавить интеграцию с ActionBar. Теперь при навигации в ActionBar будет помещаться Label у destination. И иконка будет меняться, если находимся не в стартовом destination.
        //В этом случае надо самим обработать нажатие на Home.
        //@Override
        //public boolean onOptionsItemSelected(MenuItem item) {
        //   switch (item.getItemId()) {
        //       case android.R.id.home:
        //           navController.popBackStack();
        //           return true;
        //   }
        //   return super.onOptionsItemSelected(item);
        //}
    }
*/
}