package com.bignerdranch.android.currencyconverter

import com.bignerdranch.android.currencyconverter.models.Valute
import com.bignerdranch.android.currencyconverter.views.SimpleClass
import com.bignerdranch.android.currencyconverter.views.ValuteItemViewModel
import junit.framework.Assert.assertNotNull
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.Is
import org.junit.Before

import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mockito.*

//Избегай внутри @Test УСЛОВИЙ - if/when и др.  Делай на каждый случай отдельный тест

//Избегай внутри @Test ЦИКЛОВ Если тест не прошел на определенном элементе, то можете ли вы быть уверены, что все оставшиеся элементы, до которых for не дошел, также не вызовут ошибки?

//Не перегружай @Test метод, выноси лишний код в функции


class ValuteItemViewModelTest {

    private lateinit var valute: Valute
    private lateinit var subject: ValuteItemViewModel // даем имя subject т.к. это наш тестируемый объект
    private lateinit var simpleClass: SimpleClass

    @Before//Код, содержащийся в функции с пометкой @Before, будет выполнен один раз перед выполнением каждого теста.
    fun setUp() {
        valute = Valute(id="123", numCode=4, charCode = "RUB", nominal = 1, name = "Российский рубль", value = "81.25")
        subject = ValuteItemViewModel()
        subject.valute = valute

        simpleClass = mock(SimpleClass::class.java)//создать объект для имитации с помощью Mockito
        //можно задавать возвращаемые параметры (не подменять!! а именно задать возвращаемые значения т.к. если их не задавать то возвращаются default value: в случае с string это null, а int это 0)
        `when`(simpleClass.funA()).thenReturn(576)//Mockito.when
        `when`(simpleClass.funB()).thenReturn("xyz")
    }

    /*ПРОВЕРКА СРАБАТЫВАНИЯ ВЫЗОВА ФУНКЦИИ В ДРУГОМ КЛАССЕ (чтобы не сталкнуть с ошибками в другом классе создаем его имитацию,
которая имеет все те же функции, что и реальный класс но ни одна из этих функций ничего не делает)*/
    @Test
    fun funCall_nothing_inSimpleClassTEST() {/*ТУТ С Mockito ТЕСТИРУЕТСЯ НЕ ПРОСТО ВЫЗОВ ФУНКЦИИ ИЗ КЛАССА, А ВЫЗОВ ФУНКЦИИ ИЗ ОБЪЕКТА ПЕРЕДАННОГО В КЛАСС*/
        subject.simpleClass = simpleClass
        subject.callSimpl()
        verify(simpleClass).nothingFun(true)//Все объекты Mockito отслеживают, какие из их функций вызывались и какие параметры передавались при каждом вызове. Функция verify(Object) объекта Mockito проверяет, вызывались ли эти функции так, как вы ожидали.
        //Вызов verify(simpleClass) означает: «Я хочу проверить, что для simpleClass была вызвана функция». Следующий вызов функции интерпретируется так: «Проверить, что эта функция был вызвана именно так».
    }

    @Test
    fun funCall_funB_inSimpleClassTEST(){
        subject.simpleClass = simpleClass
        subject.callFunB()//эта функция вызывает funB и сохраняет значение в subject.valFunB
        verify(simpleClass).funB() // проверяем вызвалась ли функция
        assertThat(subject.valFunB, `is`("xyz"))// в subject.valFunB записывается заданное нами значение (реальная функция возвращает  "abc", а без задания значения мок функция возращает null для стринг, 0 для инт)

        /*СКОЛЬКО РАЗ ВЫЗЫВАЛСЯ МЕТОД*/
        verify(simpleClass, times(0)).funA()// проверяем вызвалась ли функция 0 РАЗ
        //verifyNoMoreInteractions(simpleClass)//проверяем что у mock-объекта не был вызван ни один метод
    }

    @Test
    fun testCreated(){
        assertNotNull(subject)
    }

    @Test/*ПРОВЕРКА НА ПРАВИЛЬНОСТЬ УСТАНОВКИ ЗНАЧЕНИЙ ПОЛЯМ КЛАССА */
    fun exposesValuteNameAsCurrencyName(){
        assertThat(subject.currencyName, Is.`is`(valute.name))//Положим, что свойство currencyName субъекта будет тем же, что и имя valute. Если две функции возвращают разные значения, тест не пройдет.
    }

    @Test/*ПРОВЕРКА НА ПРАВИЛЬНОСТЬ УСТАНОВКИ ЗНАЧЕНИЙ ПОЛЯМ КЛАССА */
    fun exposesValuteCharCodeAsCurrencyCode(){
        assertThat(subject.currencyCode, Is.`is`(valute.charCode))
        //assertThat(subject.currencyCode).isEqualTo(valute.charCode)
    }

//    @Test
//    fun fileInstallationTest(){
//        val names = arrayListOf("first", "second", "third")
//        saveMyFiles(names)
////        for (name in names){
////            assertPackageExists(name)
////        }
//        assertThat(names, everyItem(Is.`is`(installedPackage)))
//    }
}
