package com.ignaherner.finanzasapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ignaherner.finanzasapp.data.CreditTransactionRepository
import com.ignaherner.finanzasapp.data.local.CreditTransactionEntity
import com.ignaherner.finanzasapp.model.InstallmentGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CreditTransactionViewModel(
    private val repository: CreditTransactionRepository
) : ViewModel() {

    fun getGroupedByParent(cardId: Int): LiveData<List<InstallmentGroup>> {
        return repository.getByCard(cardId).map { list ->
            list.groupBy { it.parentId }.mapNotNull { (parentId, txList) ->
                val first = txList.firstOrNull() ?: return@mapNotNull null
                val totalInstallments = first.totalInstallments
                val title = first.title.substringBefore(" (")
                val paid = txList.count { it.isPaid }
                val remaining = totalInstallments - paid
                val totalAmount = txList.sumOf { it.amount }
                val nextDue = txList.firstOrNull { !it.isPaid }?.date ?: first.date

                InstallmentGroup(
                    parentId = parentId,
                    title = title,
                    totalAmount = totalAmount,
                    totalInstallments = totalInstallments,
                    paidInstallments = paid,
                    remainingInstallments = remaining,
                    nextDueDate = nextDue
                )
            }
        }.asLiveData()
    }



    fun getByCard(cardId: Int): LiveData<List<CreditTransactionEntity>> {
        return repository.getByCard(cardId).asLiveData()
    }

    suspend fun getUsedAmount(cardId: Int): Double {
        return repository.getByCard(cardId).first().sumOf { it.amount }
    }

    fun add(tx: CreditTransactionEntity) {
        viewModelScope.launch {
            repository.insert(tx)
        }
    }

    fun update(tx: CreditTransactionEntity) {
        viewModelScope.launch {
            repository.update(tx)
        }
    }

    fun delete(tx: CreditTransactionEntity) {
        viewModelScope.launch {
            repository.delete(tx)
        }
    }
}