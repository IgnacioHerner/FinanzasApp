package com.ignaherner.finanzasapp.ui.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ignaherner.finanzasapp.data.CreditCardRepository
import com.ignaherner.finanzasapp.data.CreditTransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.databinding.FragmentCreditDetailBinding
import com.ignaherner.finanzasapp.viewmodel.CreditCardViewModel
import com.ignaherner.finanzasapp.viewmodel.CreditCardViewModelFactory
import com.ignaherner.finanzasapp.viewmodel.CreditTransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.CreditTransactionViewModelFactory
import kotlinx.coroutines.launch

class CreditDetailFragment : Fragment() {

    private var _binding: FragmentCreditDetailBinding? = null
    private val binding get() = _binding!!

    private val args: CreditDetailFragmentArgs by navArgs()
    private val cardId by lazy { args.cardId }

    private val txViewModel: CreditTransactionViewModel by viewModels {
        CreditTransactionViewModelFactory(
            CreditTransactionRepository(
                AppDatabase.getDatabase(requireContext()).creditTransactionDao()
            )
        )
    }

    private val cardViewModel: CreditCardViewModel by viewModels {
        CreditCardViewModelFactory(
            CreditCardRepository(AppDatabase.getDatabase(requireContext()).creditCardDao()),
            CreditTransactionRepository(AppDatabase.getDatabase(requireContext()).creditTransactionDao())
        )
    }

    private lateinit var adapter: InstallmentGroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreditDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up grouped adapter
        adapter = InstallmentGroupAdapter()
        binding.rvInstallmentGroups.layoutManager = LinearLayoutManager(requireContext())
        binding.rvInstallmentGroups.adapter = adapter


        // Observar grupos por parentId
        txViewModel.getGroupedByParent(cardId).observe(viewLifecycleOwner) { groups ->
            adapter.submitList(groups)
        }

        // Mostrar info general de la tarjeta
        lifecycleScope.launch {
            val card = cardViewModel.getById(cardId)
            val used = txViewModel.getUsedAmount(cardId)

            binding.tvCardName.text = card?.name ?: "Tarjeta"
            val disponible = (card?.limit ?: 0.0) - used
            binding.tvCardBalance.text = "Usado: $${used} / Disponible: $${disponible}"
        }

        // Agregar nuevo consumo
        binding.fabAddTx.setOnClickListener {
            val action = CreditDetailFragmentDirections
                .actionCreditDetailFragmentToAddCreditTransactionFragment(cardId)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
