package com.bignerdranch.android.currencyconverter.views

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.R
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.currencyconverter.MainActivity
import com.bignerdranch.android.currencyconverter.databinding.FragmentLoadCurrencyDataBinding
import com.bignerdranch.android.currencyconverter.viewmodels.LoadCurrencyDataViewModel

private const val TAG = "LoadCurrencyDataFragment"
//TODO: 3 добавить navigation
class LoadCurrencyDataFragment : Fragment() {
    //private lateinit var progressBar: ProgressBar
    private val loadCurrencyDataViewModel: LoadCurrencyDataViewModel by lazy {//вы не можете безопасно получить доступ к ViewModel до выполнения Activity.onCreate(...).//Использование lazy допускает применение свойства viewModel как val, а не var. Это здорово, потому что вам нужно захватить и сохранить ViewModel, лишь когда создается экземпляр activity, поэтому свойство viewModel получает значение только один раз. Что еще более важно, использование lazy означает, что расчет и назначение currencyConverterViewModel не будет происходить, пока вы не запросите доступ к currencyConverterViewModel впервые. Это хорошо, потому что вы не можете безопасно получить доступ к ViewModel до выполнения Activity.onCreate(...).
        //Как вы можете помнить, при первом запросе ViewModel для данного владельца жизненного цикла создается новый экземпляр ViewModel. Когда LoadCurrencyDataFragment уничтожается и создается заново из-за изменения конфигурации, например вращения, существующий ViewModel сохраняется.
        //ViewModelProviders.of(this).get(CurrencyConverterViewModel::class.java)//вызов ViewModelProviders.of(this) создает и возвращает ViewModelProvider, связанный с activity (в данном случае наверное с фрагментом). ViewModelProvider, в свою очередь, передает activity экземпляр ViewModel. ViewModelProvider работает как реестр ViewModel. Когда activity запрашивает QurrencyConverterViewModel после изменения конфигурации, экземпляр, который был создан изначально, возвращается.
        ViewModelProvider(this, defaultViewModelProviderFactory).get(LoadCurrencyDataViewModel::class.java)
    }
    private var _viewBinding: FragmentLoadCurrencyDataBinding? = null
    private val viewBinding get() = _viewBinding!!// This property is only valid between onCreateView and onDestroyView.

    companion object {
        fun newInstance() = LoadCurrencyDataFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //bindingView = FragmentLoadCurrencyDataBinding.inflate(layoutInflater)

        //val currencyLifeData: LiveData<List<Valute>> = CurrencyDataFetcher().fetchData()//Каждый раз при повороте устройства выполняется новый сетевой запрос Поскольку каждый раз при вращении фрагмент уничтожается и воссоздается заново
        //Вместо того чтобы выдавать новый веб-запрос каждый раз, когда происходит изменение конфигурации, нужно получить данные лишь раз при запуске и отображении фрагмента на экране. Тогда вы можете разрешить веб-запросу продолжить выполнение при изменении конфигурации путем кэширования результатов в памяти. Наконец, вы можете использовать результаты кэширования по мере их доступности, вместо того чтобы делать новый запрос. ViewModel — это как раз то, что поможет вам с этой задачей.
//        currencyLifeData.observe(this, Observer { currencyItems -> //Т.е. это надо переносить во ViewModel
//            Log.d(TAG, "Response received: ${currencyItems.size}")
//        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //val view = inflater.inflate(R.layout.fragment_load_currency_data, container, false)  // Inflate the layout for this fragment
        //progressBar = view.findViewById(R.id.progressBar)
        _viewBinding = FragmentLoadCurrencyDataBinding.inflate(inflater, container, false)
        return viewBinding.root
        //return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.progressBar.visibility = View.INVISIBLE // возможны NullPointerException если использовать ViewBinding in OnCreate,
        viewBinding.refreshImageView.visibility = View.VISIBLE

        viewBinding.refreshImageView.setOnClickListener{
//            refreshImgView.visibility = View.INVISIBLE
//            progressBar.visibility = View.VISIBLE
            viewBinding.progressBar.visibility = View.VISIBLE
            viewBinding.refreshImageView.visibility = View.INVISIBLE
            loadCurrencyDataViewModel.startDataLoading().observe(viewLifecycleOwner, Observer {// теперь мы взаимодействуем с viewmodel, а не с моделью: просим начать загрузку данных и смотрим за флагом окончания загрузки
                Toast.makeText(this.context, "Курсы валют обновлены", Toast.LENGTH_SHORT).show()// как только данные загружены мы показываем тост и запускаем новый фрагмент
            findNavController().navigate(com.bignerdranch.android.currencyconverter.R.id.action_loadCurrencyDataFragment_to_currencyExchangeFragment)
            //(activity as MainActivity).loadNewFragment(CurrencyExchangeFragment.newInstance())
            })

//            //Внедрение наблюдения в функцию onViewCreated(...) гарантирует, что виджеты пользовательского интерфейса и другие объекты будут к этому готовы. Это также гарантирует, что будет правильно обрабатываться сценарий уничтожения фрагмента. В этом сценарии при повторном присоединении фрагмента представление будет создано заново, и при создании в новое представление будет добавлено наблюдение за LiveData.
//            loadCurrencyDataViewModel.currencyLifeData.observe(viewLifecycleOwner, Observer {//Функция LiveData.observe(LifecycleOwner,Observer) используется для регистрации наблюдателя за экземпляром LiveData и связи наблюдения с жизненным циклом другого компонента. Второй параметр функции observe(...) — это реализация Observer. Этот объект отвечает за реакцию на новые данные из LiveData//Передача ViewLifecycleOwner в качестве параметра LifecycleOwner функции LiveData.observe(LifecycleOwner,Observer) гарантирует, что объект LiveData удалит наблюдателя при уничтожении представления фрагмента.
//                    currencyItems ->//
//                // Обновить данные, поддерживающие представление утилизатора
//                Toast.makeText(this.context, "Курсы валют обновлены", Toast.LENGTH_SHORT).show()
//                Log.d(TAG, "Response received: ${currencyItems.size}")
//                //dataRepository.currencyData = currencyItems
//                //callbacks?.onDataRecieved()
//                (activity as MainActivity).loadNewFragment(CurrencyExchangeFragment.newInstance())
//            })
        }
    }


//    private var callbacks: Callbacks? = null
//    interface Callbacks{//Для передачи функциональности обратно хостингу в фрагменте обычно определяется пользовательский интерфейс обратного вызова под именем Callbacks
//        fun onDataRecieved()//Этот интерфейс определяет работу, которую должна выполнить хост-activity. Любая activity, которая будет содержать фрагмент, должна реализовывать этот интерфейс.
//    }//С помощью интерфейса обратного вызова фрагмент способен вызывать функции, связанные с его хост-activity, без необходимости знать что-либо о том, какая activity является хостом.
//
//    override fun onAttach(context: Context) {//Fragment.onAttach(Context) вызывается, когда фрагмент прикреплется к activity
//        super.onAttach(context)
//        callbacks = context as Callbacks?//. Так как LoadCurrencyDataFragment размещается в activity, то объект Context, переданный onAttach(...), является экземпляром activity, в которой размещен фрагмент. Помните, что Activity является подклассом Context
//    }

//    override fun onDetach() {
//        super.onDetach()
//        callbacks = null//Здесь переменную устанавливают равной null, так как в дальнейшем вы не сможете получить доступ к activity или рассчитывать на то, что она будет продолжать существовать.
//        Log.d(TAG, "Detach")
//    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Stop")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "Pause")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "ConfChanged")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Destroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        Log.d(TAG, "DestroyView")
    }

}