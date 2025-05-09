package com.ignaherner.finanzasapp.ui.credit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.finanzasapp.data.local.CreditTransactionEntity
import com.ignaherner.finanzasapp.databinding.ItemCreditTransactionBinding

class CreditTransactionAdapter :
    ListAdapter<CreditTransactionEntity, CreditTransactionAdapter.TxViewHolder>(DiffCallback()) {

    inner class TxViewHolder(private val binding: ItemCreditTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tx: CreditTransactionEntity) {
            binding.tvTitle.text = tx.title
            binding.tvAmount.text = "$${tx.amount}"
            binding.tvDate.text = tx.date.toString()
            binding.tvCategory.text = tx.category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TxViewHolder {
        val binding = ItemCreditTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TxViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TxViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<CreditTransactionEntity>() {
        override fun areItemsTheSame(oldItem: CreditTransactionEntity, newItem: CreditTransactionEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CreditTransactionEntity, newItem: CreditTransactionEntity) =
            oldItem == newItem
    }
}