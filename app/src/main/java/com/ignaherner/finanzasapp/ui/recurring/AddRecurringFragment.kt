package com.ignaherner.finanzasapp.ui.recurring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ignaherner.finanzasapp.data.RecurringTransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.data.local.RecurringTransactionEntity
import com.ignaherner.finanzasapp.databinding.FragmentAddRecurringBinding
import com.ignaherner.finanzasapp.model.Account
import com.ignaherner.finanzasapp.model.Category
import com.ignaherner.finanzasapp.model.RecurrenceType
import com.ignaherner.finanzasapp.model.TransactionType
import com.ignaherner.finanzasapp.viewmodel.RecurringTransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.RecurringTransactionViewModelFactory
import java.time.LocalDate

class AddRecurringFragment : Fragment() {

    private var _binding: FragmentAddRecurringBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecurringTransactionViewModel by viewModels {
        RecurringTransactionViewModelFactory(
            RecurringTransactionRepository(
                AppDatabase.getDatabase(requireContext()).recurringTransactionDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecurringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSpinners()

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val amount = binding.etAmount.text.toString().toDoubleOrNull()

            if (title.isBlank() || amount == null) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val account = Account.entries[binding.spAccount.selectedItemPosition]
            val category = Category.entries[binding.spCategory.selectedItemPosition]
            val recurrence = RecurrenceType.entries[binding.spRecurrence.selectedItemPosition]
            val type = if (binding.rbIncome.isChecked) TransactionType.INCOME else TransactionType.EXPENSE

            val newRecurring = RecurringTransactionEntity(
                title = title,
                amount = amount,
                account = account,
                category = category,
                recurrence = recurrence,
                type = type,
                startDate = LocalDate.now(),
            )

            viewModel.insert(newRecurring)
            Toast.makeText(requireContext(), "Transacción recurrente guardada", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun setupSpinners() {
        binding.spAccount.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            Account.values().map { it.displayName })

        binding.spCategory.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            Category.values().map { it.displayName })

        binding.spRecurrence.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            RecurrenceType.values().map { it.label })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}