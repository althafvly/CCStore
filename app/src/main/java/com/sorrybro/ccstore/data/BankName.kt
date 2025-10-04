package com.sorrybro.ccstore.data

import androidx.compose.ui.graphics.Color

enum class BankName(val displayName: String, val gradient: List<Color> = listOf(Color(0xFF374ABE), Color(0xFF64B6FF))) {
    None("", listOf(Color(0xFF374ABE), Color(0xFF64B6FF))),

    AIRTEL("Airtel Payments Bank", listOf(Color(0xFFED1C24), Color(0xFFFF6F61))),
    AU("AU Small Finance Bank", listOf(Color(0xFF0052A5), Color(0xFF00AEEF))),
    AXIS("Axis Bank", listOf(Color(0xFFED1C24), Color(0xFFB41F1F))),
    BANDHAN("Bandhan Bank", listOf(Color(0xFFED1C24), Color(0xFFFF7F50))),
    BOB("Bank of Baroda", listOf(Color(0xFFFF6600), Color(0xFFFFCC00))),
    CANARA("Canara Bank", listOf(Color(0xFF1C75BC), Color(0xFF00AEEF))),
    CITY_UNION("City Union Bank", listOf(Color(0xFF0052A5), Color(0xFF00AEEF))),
    DCB("DCB Bank", listOf(Color(0xFFED1C24), Color(0xFFFF6F61))),
    EQUITAS("Equitas Small Finance Bank", listOf(Color(0xFFFF6600), Color(0xFFFFC107))),
    ESAF("ESAF Small Finance Bank", listOf(Color(0xFF00AEEF), Color(0xFF1C75BC))),
    FEDERAL("Federal Bank", listOf(Color(0xFF008000), Color(0xFF00FF00))),
    FINCARE("Fincare Small Finance Bank", listOf(Color(0xFFED1C24), Color(0xFFFF6F61))),
    HDFC("HDFC Bank", listOf(Color(0xFF0066B3), Color(0xFF00AEEF))),
    ICICI("ICICI Bank", listOf(Color(0xFFFF6600), Color(0xFFFFCC00))),
    IDBI("IDBI Bank", listOf(Color(0xFF008000), Color(0xFF00FF00))),
    IDFC("IDFC FIRST Bank", listOf(Color(0xFFED1C24), Color(0xFFFF6F61))),
    INDUSIND("IndusInd Bank", listOf(Color(0xFFFF6600), Color(0xFFFFCC00))),
    JANA("Jana Small Finance Bank", listOf(Color(0xFF0052A5), Color(0xFF00AEEF))),
    JIO("Jio Payments Bank", listOf(Color(0xFFFF0000), Color(0xFFFF6600))),
    KARUR_VYSYA("Karur Vysya Bank", listOf(Color(0xFF1C75BC), Color(0xFF00AEEF))),
    KOTAK("Kotak Mahindra Bank", listOf(Color(0xFFFF6600), Color(0xFFFFCC00))),
    NORTH_EAST("North East Small Finance Bank", listOf(Color(0xFF008000), Color(0xFF00FF00))),
    PAYTM("Paytm Payments Bank", listOf(Color(0xFF00AEEF), Color(0xFF1C75BC))),
    PNB("Punjab National Bank", listOf(Color(0xFFFFCC00), Color(0xFFFF6600))),
    RBL("RBL Bank", listOf(Color(0xFFED1C24), Color(0xFFFF6F61))),
    SBM("SBM Bank India", listOf(Color(0xFF0052A5), Color(0xFF00AEEF))),
    SBI("State Bank of India", listOf(Color(0xFF0033A0), Color(0xFF00AEEF))),
    SHIVALIK("Shivalik Small Finance Bank", listOf(Color(0xFF008000), Color(0xFF00FF00))),
    SOUTH_INDIAN("South Indian Bank", listOf(Color(0xFFED1C24), Color(0xFFFF6F61))),
    SURYODAY("Suryoday Small Finance Bank", listOf(Color(0xFFFF6600), Color(0xFFFFCC00))),
    UJJIVAN("Ujjivan Small Finance Bank", listOf(Color(0xFFED1C24), Color(0xFFFF6F61))),
    UNION("Union Bank of India", listOf(Color(0xFF0033A0), Color(0xFF00AEEF))),
    UNITY("Unity Small Finance Bank", listOf(Color(0xFF008000), Color(0xFF00FF00))),
    UTKARSH("Utkarsh Small Finance Bank", listOf(Color(0xFFFF6600), Color(0xFFFFCC00))),
    YES("Yes Bank", listOf(Color(0xFF0066B3), Color(0xFF00AEEF)));

    companion object {
        fun displayNames(): List<String> = entries.map { it.displayName }
    }
}
