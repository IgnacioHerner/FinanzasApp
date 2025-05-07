package com.ignaherner.finanzasapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.finanzasapp.data.local.TransactionEntity
import com.ignaherner.finanzasapp.databinding.ItemTransactionBinding
import com.ignaherner.finanzasapp.model.Transaction
import com.ignaherner.finanzasapp.model.TransactionType
import java.time.format.DateTimeFormatter

class TransactionAdapter(private var transactions: List<TransactionEntity>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionEntity) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            binding.tvDate.text = transaction.date.format(formatter)
            binding.tvTitle.text = transaction.title
            binding.tvAmount.text = formatAmount(transaction.amount, transaction.type)
            binding.tvCategory.text = transaction.category.displayName
            binding.tvAccount.text = transaction.account.displayName

            val color = when (transaction.type) {
                TransactionType.INCOME -> "#2E7D32".toColorInt()
                TransactionType.EXPENSE -> "#C62828".toColorInt()
            }
            binding.tvAmount.setTextColor(color)
        }

        private fun formatAmount(amount: Double, type: TransactionType): String {
            return when (type) {
                TransactionType.INCOME -> "+$${amount}"
                TransactionType.EXPENSE -> "-$${amount}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(
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

    fun updateData(newList: List<TransactionEntity>) {
        transactions = newList
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): TransactionEntity {
        return transactions[position]
    }
}
