package com.bignerdranch.android.currencyconverter.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.currencyconverter.models.DataRepository
import com.bignerdranch.android.currencyconverter.models.Valute
import kotlin.random.Random

class CurrencyExchangeViewModel : ViewModel() {
    val dataRepository = DataRepository.get() // СДЕЛАЛ ПАБЛИК ЧТОБ В TestChangeCurrencyFragment можно было подменять репозиторий
    val isLeft
        get() = dataRepository.isLeft
    var firstCur: Valute
    var secondCur: Valute
    init {
        var i: Int
        var j: Int
        do {
            i = Random.nextInt(0, dataRepository.currencyData.size)
            j = Random.nextInt(0, dataRepository.currencyData.size)
        }while (i==j)
        firstCur = dataRepository.currencyData[i]
        secondCur = dataRepository.currencyData[j]

        Log.d("CurrencyExchangeViewModel", "CurrencyExchangeViewModel INIT")
    }

    fun setNewValute(){
        dataRepository.newValute?.let {
            if(dataRepository.isLeft==true)
                firstCur = it
            else if(dataRepository.isLeft == false){
                secondCur = it
            }
        }
    }

    fun setChangingValute(oldValute: Valute, isLeft: Boolean){
        dataRepository.oldValute = oldValute //старое значение сохраняме с целью показать на против него checked в окне выбора валют
        dataRepository.newValute = oldValute//в поле для нового значения сохраняем старое на случай если пользователь выбрав валюту на экране выбора передумает и нажмет вернуться назад
        dataRepository.isLeft = isLeft
    }

    fun valuteChanged(){
        dataRepository.isLeft = null
    }

    fun swapCurrency() {
        val tempCur = firstCur
        firstCur = secondCur
        secondCur = tempCur
    }
}