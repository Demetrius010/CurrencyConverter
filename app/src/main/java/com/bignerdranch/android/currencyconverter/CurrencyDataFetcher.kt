package com.bignerdranch.android.currencyconverter

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.currencyconverter.API.CbrApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

private const val TAG = "CurrencyDataFetcher"
private const val BASE_URL = "http://www.cbr.ru/scripts/"

class CurrencyDataFetcher { //MY DATA REPOSETORY CLASSS
    private val cbrApi: CbrApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()//Экземпляр Retrofit отвечает за реализацию и создание экземпляров вашего интерфейса API Retrofit.Builder() — это интерфейс, выполняющий настройку и сборку вашего экземпляра Retrofit.
            .baseUrl(BASE_URL)//Базовый URL для вашей конечной точки
            .addConverterFactory(SimpleXmlConverterFactory.create()) //Using Jaxb XML converter По умолчанию Retrofit десериализует веб-ответы в объекты okhttp3.ResponseBody
            .build()//возвращает экземпляр Retrofit, у которого появляются настройки, заданные с помощью объекта builder
        cbrApi = retrofit.create(CbrApi::class.java)//Retrofit использует информацию в указанном вами интерфейсе API наряду с информацией, указанной вами при сборке экземпляра Retrofit, для создания экземпляров анонимного класса, реализующего интерфейс «на лету».
    }

//вы приписываете значение responseLiveData пустому объекту MutableLiveData. Затем вы ставите в очередь вебзапрос для получения страницы и возвращаете responseLiveData (до завершения запроса). После успешного завершения результат становится публичным путем установки значения responseLiveData.value. Таким образом, другие компоненты, такие как Fragment, могут наблюдать объект LiveData, возвращенный из fetchContents(), чтобы в конечном итоге получить результаты веб-запроса.
    fun fetchData(): LiveData<List<Valute>>{//getting data using Retrofit
        val responseLiveData: MutableLiveData<List<Valute>> = MutableLiveData()//LiveData — это класс — контейнер данных из Jetpack библиотеки lifecycle-extensions. Вы можете настроить activity или фрагмент на наблюдение за LiveData, и в этом случае ваша activity или фрагмент будет уведомлен в основном потоке, когда они будут готовы.
        cbrApi.getValCurs().enqueue(object: Callback<ValCurs> {//asynch call //getValCurs возвращает объект Call, представляющий собой веб-запрос. Для выполнения веб-запроса, содержащегося в объекте Call, необходимо вызвать функцию enqueue(запрос выполняется в фоновом потоке) и передать экземпляр retrofit2.Callback. Объект Callback, который вы передаете в enqueue(...), позволяет определить, что вы хотите сделать после того, как будет получен ответ на запрос
        override fun onResponse(call: Call<ValCurs>, response: Response<ValCurs>) {//Передача Response Retrofit в onResponse() содержит в своем теле содержимое результата. Тип результата будет соответствовать типу возвращаемого объекта, который вы указали в соответствующей функции в интерфейсе API
            if (response.isSuccessful()) {
                val valCurs: ValCurs? = response.body()
                responseLiveData.value = valCurs?.valutes ?: mutableListOf()
//              ((GlobalData)getApplicationContext()).setValCursData(response.body()); // save received data to the global class
                Log.d(TAG,"Response received: ${responseLiveData.value!![0]}")
            } else {
                Log.d(TAG,"ERROR! ${response.code()}")
            }
        }
            override fun onFailure(call: Call<ValCurs>, t: Throwable) {//Объект Call, переданный функциям onResponse() и onFailure(), — это исходный объект вызова, используемый для инициирования запроса.
                Log.d(TAG,"ERROR! ${t.message}")
            }
        })
        return responseLiveData//Обратите внимание, что возвращаемый тип является неизменяемым LiveData. Вам следует избегать выставления на публику объектов «живых» данных, если это возможно, чтобы другие компоненты не могли изменять содержимое «живых» данных. Данные через LiveData должны идти в одном направлении
    }
}