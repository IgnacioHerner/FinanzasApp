package com.ignaherner.finanzasapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CurrencyViewModel : ViewModel(){
    //Estado de la moneda seleccionada (inicial :ARS)
    private val _selectedCurrency = MutableStateFlow("ARS")
    val selectedCurrency: StateFlow<String> = _selectedCurrency

    // Funcion para cambiar la moneda
    fun setCurrency(currency: String) {
        _selectedCurrency.value = currency
    }
}