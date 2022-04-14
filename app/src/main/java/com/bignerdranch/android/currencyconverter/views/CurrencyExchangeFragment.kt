package com.bignerdranch.android.currencyconverter.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.currencyconverter.MainActivity
import com.bignerdranch.android.currencyconverter.databinding.FragmentCurrencyExchangeBinding
import com.bignerdranch.android.currencyconverter.viewmodels.CurrencyExchangeViewModel
import java.util.*

private const val TAG = "CurrencyExchangeFragment"

class CurrencyExchangeFragment : Fragment(){
    private val currencyExchangeViewModel: CurrencyExchangeViewModel by lazy {//Использование lazy допускает применение свойства quizViewModel как val, а не var. Это здорово, потому что вам нужно захватить и сохранить CurrencyConverterViewModel, лишь когда создается экземпляр activity, поэтому currencyConverterViewModel получает значение только один раз. Что еще более важно, использование lazy означает, что расчет и назначение currencyConverterViewModel не будет происходить, пока вы не запросите доступ к currencyConverterViewModel впервые. Это хорошо, потому что вы не можете безопасно получить доступ к ViewModel до выполнения Activity.onCreate(...).
        //Как вы можете помнить, при первом запросе ViewModel для данного владельца жизненного цикла создается новый экземпляр ViewModel. Когда LoadCurrencyDataFragment уничтожается и создается заново из-за изменения конфигурации, например вращения, существующий ViewModel сохраняется.
        //ViewModelProviders.of(this).get(CurrencyConverterViewModel::class.java)//вызов ViewModelProviders.of(this) создает и возвращает ViewModelProvider, связанный с activity (в данном случае наверное с фрагментом). ViewModelProvider, в свою очередь, передает activity экземпляр ViewModel. ViewModelProvider работает как реестр ViewModel. Когда activity запрашивает QurrencyConverterViewModel после изменения конфигурации, экземпляр, который был создан изначально, возвращается.
        ViewModelProvider(this, defaultViewModelProviderFactory).get(CurrencyExchangeViewModel::class.java)
    }
    private var _viewBinding: FragmentCurrencyExchangeBinding? = null
    private val vb get() = _viewBinding!!// This property is only valid between onCreateView and onDestroyView.

