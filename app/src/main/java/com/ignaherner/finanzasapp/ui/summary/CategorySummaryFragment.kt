package com.ignaherner.finanzasapp.ui.summary

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.ignaherner.finanzasapp.data.TransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.databinding.FragmentCategorySummaryBinding
import com.ignaherner.finanzasapp.model.TransactionType
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModelFactory
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class CategorySummaryFragment : Fragment() {
    private var _binding: FragmentCategorySummaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository(
                AppDatabase.getDatabase(requireContext()).transactionDao()
            )
        )
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategorySummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.transactions.asLiveData().observe(viewLifecycleOwner) { allTransactions ->

            val meses = allTransactions.map { YearMonth.from(it.date) }.distinct().sorted()
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es"))

            val labels = meses.map { it.format(formatter).replaceFirstChar { c -> c.uppercaseChar() } }

            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, labels)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spMonth.adapter = adapter

            fun actualizarGrafico(mes: YearMonth) {
                val gastosDelMes = allTransactions.filter {
                    it.type == TransactionType.EXPENSE && YearMonth.from(it.date) == mes
                }

                val agrupados = gastosDelMes.groupBy { it.category.displayName }
                    .mapValues { it.value.sumOf { t -> t.amount } }

                val entries = agrupados.map { (category, total) ->
                    PieEntry(total.toFloat(), category)
                }

                val dataSet = PieDataSet(entries, "Gastos por categoría")
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
                dataSet.sliceSpace = 3f
                dataSet.valueTextSize = 14f
                dataSet.valueTextColor = Color.WHITE

                val pieData = PieData(dataSet)

                binding.pieChart.apply {
                    data = pieData
                    description.isEnabled = false
                    centerText = "Gastos: ${mes.format(formatter)}"
                    setEntryLabelColor(Color.BLACK)
                    animateY(1000)
                    invalidate()
                }
            }

            // Cambiar gráfico al seleccionar un mes
            binding.spMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                    actualizarGrafico(meses[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            // Mostrar último mes por defecto
            if (meses.isNotEmpty()) {
                binding.spMonth.setSelection(meses.lastIndex)
                actualizarGrafico(meses.last())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
