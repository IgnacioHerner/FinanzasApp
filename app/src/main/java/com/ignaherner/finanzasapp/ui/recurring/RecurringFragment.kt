package com.ignaherner.finanzasapp.ui.recurring

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.ignaherner.finanzasapp.R
import com.ignaherner.finanzasapp.data.RecurringTransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.databinding.FragmentRecurringBinding
import com.ignaherner.finanzasapp.viewmodel.RecurringTransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.RecurringTransactionViewModelFactory


class RecurringFragment : Fragment() {

    private var _binding: FragmentRecurringBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RecurringAdapter

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
        _binding = FragmentRecurringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = RecurringAdapter()
        binding.rvRecurring.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecurring.adapter = adapter

        viewModel.recurringTransaction.asLiveData().observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}