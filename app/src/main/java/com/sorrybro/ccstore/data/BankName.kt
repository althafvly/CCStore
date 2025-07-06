package com.sorrybro.ccstore.data

enum class BankName(val displayName: String) {
    None(""),
    SBI("State Bank of India"),
    HDFC("HDFC Bank"),
    ICICI("ICICI Bank"),
    AXIS("Axis Bank"),
    KOTAK("Kotak Mahindra Bank"),
    PNB("Punjab National Bank"),
    BOB("Bank of Baroda"),
    CANARA("Canara Bank"),
    INDUSIND("IndusInd Bank"),
    YES("Yes Bank"),
    IDFC("IDFC FIRST Bank"),
    UNION("Union Bank of India"),
    FEDERAL("Federal Bank"),
    SOUTH_INDIAN("South Indian Bank"),
    IDBI("IDBI Bank"),
    RBL("RBL Bank"),
    BANDHAN("Bandhan Bank"),
    DCB("DCB Bank"),
    KARUR_VYSYA("Karur Vysya Bank"),
    CITY_UNION("City Union Bank"),
    UJJIVAN("Ujjivan Small Finance Bank"),
    ESAF("ESAF Small Finance Bank"),
    AU("AU Small Finance Bank"),
    PAYTM("Paytm Payments Bank"),
    AIRTEL("Airtel Payments Bank"),
    JIO("Jio Payments Bank");

    companion object {
        fun displayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}
