package com.ignaherner.finanzasapp.model

import com.ignaherner.finanzasapp.data.local.CreditCardEntity

data class CreditCardWithUsage(
    val card: CreditCardEntity,
    val used: Double

)
