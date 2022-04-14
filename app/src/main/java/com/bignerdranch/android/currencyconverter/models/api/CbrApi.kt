package com.bignerdranch.android.currencyconverter.models.api

import com.bignerdranch.android.currencyconverter.models.ValCurs
import retrofit2.Call
import retrofit2.http.GET

interface CbrApi {
    @GET("XML_daily.asp")//Каждая функция в интерфейсе привязывается к конкретному HTTP-запросу и должна быть аннотирована аннотацией метода HTTP-запроса.
    fun getValCurs(): Call<ValCurs>//server query: get data from http://www.cbr.ru/scripts/XML_daily.asp
//По умолчанию все веб-запросы Retrofit возвращают объект retrofit2.Call. Объект Call представляет собой один вебзапрос, который вы можете выполнить.
//Тип, который вы используете в качестве общего параметра типа Call, определяет тип данных, на которые Retrofit раскладывает HTTP-ответ.
}