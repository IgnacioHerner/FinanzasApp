package com.ignaherner.finanzasapp.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ignaherner.finanzasapp.data.TransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.databinding.FragmentTransactionsBinding
import com.ignaherner.finanzasapp.model.Account
import com.ignaherner.finanzasapp.model.Category
import com.ignaherner.finanzasapp.ui.home.TransactionAdapter
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModelFactory

class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TransactionAdapter
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
    ): View? {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = TransactionAdapter(emptyList())
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTransactions.adapter = adapter

        // Setup spinners
        val accounts = listOf("Todas") + Account.entries.map { it.displayName }
        val categories = listOf("Todas") + Account.entries.map { it.displayName }

        binding.spFilterAccount.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            accounts
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        binding.spFilterCategory.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            categories
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        // Filtro centralizado
        fun filtrar(transactions: List<com.ignaherner.finanzasapp.data.local.TransactionEntity>): List<com.ignaherner.finanzasapp.data.local.TransactionEntity> {

            val accountIndex = binding.spFilterAccount.selectedItemPosition
            val categoryIndex = binding.spFilterCategory.selectedItemPosition

            return transactions.filter { transactions ->
                val matchAccount = accountIndex == 0 || transactions.account == Account.entries[accountIndex - 1]
                val matchCategory = categoryIndex == 0 || transactions.category == Category.entries[categoryIndex - 1]
                matchAccount && matchCategory
            }
        }
        // Observa transacciones reales
        viewModel.transactions.asLiveData().observe (viewLifecycleOwner){ allTransactions ->
            adapter.updateData(filtrar(allTransactions))
        }


        binding.spFilterAccount.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    adapter.updateData(filtrar(viewModel.transactions.value))

                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

        binding.spFilterCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    adapter.updateData(filtrar(viewModel.transactions.value))
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}