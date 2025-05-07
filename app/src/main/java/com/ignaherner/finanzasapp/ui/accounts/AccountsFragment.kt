package com.ignaherner.finanzasapp.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ignaherner.finanzasapp.data.TransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.databinding.FragmentAccountBinding
import com.ignaherner.finanzasapp.model.Account
import com.ignaherner.finanzasapp.model.TransactionType
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModelFactory

class AccountsFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AccountAdapter

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
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = AccountAdapter()
        binding.rvAccounts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAccounts.adapter = adapter

        viewModel.transactions.asLiveData().observe(viewLifecycleOwner) { transactions ->

            val accountsWithBalance = Account.values().map { account ->
                val accountTransactions = transactions.filter { it.account == account }

                val income = accountTransactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
                val expense = accountTransactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
                val balance = income - expense

                AccountItem(
                    name = account.displayName,
                    balance = balance
                )
            }

            adapter.submitList(accountsWithBalance)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}