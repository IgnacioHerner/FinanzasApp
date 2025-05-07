package com.ignaherner.finanzasapp.ui.home

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.finanzasapp.data.RecurringTransactionProcessor
import com.ignaherner.finanzasapp.R
import com.ignaherner.finanzasapp.data.TransactionRepository
import com.ignaherner.finanzasapp.data.local.AppDatabase
import com.ignaherner.finanzasapp.data.local.TransactionEntity
import com.ignaherner.finanzasapp.databinding.DialogEditTransactionBinding
import com.ignaherner.finanzasapp.databinding.FragmentHomeBinding
import com.ignaherner.finanzasapp.model.TransactionType
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModel
import com.ignaherner.finanzasapp.viewmodel.TransactionViewModelFactory
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Procesar gastos recurrentes al iniciar
        val db = AppDatabase.getDatabase(requireContext())
        val processor = RecurringTransactionProcessor(
            recurringDao = db.recurringTransactionDao(),
            transactionDao = db.transactionDao()
        )
        lifecycleScope.launch {
            processor.processRecurringTransactions()
        }

        adapter = TransactionAdapter(emptyList())
        binding.rvLastTransactions.adapter = adapter
        binding.rvLastTransactions.layoutManager = LinearLayoutManager(requireContext())

        viewModel.transactions.asLiveData().observe(viewLifecycleOwner) { transactions ->

            val income =
                transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val expenses =
                transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val balance = income - expenses

            binding.tvIncomeValue.text = "$${viewModel.getIncome()}"
            binding.tvExpenseValue.text = "$${viewModel.getExpenses()}"
            binding.tvBalanceValue.text = "$${viewModel.getBalance()}"

            adapter.updateData(transactions)
        }

        binding.fabAddTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addTransactionFragment)
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val transaction = adapter.getItemAt(position)

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.deleteTransaction(transaction)
                        Toast.makeText(
                            requireContext(),
                            "Transacción eliminada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    ItemTouchHelper.RIGHT -> {
                        showEditDialog(transaction)
                        adapter.notifyItemChanged(position)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val cornerRadius = 32f
                val iconSize = 48
                val iconMargin = 48

                val editIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_edit)
                val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)

                val backgroundPaint = Paint().apply {
                    isAntiAlias = true
                }

                if (dX > 0) {
                    // 👉 Swipe derecha: EDITAR
                    backgroundPaint.color = Color.parseColor("#E3F2FD") // Azul claro

                    val rectF = RectF(
                        itemView.left.toFloat(), itemView.top.toFloat(),
                        itemView.left + dX, itemView.bottom.toFloat()
                    )
                    c.drawRoundRect(rectF, cornerRadius, cornerRadius, backgroundPaint)

                    editIcon?.setBounds(
                        itemView.left + iconMargin,
                        itemView.top + (itemView.height - iconSize) / 2,
                        itemView.left + iconMargin + iconSize,
                        itemView.top + (itemView.height + iconSize) / 2
                    )
                    editIcon?.draw(c)

                } else if (dX < 0) {
                    // 👈 Swipe izquierda: ELIMINAR
                    backgroundPaint.color = Color.parseColor("#FFEBEE") // Rojo claro

                    val rectF = RectF(
                        itemView.right + dX, itemView.top.toFloat(),
                        itemView.right.toFloat(), itemView.bottom.toFloat()
                    )
                    c.drawRoundRect(rectF, cornerRadius, cornerRadius, backgroundPaint)

                    deleteIcon?.setBounds(
                        itemView.right - iconMargin - iconSize,
                        itemView.top + (itemView.height - iconSize) / 2,
                        itemView.right - iconMargin,
                        itemView.top + (itemView.height + iconSize) / 2
                    )
                    deleteIcon?.draw(c)
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }



            private fun showEditDialog(transaction: TransactionEntity) {
                val dialogBinding = DialogEditTransactionBinding.inflate(layoutInflater)

                // Prellenar campos
                dialogBinding.etTitleEdit.setText(transaction.title)
                dialogBinding.etAmountEdit.setText(transaction.amount.toString())
                if (transaction.type == TransactionType.INCOME) {
                    dialogBinding.rbIncomeEdit.isChecked = true
                } else {
                    dialogBinding.rbExpenseEdit.isChecked = true
                }

                AlertDialog.Builder(requireContext())
                    .setTitle("Editar transacción")
                    .setView(dialogBinding.root)
                    .setPositiveButton("Guardar") { _, _ ->
                        val newTitle = dialogBinding.etTitleEdit.text.toString()
                        val newAmount = dialogBinding.etAmountEdit.text.toString().toDoubleOrNull()
                        val newType = if (dialogBinding.rbIncomeEdit.isChecked) {
                            TransactionType.INCOME
                        } else {
                            TransactionType.EXPENSE
                        }

                        if (newTitle.isNotBlank() && newAmount != null) {
                            val update = transaction.copy(
                                title = newTitle,
                                amount = newAmount,
                                type = newType
                            )
                            viewModel.addTransaction(update) // Insertaa (Room lo reemplaza si ID coincide)
                            Toast.makeText(
                                requireContext(),
                                "Transacción actualzada",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Completa todos los campos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvLastTransactions)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}