package com.ignaherner.finanzasapp.ui.credit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ignaherner.finanzasapp.data.CreditTransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.data.local.CreditTransactionEntity
import com.ignaherner.finanzasapp.databinding.FragmentAddCreditTransactionBinding
import com.ignaherner.finanzasapp.model.Category
import com.ignaherner.finanzasapp.viewmodel.CreditTransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.CreditTransactionViewModelFactory
import java.time.LocalDate
import java.util.UUID

class AddCreditTransactionFragment : Fragment() {
    private var _binding: FragmentAddCreditTransactionBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<AddCreditTransactionFragmentArgs>()

    private val viewModel: CreditTransactionViewModel by viewModels {
        CreditTransactionViewModelFactory(
            CreditTransactionRepository(
                AppDatabase.getDatabase(requireContext()).creditTransactionDao()
            )
        )
    }

    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCreditTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Spinner de cuotas (1 a 24)
        val installments = (1..24).map { "$it cuotas" }
        binding.spInstallments.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            installments
        )

        //Spinner categorias
        val categoryList = Category.entries.map { it.name }
        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            categoryList
        )

        // Fecha
        binding.etDate.setText(selectedDate.toString())
        binding.etDate.setOnClickListener {
            val today = LocalDate.now()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                selectedDate = LocalDate.of(year, month + 1, day)
                binding.etDate.setText(selectedDate.toString())
            }, today.year, today.monthValue - 1, today.dayOfMonth).show()
        }

        //Guardar
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val amount = binding.etAmount.text.toString().toDoubleOrNull()
            val category = Category.valueOf(binding.spCategory.selectedItem.toString())
            val totalInstallments = binding.spInstallments.selectedItemPosition + 1

            if (title.isNotBlank() && amount != null) {
                val installmentAmount = amount / totalInstallments
                val commonId = UUID.randomUUID().toString()
                val txList = mutableListOf<CreditTransactionEntity>()

                for (i in 1..totalInstallments) {
                    val installmentDate = selectedDate.plusMonths((i - 1).toLong())
                    txList.add(
                        CreditTransactionEntity(
                            cardId = args.cardId,
                            title = "$title ($i/$totalInstallments)",
                            amount = installmentAmount,
                            category = category,
                            date = installmentDate,
                            installment = i,
                            totalInstallments = totalInstallments,
                            parentId = commonId
                        )
                    )
                }

                txList.forEach { viewModel.add(it) }

                Toast.makeText(
                    requireContext(),
                    "Consumo registrado en $totalInstallments cuotas",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Completa todos los campos correctamente",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}