package com.ignaherner.finanzasapp.ui.credit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.finanzasapp.databinding.ItemCreditCardBinding
import com.ignaherner.finanzasapp.model.CreditCardWithUsage

class CreditAdapter(
    private var cards: List<CreditCardWithUsage>
) : RecyclerView.Adapter<CreditAdapter.CreditViewHolder>() {

    inner class CreditViewHolder(private val binding: ItemCreditCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cardUsage: CreditCardWithUsage) {
            val card = cardUsage.card
            val used = cardUsage.used
            val available = card.limit - used
            val percentUsed = if (card.limit > 0) ((used / card.limit) * 100).toInt() else 0

            binding.tvCardName.text = card.name
            binding.tvLimit.text = "Límite: $${"%.2f".format(card.limit)}"
            binding.tvAvailable.text = "Disponible: $${"%.2f".format(available)}"
            binding.tvDueDate.text = "Vencimiento: ${card.dueDate}/mes"

            binding.progressUsage.apply {
                max = 100
                progress = percentUsed.coerceIn(0, 100)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        val binding = ItemCreditCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount(): Int = cards.size

    fun updateData(newCards: List<CreditCardWithUsage>) {
        cards = newCards
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): CreditCardWithUsage = cards[position]
}
