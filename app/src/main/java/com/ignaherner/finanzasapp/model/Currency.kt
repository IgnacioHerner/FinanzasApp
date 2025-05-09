package com.ignaherner.finanzasapp.model

enum class Currency(val symbol: String) {
    ARS("$"),
    USD("US$");

    override fun toString(): String = symbol
}