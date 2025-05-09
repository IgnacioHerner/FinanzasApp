package com.ignaherner.finanzasapp.ui.credit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.finanzasapp.databinding.ItemInstallmentGroupBinding
import com.ignaherner.finanzasapp.model.InstallmentGroup

class InstallmentGroupAdapter : RecyclerView.Adapter<InstallmentGroupAdapter.GroupViewHolder>() {

    private var groups: List<InstallmentGroup> = emptyList()

    inner class GroupViewHolder(private val binding: ItemInstallmentGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(group: InstallmentGroup) {
            binding.tvTitle.text = group.title
            binding.tvDetail.text = "${group.paidInstallments}/${group.totalInstallments} cuotas pagadas"
            binding.tvAmount.text = "$${group.totalAmount}"
            binding.tvNextDue.text = "Próxima: ${group.nextDueDate}"

            binding.progressBar.apply {
                max = group.totalInstallments
                progress = group.paidInstallments
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemInstallmentGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    override fun getItemCount(): Int = groups.size

    fun submitList(newList: List<InstallmentGroup>) {
        groups = newList
        notifyDataSetChanged()
    }
}
