package com.bignerdranch.android.currencyconverter

import com.bignerdranch.android.currencyconverter.views.SimpleClass
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

//@RunWith(JUnit4::class)//указывается так называемый Runner, который и отвечает за запуск тестов, корректный вызов и обработку всех методов
class SimpleClassTest {

    private lateinit var mSimpl: SimpleClass

    @Before
    fun setUp() {
        mSimpl = SimpleClass()
    }

    @Test
    fun sum() {
        assertEquals(7, mSimpl.sum(3,4))
    }

    @Test
    fun greather(){
        //assertThat(5, greaterThan(2)) // тоже работает
        assertThat(5, Is.`is`(greaterThan(4)))
    }

    @Test
    fun container(){
        val container = listOf(1, 22, 43, 45)
        //assertThat(container, Is.`is`(not(contains(22))))//не работает
        assertThat(listOf("foo", "bar"), contains("foo", "bar"))
        //assertThat(3, not(2))
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @After
    fun tearDown() {
    }
}