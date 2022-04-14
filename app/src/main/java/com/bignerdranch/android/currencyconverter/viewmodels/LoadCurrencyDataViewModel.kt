package com.bignerdranch.android.currencyconverter.viewmodels

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.currencyconverter.MainActivity
import com.bignerdranch.android.currencyconverter.models.CurrencyDataFetcher
import com.bignerdranch.android.currencyconverter.models.DataRepository
import com.bignerdranch.android.currencyconverter.models.Valute
import com.bignerdranch.android.currencyconverter.views.CurrencyExchangeFragment

private const val TAG = "CurrencyConverterViewModel"
/*ЭТО ViewModel НА ОСНОВЕ КЛАССА ViewModel который входит в состав пакета androidx.lifecycle,
А НЕ КАСТОМНАЯ ViewModel ДЛЯ РЕАЛИЗАЦИИ  MVC или MVVM,
ОНА СВЯЗЫВАЕТСЯ ТОЛЬКО С ОДНИМ ФРАГМЕНТОМ (АКТИВИТИ) И РАБОТАЕТ С НИМ ДО ЕГО ПОЛНОГО УНИЧТОЖЕНИЯ (ПРИ ПОВОРОТЕ ЭКРАНА ВОСОЗДАННЫЙ ФРАГМЕНТ (АКТИВИТИ) БУДЕТ ИСПОЛЬЗОВАТЬ ПЕРВЫЙ ОРИГИНАЛ)
 */
//ViewModel используется для реализации "репозитория / глобального класса" вы можете сохранять данные о состоянии интерфейса с помощью ViewModel, даже если конфигурация изменяется. Когда activity запрашивает CurrencyConverterViewModel после изменения конфигурации, экземпляр, который был создан изначально, возвращается.
//(Все что можно вырезать (данные и методы их обновления) из Activity вставляй в ViewModel). Потом ссылайся на них из Activity.)
class LoadCurrencyDataViewModel: ViewModel() { // ViewModel входит в состав пакета androidx.lifecycle Объект ViewModel связан с одним конкретным экраном и отлично подходит для размещения логики форматирования данных для отображения на этом экране. Объект ViewModel связан с модельным объектом и «декорирует» модель, добавляя ей функциональность для отображения на экране, которая в самой модели вам не нужна
    //ViewModel уничтожается после уничтожения фрагмента. Это может произойти, когда пользователь нажимает кнопку «Назад», закрывая экран. Это также может произойти, если хост-activity заменяет фрагмент на другой. Хотя на экране отображается та же activity, и фрагмент, и связанный с ним ViewModel будут уничтожены, так как они больше не нужны.
    //Есть один частный случай — это когда вы добавляете транзакцию проекта обратно в стек. Когда activity заменяет текущий фрагмент другим, а транзакция возвращается в стек, фрагмент и его ViewModel уничтожены не будут. Если пользователь нажимает кнопку «Назад», транзакция фрагмента восстанавливается. Оригинальный экземпляр фрагмента помещается обратно на экран, и все данные ViewModel сохраняются.
    init {
        Log.d(TAG, "Init")

        //currencyLifeData = CurrencyDataFetcher().fetchData()//Выполним веб-запрос для получения данных фото при первой инициализации ViewModel и сохраним полученные данные в созданное нами свойство.
    }//Функция CurrencyDataFetcher().fetchData() вызывается в блоке init{} модели CurrencyConverterViewModel. Она запускает запрос данных фото при первом создании ViewModel. Так как ViewModel создается только один раз в течение жизненного цикла владельца (при первом запросе из класса ViewModelProviders), запрос выполнится только один раз (при запуске пользователем LoadCurrencyDataFragment)
//Когда пользователь поворачивает устройство или иным образом инициирует изменение конфигурации, ViewModel останется в памяти, а воссозданная версия фрагмента сможет получить доступ к результатам оригинального запроса через ViewModel

    private val dataRepository = DataRepository.get()
    var currencyLifeData: LiveData<List<Valute>> = MutableLiveData() // здесь будем хранить LiveData из CurrencyDataFetcher
    val dataUpdatedFlag = MutableLiveData<Boolean>() // флаг по которому View (фрагмент) узнает что данные обновлены

    val currencyDataFetcherObserver = Observer<List<Valute>>{ currencyItems->// observer за данными из DataFetcher
        dataRepository.currencyData = currencyItems // передаем загруженные данные в Model (репозиторий)
        dataUpdatedFlag.value = true //Загрузив данные мы должны сообщить об этом View
    }

    fun startDataLoading(): LiveData<Boolean>  {//MutableLiveData отличается от LiveData наличием методов для добавления значений (setValue и postValue), однако в View не должно быть возможности добавлять значения поэтому мы передаем ей просто LiveData (для создания используй MutableLiveData, для отправки LiveData)
        currencyLifeData = CurrencyDataFetcher().fetchData() // инициализируем загрузку данных
        currencyLifeData.observeForever(currencyDataFetcherObserver)//вешаем наш Observer
        return dataUpdatedFlag
    }


    override fun onCleared() {// вызывается непосредственно перед уничтожением ViewModel
        currencyLifeData.removeObserver(currencyDataFetcherObserver) // убираем наш Observer
        super.onCleared()
        Log.d(TAG, "Cleared")
    }
}
//Когда пользователь поворачивает устройство или иным образом инициирует изменение конфигурации, ViewModel останется в памяти, а воссозданная версия фрагмента сможет получить доступ к результатам оригинального запроса через ViewModel. При таком устройстве репозиторий CurrencyDataFetcher будет продолжать выполнять запрос даже в том случае, если пользователь заблаговременно откажется от хост-activity фрагмента. В вашем приложении результат запроса будет просто проигнорирован. В реальном же приложении вы можете кэшировать результаты в базе данных или другом локальном хранилище, поэтому имеет смысл позволить фрагменту продолжить выполнение запроса.
//Если вы хотите вместо этого остановить запрос CurrencyDataFetcher при выходе пользователя из фрагмента, можно научить CurrencyDataFetcher сохранять объект Call, представляющий веб-запрос, и отменять запрос, когда ViewModel будет удален из памяти
