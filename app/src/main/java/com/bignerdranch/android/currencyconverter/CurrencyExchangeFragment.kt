package com.bignerdranch.android.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val TAG = "CurrencyExchangeFragment"

class CurrencyExchangeFragment : Fragment(){
    //private val dataRepository: DataRepository = DataRepository.get()// везде где использую свойства репозитория их нужно перенести в view model и из него ссылатся на репозиторий
    private lateinit var currencyLeft: EditText
    private lateinit var currencyRight: EditText
    private lateinit var currencyCharCodeLeft: TextView
    private lateinit var currencyCharCodeRight: TextView
    private lateinit var swapCurrency: ImageView
    private val currencyExchangeViewModel: CurrencyExchangeViewModel by lazy {//Использование lazy допускает применение свойства quizViewModel как val, а не var. Это здорово, потому что вам нужно захватить и сохранить CurrencyConverterViewModel, лишь когда создается экземпляр activity, поэтому currencyConverterViewModel получает значение только один раз. Что еще более важно, использование lazy означает, что расчет и назначение currencyConverterViewModel не будет происходить, пока вы не запросите доступ к currencyConverterViewModel впервые. Это хорошо, потому что вы не можете безопасно получить доступ к ViewModel до выполнения Activity.onCreate(...).
        //Как вы можете помнить, при первом запросе ViewModel для данного владельца жизненного цикла создается новый экземпляр ViewModel. Когда LoadCurrencyDataFragment уничтожается и создается заново из-за изменения конфигурации, например вращения, существующий ViewModel сохраняется.
        //ViewModelProviders.of(this).get(CurrencyConverterViewModel::class.java)//вызов ViewModelProviders.of(this) создает и возвращает ViewModelProvider, связанный с activity (в данном случае наверное с фрагментом). ViewModelProvider, в свою очередь, передает activity экземпляр ViewModel. ViewModelProvider работает как реестр ViewModel. Когда activity запрашивает QurrencyConverterViewModel после изменения конфигурации, экземпляр, который был создан изначально, возвращается.
        ViewModelProvider(this, defaultViewModelProviderFactory).get(CurrencyExchangeViewModel::class.java)
    }

