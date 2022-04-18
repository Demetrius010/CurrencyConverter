package com.bignerdranch.android.currencyconverter

import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.bignerdranch.android.currencyconverter.models.DataRepository
import com.bignerdranch.android.currencyconverter.models.Valute
import com.bignerdranch.android.currencyconverter.views.ChangeCurrencyFragment
import com.bignerdranch.android.currencyconverter.views.CurrencyExchangeFragment
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TestChangeCurrencyFragment {

    lateinit var repo: DataRepository
    @Before
    fun setUp(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        DataRepository.initialize(appContext)
        repo = DataRepository.get()
        repo.currencyData = listOf(Valute(id="123", numCode=4, charCode = "RUB", nominal = 1, name = "Российский рубль", value = "1"), Valute(id="647", numCode=8, charCode = "USD", nominal = 1, name = "Доллар США", value = "81.25"))
    }
    @Test
    fun testFragment(){
        //val fragmentArgs = bundleOf("name" to "value")// // The "fragmentArgs" arguments are optional.
        //тут могут возникнуть ошибки если фрагмент ссылается на активити FragmentScenario$EmptyFragmentActivity cannot be cast to MainActivity
        val scenario = launchFragmentInContainer<CurrencyExchangeFragment>()//fragmentArgs)
        //onView(withId(R.id.swapImgView)).perform(click())
        //onView(withId(R.id.tvCharCodeLeft)).check(matches(withText("USD")))
        //onView(withId(R.id.tvCharCodeLeft)).check(matches(withText("RUB")))

        //If you need to call a method on the fragment itself, you can do so safely by getting a reference to the fragment using FragmentScenario.onFragment() and passing in a FragmentAction:
        scenario.onFragment{fragment ->
            val resul = fragment.convert(125.0,repo.currencyData[0].value.toDouble(), repo.currencyData[0].nominal.toDouble(),
                repo.currencyData[1].value.toDouble(), repo.currencyData[1].nominal.toDouble())
            assertEquals(1.54, resul,0.001)
        }
    }
}