    companion object{
        fun newInstance() = CurrencyExchangeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {// Функция Fragment.onCreate(Bundle?) объявлена открытой, а функции Kotlin по умолчанию объявляются публичными, если при объявлении не указан модификатор видимости, тогда как функция Activity.onCreate(Bundle?) защищена. Fragment.onCreate(Bundle?) и другие функции жизненного цикла Fragment должны быть открытыми, потому что они будут вызываться произвольной activity, которая станет хостом фрагмента
        super.onCreate(savedInstanceState)
    }

//Состояние виджета восстанавливается после функции onCreateView(...) и перед функцией onStart().
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//      val view = inflater.inflate(R.layout.fragment_currency_exchange, container, false) //явно заполняем представление фрагмента, Второй параметр определяет родителя представления, что обычно необходимо для правильной настройки виджета. Третий параметр указывает, нужно ли включать заполненное представление в родителя. Мы сделаем это позже в activity
//      etCurrencyLeft = view.findViewById(R.id.etCurrencyLeft)// as EditText
        _viewBinding = FragmentCurrencyExchangeBinding.inflate(inflater, container, false)
        return vb.root//заполненный объект View возвращается хостactivity
        //return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateCharCodes()// в этом методе мы имеем ссылку на ViewModel поскольку она lazy, то updateCharCodes создается ViewModel с рандомными Valute для которых мы обновляем поля

        //Слушатели, которые реагируют только на взаимодействие с пользователем, такие как OnClickListener, на них не влияет настройка данных в виджете. То есть они не срабатывают при повороте, поэтому их можно настроить все в onCreate(...) — перед любым восстановлением состояния.
        vb.swapImgView.setOnClickListener{// здесь можно так сделать потому-что
            currencyExchangeViewModel.swapCurrency()//меняем Valute внутри ViewModel
            updateCharCodes()// обновляем аббревиатуры
            if(vb.etCurrencyLeft.isFocused){//пересчитываем значения
                vb.etCurrencyLeft.requestFocus()
                vb.etCurrencyLeft.text = vb.etCurrencyRight.text
            }else{
                vb.etCurrencyRight.requestFocus()
                vb.etCurrencyRight.text = vb.etCurrencyLeft.text
            }
        }
        vb.tvCharCodeLeft.setOnClickListener({
            currencyExchangeViewModel.setChangingValute(currencyExchangeViewModel.firstCur, true)//save the currency that needs to be changed //set a flag variable pointing to the variable that will be changed
            (activity as MainActivity).loadNewFragment(ChangeCurrencyFragment.newInstance())
        })
        vb.tvCharCodeRight.setOnClickListener({
            currencyExchangeViewModel.setChangingValute(currencyExchangeViewModel.secondCur, false)
            (activity as MainActivity).loadNewFragment(ChangeCurrencyFragment.newInstance())
        })
    }
    override fun onStart() {
        super.onStart()
        //слушатель TextWatcher настраивается в функции onStart(). Некоторые слушатели срабатывают не только при взаимодействии с ними, но и при восстановлении состояния виджета, например при повороте.
        vb.etCurrencyLeft.addTextChangedListener(object: TextWatcher{//Мы создаем анонимный класс, который реализует интерфейс слушателя TextWatcher.
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //p0.toString()
                if(vb.etCurrencyLeft.text.toString() == "")//if EditText is empty setting 0 by default
                    vb.etCurrencyLeft.setText("0")
                if(Character.isDigit(vb.etCurrencyLeft.text[0])) {//if the EditText has a number, then convert this value and set to the opposite EditText
                    if(vb.etCurrencyLeft.hasFocus()){
                        vb.etCurrencyRight.setText(String.format(Locale.getDefault(),"%.3f",
                            convert(vb.etCurrencyLeft.text.toString().replace(',', '.').toDouble().toDouble(), // все данные для convert берем из viewModel
                                currencyExchangeViewModel.firstCur.getValue(), currencyExchangeViewModel.firstCur.nominal.toDouble(), currencyExchangeViewModel.secondCur.getValue(), currencyExchangeViewModel.firstCur.nominal.toDouble())))
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        vb.etCurrencyRight.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //p0.toString()
                if(vb.etCurrencyRight.text.toString() == "")
                    vb.etCurrencyRight.setText("0")
                if(Character.isDigit(vb.etCurrencyRight.text[0])) {////if the EditText has a number, then convert this value and set to the opposite EditText
                    if(vb.etCurrencyRight.hasFocus()){
                        vb.etCurrencyLeft.setText(String.format(Locale.getDefault(),"%.3f",
                            convert(vb.etCurrencyRight.text.toString().replace(',', '.').toDouble(),
                                currencyExchangeViewModel.secondCur.getValue(),  currencyExchangeViewModel.secondCur.nominal.toDouble(), currencyExchangeViewModel.firstCur.getValue(), currencyExchangeViewModel.firstCur.nominal.toDouble())))
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        ifCurrencyWasChanged() // проверяем не были ли изменены Valute
    }

    private fun ifCurrencyWasChanged(){
        if(currencyExchangeViewModel.isLeft == true){//if the currency value was changed then field updates
            currencyExchangeViewModel.setNewValute()//сохраняем в viewModel новое значение для измененной Valute
            currencyExchangeViewModel.valuteChanged()//remove the flag variable
            vb.etCurrencyLeft.requestFocus()
            vb.etCurrencyLeft.text = vb.etCurrencyLeft.text// этим вызываем onTextChanged который пересчитывает значения для измененной валюты
            updateCharCodes()
        }
        else if (currencyExchangeViewModel.isLeft == false){
            currencyExchangeViewModel.setNewValute()
            currencyExchangeViewModel.valuteChanged()
            vb.etCurrencyRight.requestFocus()
            vb.etCurrencyRight.text = vb.etCurrencyRight.text
            updateCharCodes()
        }
    }

    private fun convert(curAmount:Double, curValueA:Double, curNomianalA:Double, curValueB:Double, curNominalB:Double):Double{// currency converter
        val rub: Double = curAmount*curValueA/curNomianalA//exchange rates are determined relative to the ruble, so first we find the rubbles
        return rub*curNominalB/curValueB//get money in another currency
    }

    private fun updateCharCodes(){
        vb.tvCharCodeLeft.text = currencyExchangeViewModel.firstCur.charCode
        vb.tvCharCodeRight.text = currencyExchangeViewModel.secondCur.charCode
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}