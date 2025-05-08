package com.ignaherner.finanzasapp.ui.credit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ignaherner.finanzasapp.data.CreditCardRepository
import com.ignaherner.finanzasapp.data.CreditTransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.data.local.CreditCardEntity
import com.ignaherner.finanzasapp.databinding.FragmentAddEditCreditBinding
import com.ignaherner.finanzasapp.viewmodel.CreditCardViewModel
import com.ignaherner.finanzasapp.viewmodel.CreditCardViewModelFactory
import kotlinx.coroutines.launch

class AddEditCreditFragment : Fragment() {

    private var _binding: FragmentAddEditCreditBinding? = null
    private val binding get() = _binding!!

    private val args: AddEditCreditFragmentArgs by navArgs()

    private val viewModel: CreditCardViewModel by viewModels {
        CreditCardViewModelFactory(
            CreditCardRepository(AppDatabase.getDatabase(requireContext()).creditCardDao()),
            CreditTransactionRepository(AppDatabase.getDatabase(requireContext()).creditTransactionDao())
        )
    }

    private var isEditing = false
    private var editingCardId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditCreditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editingCardId = args.cardId
        isEditing = editingCardId != 0

        if (isEditing) {
            lifecycleScope.launch {
                val card = viewModel.getById(editingCardId).await()
                card?.let {
                    binding.etCardName.setText(it.name)
                    binding.etLimit.setText(it.limit.toString())
                    binding.etDueDate.setText(it.dueDate.toString())
                    binding.etCutDate.setText(it.cutDate.toString())
                }
            }
        }


        binding.btnSaveCard.setOnClickListener {
            val name = binding.etCardName.text.toString()
            val limit = binding.etLimit.text.toString().toDoubleOrNull()
            val dueDate = binding.etDueDate.text.toString().toIntOrNull()
            val cutDate = binding.etCutDate.text.toString().toIntOrNull()

            if (name.isBlank() || limit == null || dueDate == null || cutDate == null) {
                Toast.makeText(requireContext(), "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val card = CreditCardEntity(
                id = if (isEditing) editingCardId else 0,
                name = name,
                limit = limit,
                dueDate = dueDate,
                cutDate = cutDate
            )

            if (isEditing) {
                viewModel.update(card)
            } else {
                viewModel.add(card)
            }

            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}