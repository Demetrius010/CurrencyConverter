package com.bignerdranch.android.currencyconverter.views

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimpleClass: Application()  {
    fun nothing(bool: Boolean):LiveData<String>{
        return MutableLiveData()
    }

    fun sum(a: Int, b: Int): Int{
        return a+b
    }

    fun funA():Int{
        return 123
    }
    fun funB():String{
        return "abc"
    }
}
