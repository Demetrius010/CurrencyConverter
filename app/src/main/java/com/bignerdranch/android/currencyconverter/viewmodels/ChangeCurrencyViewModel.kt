package com.bignerdranch.android.currencyconverter.viewmodels

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.currencyconverter.models.DataRepository
import com.bignerdranch.android.currencyconverter.models.Valute

class ChangeCurrencyViewModel: ViewModel() {
    private val dataRepository = DataRepository.get()
    val oldValue = dataRepository.oldValute
    val currencyData = dataRepository.currencyData
    fun saveNewValue(value: Valute?){
        dataRepository.newValute = value
    }
}