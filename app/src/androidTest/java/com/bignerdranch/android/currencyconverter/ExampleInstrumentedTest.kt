package com.bignerdranch.android.currencyconverter

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.bignerdranch.android.currencyconverter.models.DataRepository
import com.bignerdranch.android.currencyconverter.models.Valute
import com.bignerdranch.android.currencyconverter.viewmodels.CurrencyExchangeViewModel


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)//перед нами тест Android, который должен работать с activity и другими средствами времени выполнения Android
class ExampleInstrumentedTest {

    @get:Rule//сообщает JUnit о необходимости запускать экземпляр MainActivity перед запуском каждого теста.
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup(){
    }

   @Test
   fun shownCharCode(){
       onView(withId(R.id.refreshImageView)).perform(click())//выполнить клик. Когда вы взаимодействуете с представлением, Espresso дождется бездействия приложения, перед тем как продолжать тестирование. В Espresso существуют встроенные средства проверки завершения обновлений пользовательского интерфейса, но если вам потребуется более продолжительное ожидание — используйте подкласс IdlingResource для передачи Espresso сигнала о том, что приложение еще не завершило свои операции.
      // в perform можно перечислить множество действий для заданного view
       //onView(withText("AUD")).check(matches(isDisplayed()))строка onView(withText("TEXT")) находит представление с текстом TEXT для выполнения тестовой операции. Вызов check(matches(isDisplayed())) проверяет, что такое представление существует.
       //onView(withText("AUD")).check(matches(withText("AUD")))
   }

//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.bignerdranch.android.currencyconverter", appContext.packageName)
//    }

}