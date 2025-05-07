package com.ignaherner.finanzasapp.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.ignaherner.finanzasapp.data.TransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.databinding.FragmentMonthlySummaryBinding
import com.ignaherner.finanzasapp.model.TransactionType
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModelFactory
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class MonthlySummaryFragment : Fragment() {
    private var _binding: FragmentMonthlySummaryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(
            TransactionRepository(
                AppDatabase.getDatabase(requireContext()).transactionDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlySummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.transactions.asLiveData().observe(viewLifecycleOwner) { transactions ->

            val grouped = transactions.groupBy { YearMonth.from(it.date) }

            val barEntries = mutableListOf<BarEntry>()
            val labels = mutableListOf<String>()
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es"))

            var index = 0f
            grouped.toSortedMap().forEach { (month, list) ->
                val income = list.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
                val expense = list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }

                barEntries.add(BarEntry(index, floatArrayOf(income.toFloat(), expense.toFloat())))
                labels.add(month.format(formatter).replaceFirstChar { it.uppercaseChar() })
                index++
            }

            val dataSet = BarDataSet(barEntries, "Ingresos vs Gastos")
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
            dataSet.stackLabels = arrayOf("Ingresos", "Gastos")

            val barData = BarData(dataSet)
            barData.barWidth = 0.4f

            with(binding.barChart) {
                data = barData
                description.isEnabled = false
                setFitBars(true)
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                axisRight.isEnabled = false
                animateY(1000)
                invalidate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