    companion object{
        fun newInstance() = CurrencyExchangeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {// Функция Fragment.onCreate(Bundle?) объявлена открытой, а функции Kotlin по умолчанию объявляются публичными, если при объявлении не указан модификатор видимости, тогда как функция Activity.onCreate(Bundle?) защищена. Fragment.onCreate(Bundle?) и другие функции жизненного цикла Fragment должны быть открытыми, потому что они будут вызываться произвольной activity, которая станет хостом фрагмента
        super.onCreate(savedInstanceState)
    }

//Состояние виджета восстанавливается после функции onCreateView(...) и перед функцией onStart().
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_currency_exchange, container, false) //явно заполняем представление фрагмента, Второй параметр определяет родителя представления, что обычно необходимо для правильной настройки виджета. Третий параметр указывает, нужно ли включать заполненное представление в родителя. Мы сделаем это позже в activity
        currencyLeft = view.findViewById(R.id.etCurrencyLeft)// as EditText
        currencyRight = view.findViewById(R.id.etCurrencyRight)
        currencyCharCodeLeft = view.findViewById(R.id.tvCharCodeLeft)
        currencyCharCodeRight = view.findViewById(R.id.tvCharCodeRight)
        updateCharCodes()
        swapCurrency = view.findViewById(R.id.swapImgView)
        //Слушатели, которые реагируют только на взаимодействие с пользователем, такие как OnClickListener, на них не влияет настройка данных в виджете. То есть они не срабатывают при повороте, поэтому их можно настроить все в onCreate(...) — перед любым восстановлением состояния.
        swapCurrency.setOnClickListener{// здесь можно так сделать потому-что
            currencyExchangeViewModel.swapCurrency()
            updateCharCodes()
            if(currencyLeft.isFocused){
                currencyLeft.requestFocus()
                currencyLeft.text = currencyRight.text
            }else{
                currencyRight.requestFocus()
                currencyRight.text = currencyLeft.text
            }
        }
        currencyCharCodeLeft.setOnClickListener({
            currencyExchangeViewModel.setChangingValute(currencyExchangeViewModel.firstCur, true)//save the currency that needs to be changed //set a flag variable pointing to the variable that will be changed
            (activity as MainActivity).loadNewFragment(ChangeCurrencyFragment.newInstance())
        })
        currencyCharCodeRight.setOnClickListener({
            currencyExchangeViewModel.setChangingValute(currencyExchangeViewModel.secondCur, false)
            (activity as MainActivity).loadNewFragment(ChangeCurrencyFragment.newInstance())
        })
        return view//заполненный объект View возвращается хостactivity
    }

    override fun onStart() {
        super.onStart()
        //слушатель TextWatcher настраивается в функции onStart(). Некоторые слушатели срабатывают не только при взаимодействии с ними, но и при восстановлении состояния виджета, например при повороте.
        currencyLeft.addTextChangedListener(object: TextWatcher{//Мы создаем анонимный класс, который реализует интерфейс слушателя TextWatcher.
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //p0.toString()
                if(currencyLeft.text.toString() == "")//if EditText is empty setting 0 by default
                    currencyLeft.setText("0")
                if(Character.isDigit(currencyLeft.text[0])) {//if the EditText has a number, then convert this value and set to the opposite EditText
                    if(currencyLeft.hasFocus()){
                        currencyRight.setText(String.format(Locale.getDefault(),"%.3f", convert(currencyLeft.text.toString().replace(',', '.').toDouble().toDouble(), currencyExchangeViewModel.firstCur.getValue(), currencyExchangeViewModel.firstCur.nominal.toDouble(), currencyExchangeViewModel.secondCur.getValue(), currencyExchangeViewModel.firstCur.nominal.toDouble())))
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        currencyRight.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //p0.toString()
                if(currencyRight.text.toString() == "")
                    currencyRight.setText("0")
                if(Character.isDigit(currencyRight.text[0])) {////if the EditText has a number, then convert this value and set to the opposite EditText
                    if(currencyRight.hasFocus()){
                        currencyLeft.setText(String.format(Locale.getDefault(),"%.3f", convert(currencyRight.text.toString().replace(',', '.').toDouble(), currencyExchangeViewModel.secondCur.getValue(),  currencyExchangeViewModel.secondCur.nominal.toDouble(), currencyExchangeViewModel.firstCur.getValue(), currencyExchangeViewModel.firstCur.nominal.toDouble())))
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        ifCurrencyWasChanged()
    }

    private fun ifCurrencyWasChanged(){
        if(currencyExchangeViewModel.isLeft == true){//if the currency value was changed then field updates
            currencyExchangeViewModel.setNewValute()
            currencyExchangeViewModel.valuteChanged()//remove the flag variable
            currencyLeft.requestFocus()
            currencyLeft.text = currencyLeft.text// этим вызываем onTextChanged который пересчитывает значения для измененной валюты
            updateCharCodes()
        }
        else if (currencyExchangeViewModel.isLeft == false){
            currencyExchangeViewModel.setNewValute()
            currencyExchangeViewModel.valuteChanged()
            currencyRight.requestFocus()
            currencyRight.text = currencyRight.text
            updateCharCodes()
        }
    }

    private fun convert(curAmount:Double, curValueA:Double, curNomianalA:Double, curValueB:Double, curNominalB:Double):Double{// currency converter
        val rub: Double = curAmount*curValueA/curNomianalA//exchange rates are determined relative to the ruble, so first we find the rubbles
        return rub*curNominalB/curValueB//get money in another currency
    }

    private fun updateCharCodes(){
        currencyCharCodeLeft.text = currencyExchangeViewModel.firstCur.charCode
        currencyCharCodeRight.text = currencyExchangeViewModel.secondCur.charCode
    }
}