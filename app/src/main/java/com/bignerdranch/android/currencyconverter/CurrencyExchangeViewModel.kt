package com.bignerdranch.android.currencyconverter

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class CurrencyExchangeViewModel : ViewModel() {
    private val dataRepository = DataRepository.get()// ссылка на репозиторий
    val isLeft
        get() = dataRepository.isLeft
    var firstCur: Valute
    var secondCur: Valute
    init {
        Log.d("TAG", "INIT")
        var i: Int
        var j: Int
        do {
            i = Random.nextInt(0, dataRepository.currencyData.size)
            j = Random.nextInt(0, dataRepository.currencyData.size)
        }while (i==j)
        firstCur = dataRepository.currencyData[i]
        secondCur = dataRepository.currencyData[j]
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
        dataRepository.oldValute = oldValute
        dataRepository.newValute = oldValute
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