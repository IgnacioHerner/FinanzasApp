package com.ignaherner.finanzasapp.ui.recurring

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.finanzasapp.data.local.RecurringTransactionEntity
import com.ignaherner.finanzasapp.databinding.ItemRecurringBinding
import com.ignaherner.finanzasapp.model.RecurrenceType
import com.ignaherner.finanzasapp.model.TransactionType

class RecurringAdapter : RecyclerView.Adapter<RecurringAdapter.RecurringViewHolder>() {

    private var recurringList = listOf<RecurringTransactionEntity>()

    inner class RecurringViewHolder(private val binding: ItemRecurringBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecurringTransactionEntity) {
            binding.tvTitle.text = item.title
            binding.tvAmount.text = formatAmount(item.amount, item.type)

            val recurrence = when (item.recurrence) {
                RecurrenceType.DAILY -> "Diario"
                RecurrenceType.WEEKLY -> "Semanal"
                RecurrenceType.MONTHLY -> "Mensual"
                RecurrenceType.YEARLY -> "Anual"
            }

            binding.tvMeta.text = "$recurrence - ${item.category.displayName}"
        }

        private fun formatAmount(amount: Double, type: TransactionType): String {
            val prefix = if (type == TransactionType.INCOME) "+" else "-"
            return "$prefix$${"%,.2f".format(amount)}"
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecurringViewHolder {
        val binding = ItemRecurringBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecurringViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecurringViewHolder,
        position: Int
    ) {
        holder.bind(recurringList[position])
    }

    override fun getItemCount() = recurringList.size

    fun submitList(list: List<RecurringTransactionEntity> ) {
        recurringList = list
        notifyDataSetChanged()
    }

}