package com.bignerdranch.android.currencyconverter.views

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimpleClass: Application()  {
    var result = 0
    fun nothingFun(bool: Boolean):LiveData<String>{
        return MutableLiveData()
    }

    fun sum(a: Int, b: Int): Int{
        result = a+b
        return result
    }

    fun funA():Int{
        return 123
    }
    fun funB():String{
        return "abc"
    }
}
