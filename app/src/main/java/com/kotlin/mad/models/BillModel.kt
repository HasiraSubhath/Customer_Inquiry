package com.kotlin.mad.models

data class BillModel(
    var billId: String? = null,
    var billType: String? = null,
    var billAmount: String? = null,
    var billNotes: String? = null,
    var billDate: String? = null

)