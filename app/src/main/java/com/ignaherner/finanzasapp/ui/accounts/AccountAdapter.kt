package com.ignaherner.finanzasapp.ui.accounts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ignaherner.finanzasapp.databinding.ItemAccountBinding
import com.ignaherner.finanzasapp.ui.accounts.AccountAdapter.*

class AccountAdapter : RecyclerView.Adapter<AccountViewHolder>() {

    private var accounts = listOf<AccountItem>()

    inner class AccountViewHolder(private val binding: ItemAccountBinding) :
    RecyclerView.ViewHolder(binding.root) {
        fun bind(account: AccountItem) {
            binding.tvAccountName.text = account.name
            binding.tvAccountBalance.text = "$${account.balance}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AccountViewHolder,
        position: Int
    ) {
        holder.bind(accounts[position])
    }

    override fun getItemCount() = accounts.size

    fun submitList(list: List<AccountItem>) {
        accounts = list
        notifyDataSetChanged()
    }

}