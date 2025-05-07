package com.ignaherner.finanzasapp.ui.transactions

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.finanzasapp.R
import com.ignaherner.finanzasapp.databinding.ItemTransactionHistoryBinding
import com.ignaherner.finanzasapp.model.Category
import com.ignaherner.finanzasapp.model.Transaction
import com.ignaherner.finanzasapp.model.TransactionType
import java.time.format.DateTimeFormatter

class TransactionsAdapter(private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: ItemTransactionHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            binding.tvTitle.text = transaction.title
            binding.tvCategory.text = transaction.category.displayName
            binding.tvAccount.text = transaction.account.displayName
            binding.tvDate.text = transaction.date.format(formatter)

            val amountText = when (transaction.type) {
                TransactionType.INCOME -> "+$${transaction.amount}"
                TransactionType.EXPENSE -> "-$${transaction.amount}"
            }
            val amountColor = when (transaction.type) {
                TransactionType.INCOME -> Color.parseColor("#2E7D32") // verde
                TransactionType.EXPENSE -> Color.parseColor("#C62828") // rojo
            }

            binding.tvAmount.text = amountText
            binding.tvAmount.setTextColor(amountColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    fun updateData(newList: List<Transaction>) {
        transactions = newList
        notifyDataSetChanged()
    }
}
