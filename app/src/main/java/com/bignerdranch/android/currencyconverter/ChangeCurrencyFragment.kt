package com.bignerdranch.android.currencyconverter

import android.app.ActionBar
import android.app.Activity
import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.currencyconverter.BR.viewModel
import com.bignerdranch.android.currencyconverter.databinding.ChangeCurrencyFragmentBinding
import com.bignerdranch.android.currencyconverter.databinding.RecycerViewItemBinding
import java.util.*

private const val TAG = "ChangeCurrencyFragment"

class ChangeCurrencyFragment : Fragment() {
    private val dataRepository = DataRepository.get()// ссылка на репозиторий
    private var newValute: Valute? = dataRepository.oldValute
    private lateinit var changeCurrencyRecyclerView: RecyclerView//RecyclerView является подклассом ViewGroup. Он отображает список дочерних объектов View, называемых представлениями элементов. Это один объект из списка данных представления — утилизатор
    private var currencyAdapter: CurrencyAdapter? = null
    private var actionBar: androidx.appcompat.app.ActionBar? = null

    companion object {
        fun newInstance() = ChangeCurrencyFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)//сообщаем FragmentManager, что экземпляр ChangeCurrencyFragment должен получать обратные вызовы меню.//FragmentManager отвечает за вызов Fragment.onCreateOptionsMenu(Menu,MenuInflater) при получении activity обратного вызова onCreateOptionsMenu(...) от ОС. Вы должны явно указать FragmentManager, что фрагмент должен получить вызов onCreateOptionsMenu(...). Для этого вызывается следующая функция Fragment: setHasOptionsMenu(hasMenu: Boolean)
        actionBar = (activity as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_change_currency, menu)//Мы вызываем функцию MenuInflater.inflate(int,Menu) и передаем идентификатор ресурса своего файла меню. Вызов заполняет экземпляр Menu командами, определенными в файле
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("TAG", item.itemId.toString() + " = ${activity}")
        when(item.itemId){
            android.R.id.home->{//on back button
                activity?.onBackPressed()
            }
            R.id.saveMenuBtn ->{// on save button
                //saving the value for the selected currency
                dataRepository.newValute = newValute
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: ChangeCurrencyFragmentBinding? = // это класс привязки сгенерированный библиотекой привязки данных. вместо заполнения иерархии представлений с использованием setContentView(int) мы заполним экземпляр ChangeCurrencyFragmentBinding. ChangeCurrencyFragmentBinding сохраняет иерархию представлений в свойстве root. Кроме того, сохраняются именованные ссылки для всех представлений, помеченных в файле макета атрибутом android:id.
           // Таким образом, класс ChangeCurrencyFragmentBinding сохраняет две ссылки: root для всего макета и recyclerView для RecyclerView. Конечно, в нашем макете только одно представление, поэтому обе ссылки указывают на одно представление: RecyclerView.
        //ДЛЯ ПРИВЯЗКИ К ACTIVITY:  DataBindingUtil.setContentView(activity as MainActivity, R.layout.change_currency_fragment)
        DataBindingUtil.inflate(inflater, R.layout.change_currency_fragment, container, false)
            binding?.changeCurrencyRecyclerView?.apply {////Мы использовали привязку данных для настройки recycler view.
            layoutManager = LinearLayoutManager(context)//RecyclerView не отображает элементы на самом экране. Он передает эту задачу объекту LayoutManager. LayoutManager располагает каждый элемент, а также определяет, как работает прокрутка.  Вы используете LinearLayoutManager, которая будет позиционировать элементы в списке по вертикали.
            adapter = CurrencyAdapter(dataRepository.currencyData)
        }

        return binding?.root
    }

    private inner class CurrencyHolder(private val binding: RecycerViewItemBinding): RecyclerView.ViewHolder(binding.root){

        init {//Остается подключить модель представления. Создайте объект SoundViewModel и присоедините его к классу привязки. Затем добавьте функцию привязки в Holder.
            binding.viewModel = ValuteItemViewModel()//viewModel это то свойство которое мы определили в макете класса привязки
        }//В конструкторе создается и присоединяется модель представления.

        //в функции bind обновляются данные, с которыми работает модель представления.
        fun bind(currency: Valute){//В этой функции нужно кэшировать привязываемые currency в свойства и присвоить текстовые значения свойствам TextView
            binding.apply {
                viewModel?.valute = currency
                executePendingBindings()//в данном случае данные привязки обновляются в виджете RecyclerView, который обновляет представления с очень высокой скоростью. Вызывая эти функции, вы приказываете макету обновить себя немедленно, вместо того чтобы ожидать одну-две миллисекунды. Таким образом обеспечивается быстрота реакции RecyclerView синхронно с его RecyclerView.Adapter.
            }
        }

        private val checkMark: ImageView = itemView.findViewById(R.id.recyclerCheck_mark)
        fun setChecked(p0: Boolean) {
            if (p0 == true)
                checkMark.visibility = View.VISIBLE
            else
                checkMark.visibility = View.INVISIBLE
        }
    }


//   private inner class CurrencyAdapter(var currencies: List<Valute>): RecyclerView.Adapter<CurrencyHolder>(){
private inner class CurrencyAdapter(var currencies: List<Valute>): RecyclerView.Adapter<CurrencyHolder>(){
        var selectedPosition: Int  = -1
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {//отвечает за создание представления на дисплее, оборачивает его в холдер и возвращает результат.
//            val view = layoutInflater.inflate(R.layout.recycer_view_item, parent, false)
//            return CurrencyHolder(view)
            val binding = DataBindingUtil.inflate<RecycerViewItemBinding>(layoutInflater, R.layout.recycer_view_item, parent, false)
            return CurrencyHolder(binding)
        }

        override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {//отвечает за заполнение данного холдера holder из данной позиции position.
            val currency = currencies[position]
            holder.bind(currency)

            holder.setChecked(false)
            if (currency.charCode == newValute?.charCode){ //dataRepository.newValute?.charCode){
                holder.setChecked(true)
                selectedPosition = position
            }
            holder.itemView.setOnClickListener {
                newValute = currency
                notifyItemChanged(selectedPosition)// уведомляем ранее выбранный элемент (вызываем для него onBindViewHolder)
                selectedPosition = holder.absoluteAdapterPosition// сохраняем текущий выбранный элемент
                notifyItemChanged(selectedPosition)// обновляем текущий элемент
            }
//            holder.apply {
//                //currencyName.text = currency.name//Мы рекомендуем поместить весь код, который будет выполнять привязку, внутрь CurrencyHolder (для этого все свойства в CurrencyHolder делаем приватными)
//            }
        }

        override fun getItemCount() = currencies.count()
    }

    override fun onDestroy() {
        super.onDestroy()
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

}





/*  ДО ПЕРЕВОДА НА MVVM   */


//class ChangeCurrencyFragment : Fragment() {
//    private val dataRepository = DataRepository.get()// ссылка на репозиторий
//    private lateinit var changeCurrencyRecyclerView: RecyclerView//RecyclerView является подклассом ViewGroup. Он отображает список дочерних объектов View, называемых представлениями элементов. Это один объект из списка данных представления — утилизатор
//    private var currencyAdapter: CurrencyAdapter? = null
//
//    companion object {
//        fun newInstance() = ChangeCurrencyFragment()
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val view = inflater.inflate(R.layout.change_currency_fragment, container, false)
//        changeCurrencyRecyclerView = view.findViewById(R.id.changeCurrencyRecyclerView) as RecyclerView
//        changeCurrencyRecyclerView.layoutManager = LinearLayoutManager(context)//RecyclerView не отображает элементы на самом экране. Он передает эту задачу объекту LayoutManager. LayoutManager располагает каждый элемент, а также определяет, как работает прокрутка.  Вы используете LinearLayoutManager, которая будет позиционировать элементы в списке по вертикали.
//        currencyAdapter = CurrencyAdapter(dataRepository.currencyData)
//        changeCurrencyRecyclerView.adapter = currencyAdapter
//        return view
//    }
//
//    //    //RecyclerView ожидает, что элемент представления будет обернут в экземпляр ViewHolder.
//    private inner class CurrencyHolder(view: View): RecyclerView.ViewHolder(view)////В конструкторе CurrencyHolder мы берем представление для закрепления. Вы сразу же передаете его в качестве аргумента в конструктор классов RecyclerView.ViewHolder. Базовый класс ViewHolder будет закрепляться на свойство под названием itemView. RecyclerView никогда не создает объекты View сам по себе. Он всегда создает ViewHolder, которые выводят свои itemView
//    {// View.OnClickListener//Так как каждый View имеет связанный с ним ViewHolder, вы можете создать OnClickListener для всех View.
//        private lateinit var valute: Valute
//        private val currencyName: TextView = itemView.findViewById(R.id.recyclerCurName)//ViewHolder хранит ссылку на представление элемента
//        private val currencyCode: TextView = itemView.findViewById(R.id.recyclerCurCode)
//        private val checkMark: ImageView = itemView.findViewById(R.id.recyclerCheck_mark)
//
//        //        init {
////            itemView.setOnClickListener(this)
////        }
////        override fun onClick(p0: View?) {
////            dataRepository.newValute = valute
////            setChecked(true)
////        }
//        fun bind(currency: Valute){//В этой функции нужно кэшировать привязываемые currency в свойства и присвоить текстовые значения свойствам TextView
//            this.valute = currency
//            currencyName.text = valute.name
//            currencyCode.text = valute.charCode
//        }
//
//        fun setChecked(p0: Boolean) {
//            if (p0 == true)
//                checkMark.visibility = View.VISIBLE
//            else
//                checkMark.visibility = View.INVISIBLE
//        }
//
//    }
//
//    //    //Класс RecyclerView не создает ViewHolder сам по себе. Вместо этого используется адаптер. Адаптер представляет собой объект контроллера, который находится между RecyclerView и наборами данных, которые отображает RecyclerView.
////    //Адаптер выполняет следующие функции: • создание необходимых ViewHolder по запросу; • связывание ViewHolder с данными из модельного слоя.
////    //recycler view выполняет следующие функции: • запрашивает адаптер на создание нового ViewHolder; • запрашивает адаптер привязать ViewHolder к элементу данных на этой позиции.
//    private inner class CurrencyAdapter(var currencies: List<Valute>): RecyclerView.Adapter<CurrencyHolder>(){
//        var selectedPosition: Int  = -1
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {//отвечает за создание представления на дисплее, оборачивает его в холдер и возвращает результат.
//            val view = layoutInflater.inflate(R.layout.recycer_view_item, parent, false)
//            return CurrencyHolder(view)
//        }
//        //Вместо того чтобы создать 100 объектов View, он создает их столько, сколько нужно для заполнения экрана. Когда представление пропадает с экрана, RecyclerView использует его заново, а не выбрасывает. Тем самым он оправдывает свое название — перерабатывает объекты.
////В связи с этим функция onCreateViewHolder(ViewGroup,Int) будет вызываться намного реже, чем onBindViewHolder(ViewHolder,Int). Когда создано достаточно объектов ViewHolder, RecyclerView перестает вызывать onCreateViewHolder(...). Вместо этого он экономит время и память путем утилизации старых объектов ViewHolder и передает их в onBindViewHolder(ViewHolder,Int).
////КАК Я ПОНИМАЮ: onCreateViewHolder создает новые ViewHolder в количестве которое уместится на экране. Далее работает. onBindViewHolder изменяя существующие ViewHolder новыми данными т.е. происходит переработка recycle элементов View
//        override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {//отвечает за заполнение данного холдера holder из данной позиции position.
//            val currency = currencies[position]
//            holder.setChecked(false)
//            holder.bind(currency)
//            if (currency.charCode == dataRepository.newValute?.charCode){
//                holder.setChecked(true)
//                selectedPosition = position
//            }
//            holder.itemView.setOnClickListener {
//                dataRepository.newValute = currency
//                notifyItemChanged(selectedPosition)// уведомляем ранее выбранный элемент (вызываем для него onBindViewHolder)
//                selectedPosition = holder.absoluteAdapterPosition// сохраняем текущий выбранный элемент
//                notifyItemChanged(selectedPosition)// обновляем текущий элемент
//            }
////            holder.apply {
////                //currencyName.text = currency.name//Мы рекомендуем поместить весь код, который будет выполнять привязку, внутрь CurrencyHolder (для этого все свойства в CurrencyHolder делаем приватными)
////            }
//        }
//
//        override fun getItemCount() = currencies.count()
//    }
//
//}