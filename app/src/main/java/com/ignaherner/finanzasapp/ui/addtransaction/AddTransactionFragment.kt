package com.ignaherner.finanzasapp.ui.addtransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ignaherner.finanzasapp.R
import com.ignaherner.finanzasapp.data.TransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.data.local.TransactionEntity
import com.ignaherner.finanzasapp.databinding.FragmentAddTransactionBinding
import com.ignaherner.finanzasapp.model.Account
import com.ignaherner.finanzasapp.model.Category
import com.ignaherner.finanzasapp.model.Transaction
import com.ignaherner.finanzasapp.model.TransactionType
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModelFactory
import java.time.LocalDate

class AddTransactionFragment : Fragment() {
    private var _binding: FragmentAddTransactionBinding? = null
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
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val categories = Category.entries.map { it.displayName }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCategory.adapter = adapter

        val accountOptions = Account.entries.map { it.displayName }
        val accountAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, accountOptions)

        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spAccount.adapter = accountAdapter


        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val amount = binding.etAmount.text.toString().toDoubleOrNull()
            val type =
                if (binding.rbIncome.isChecked) TransactionType.INCOME else TransactionType.EXPENSE

            val isPaid = binding.cbIsPaid.isChecked

            val selectedCategoryIndex = binding.spCategory.selectedItemPosition
            val selectedCategory = Category.entries[selectedCategoryIndex]

            val selectedAcountIndex = binding.spAccount.selectedItemPosition
            val selectedAccount = Account.entries[selectedAcountIndex]

            if (title.isNotBlank() && amount != null) {
                val newTransaction = TransactionEntity(
                    title = title,
                    amount = amount,
                    type = type,
                    account = selectedAccount,
                    category = selectedCategory,
                    date = LocalDate.now(),
                    isPaid = isPaid
                )

                viewModel.addTransaction(newTransaction)
                findNavController().navigate(R.id.action_addTransactionFragment_to_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}