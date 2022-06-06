package com.bignerdranch.android.currencyconverter


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule

import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//@LargeTest
@RunWith(AndroidJUnit4::class)
class ExchangeCurrencyTest {

    @Rule
    @JvmField
    val mActivityTestRule = ActivityTestRule(MainActivity::class.java)//ActivityScenarioRule

    @Test
    fun open_ExchangeFragment_and_write_zero_etLeft() {
        val refreshImgView = onView(
            allOf(
                withId(R.id.refreshImageView), isDisplayed()
            )
        )
        refreshImgView.perform(click())
        onView(allOf(withId(R.id.progressBar), isDisplayed()))
        //ХЗ КАК СДЕЛАТЬ ОЖИДАНИЕ ПОКАЗА НОВОГО ФРАГМЕНТА// есть такой вариант с таймаутом и кастомной функцией
        onView(isRoot()).perform(waitForView(R.id.exchangeFragmentRootCont, 5000))
        onView(
            allOf(
                withId(R.id.etCurrencyLeft), isDisplayed()
            )
        ).perform(typeText("0")) //.perform(replaceText("0"), closeSoftKeyboard())// печать текста в поле
        closeSoftKeyboard()//скрываем клавиатуру
        onView(
            allOf(
                withId(R.id.etCurrencyRight),
                isDisplayed()
            )
        ).check(matches(withText("0.0")))////проверяем изменение текста



    }
}
/*
//        onView(withId(R.id.loginEdit))
//            .perform(typeText("MyLogin"))
//            .check(matches(withText("MyLogin")));
Это и есть общая схема всех тестов с Espresso:
1.	Найти View, передав в метод onView объект Matcher.
2.	Выполнить какие-то действия над этой View, передав в метод perform объект ViewAction.
3.	Проверить состояние View, передав в метод check объект ViewAssertion. Обычно для создания объекта ViewAssertion используют метод matches, который принимает объект Matcher из пункта 1.

1.	withId, withText, withHint, withTagKey, … – Matcher.
2.	click, doubleClick, scrollTo, swipeLeft, typeText, … – ViewAction.
3.	matches, doesNotExist, isLeftOf, noMultilineButtons – ViewAssertion.
И, разумеется, если вам не хватит стандартных объектов, вы всегда можете создать свои собственные

Espresso – это очень умный фреймворк, который грамотно проверяет все элементы, при этом он может ждать некоторое время, пока выполнится определенное условие, что очень удобно, так как не всегда элементы на экране появляются мгновенно.

1. ПРОВЕРКА ОТКРЫТИЯ АКТИВИТИ ЧЕРЕЗ ИНТЕНТ (см. пример в doc) androidTestCompile ‘com.android.support.test.espresso:espresso-intents:2.2.2’
@Before
public void setUp() throws Exception {
   Intents.init();
}

@After
public void tearDown() throws Exception {
   Intents.release();
}
@Test public void test(){
   Intents.intended(hasComponent(CommitsActivity.class.getName()));


2. Изменим ActivityTestRule так, чтобы по умолчанию Activity не запускалась. Тогда мы сможем запускать ее вручную и, соответственно, делать это в нужный момент, например, после сохранения ошибки вместо токена:
@Rule
public final ActivityTestRule<RepositoriesActivity> mActivityRule = new ActivityTestRule<>(RepositoriesActivity.class, false, false);
private void launchActivity() {//Создадим метод, который позволит вручную запускать Activity:
   Context context = InstrumentationRegistry.getContext();
   Intent intent = new Intent(context, RepositoriesActivity.class);
   mActivityRule.launchActivity(intent);
}

RepositoryProvider.provideKeyValueStorage().saveValue(myValue);
launchActivity();
   onView(withId(R.id.empty)).check(matches(isDisplayed()));
   onView(withId(R.id.recyclerView)).check(matches(not(isDisplayed())));
 onView(withId(R.id.empty)).check(matches(withText(R.string.empty_repositories)));


3.	Приложение запускается с пустыми полями ввода и кнопкой с корректным текстом.
onView(withId(R.id.loginEdit)).check(matches(allOf(
           withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
           isFocusable(),
           isClickable(),
           withText("")
   )));
   onView(withId(R.id.passwordEdit)).check(matches(allOf(//методе мы для обоих полей ввода проверяем, что они отображаются, что на них можно навести фокус и нажать, и то, что в них отображается пустой текст. Для поля ввода пароля дополнительно проверяется, что они имеет тип ввода для пароля
           withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
           isFocusable(),
           isClickable(),
           withInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD),
           withText("")
   )));
   onView(withId(R.id.logInButton)).check(matches(allOf(
           withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE),
           isClickable(),
           withText(R.string.log_in)
   )));
Далее мы хотим проверить, что вводимый пользователем текст корректно отображается в полях ввода (например, мы не хотим, чтобы какой-то TextWatcher отслеживал изменения и менял текст логина / пароля в каких-то случаях)
   onView(withId(R.id.loginEdit)).perform(typeText("TestLogin"));
   closeSoftKeyboard();после каждого ввода данных нужно закрывать клавиатуру после ввода данных. Иначе клавиатура может закрыть собой элемент, который вы хотите найти, и тогда Espresso выдаст ошибку.
   onView(withId(R.id.passwordEdit)).perform(typeText("TestPassword"));
   closeSoftKeyboard();
   onView(withId(R.id.loginEdit)).check(matches(withText("TestLogin")));
   onView(withId(R.id.passwordEdit)).check(matches(withText("TestPassword")));

4.	При вводе пустого логина и нажатии на кнопку входа в поле ввода логина отображается ошибка. Напомним, что мы отображаем ошибку с помощью TextInputLayout
   onView(withId(R.id.loginEdit)).perform(typeText(""));
   closeSoftKeyboard();
   onView(withId(R.id.logInButton)).perform(click());
   //И теперь возникает вопрос – как именно проверить тот факт, что в TextInputLayout отобразилась ошибка? Разумеется, можно получить экземпляр Activity, найти нужный TextInputLayout и проверить текущую ошибку:
!!!   TextInputLayout inputLayout = (TextInputLayout) mActivityRule.getActivity().findViewById(R.id.loginInputLayout);
!!!   assertEquals(inputLayout.getResources().getString(R.string.error), inputLayout.getError());
Но так никогда не нужно делать при тестирование с Espresso, так как это нарушает все принципы работы этого фреймворка. Вместо этого мы могли бы описать свой Matcher для проверки. Тогда бы наш тест выглядел вот так:
    onView(withId(R.id.loginEdit)).perform(typeText(""));
   onView(withId(R.id.logInButton)).perform(click());
   onView(withId(R.id.loginInputLayout)).check(matches(withInputError(R.string.error)));
Метод withInputError – это статический метод для создания нашего объекта Matcher, который проверит, что ошибка, отображаемая в TextInputLayout, соответствует переданному идентификатору ресурса.
Как создать такой Matcher? Изначально, Matcher – это интерфейс, но его не нужно реализовывать напрямую, вместо этого следует либо наследоваться от класса BaseMatcher, либо использовать еще более продвинутый вариант с наследованием TypeSafeMatcher, как мы и поступим.
Создадим свой класс, который будет наследоваться от TypeSafeMatcher, и определим в нем статический метод withInputError, чтобы удобно использовать его в дальнейшем
СМОТРИ ПРИМЕР В doc файле


5. recyclerView
onView(withId(R.id.recyclerView))
           .perform(scrollToPosition(15))
           .perform(scrollToPosition(8))
           .perform(scrollToPosition(1))

onView(withId(R.id.recyclerView))
           .perform(actionOnItemAtPosition(14, click()));




*/
