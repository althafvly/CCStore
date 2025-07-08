package com.sorrybro.ccstore.data

enum class BankName(val displayName: String) {
    None(""),
    AIRTEL("Airtel Payments Bank"),
    AU("AU Small Finance Bank"),
    AXIS("Axis Bank"),
    BANDHAN("Bandhan Bank"),
    BOB("Bank of Baroda"),
    CANARA("Canara Bank"),
    CITY_UNION("City Union Bank"),
    DCB("DCB Bank"),
    EQUITAS("Equitas Small Finance Bank"),
    ESAF("ESAF Small Finance Bank"),
    FEDERAL("Federal Bank"),
    FINCARE("Fincare Small Finance Bank"),
    HDFC("HDFC Bank"),
    ICICI("ICICI Bank"),
    IDBI("IDBI Bank"),
    IDFC("IDFC FIRST Bank"),
    INDUSIND("IndusInd Bank"),
    JANA("Jana Small Finance Bank"),
    JIO("Jio Payments Bank"),
    KARUR_VYSYA("Karur Vysya Bank"),
    KOTAK("Kotak Mahindra Bank"),
    NORTH_EAST("North East Small Finance Bank"),
    PAYTM("Paytm Payments Bank"),
    PNB("Punjab National Bank"),
    RBL("RBL Bank"),
    SBM("SBM Bank India"),
    SBI("State Bank of India"),
    SHIVALIK("Shivalik Small Finance Bank"),
    SOUTH_INDIAN("South Indian Bank"),
    SURYODAY("Suryoday Small Finance Bank"),
    UJJIVAN("Ujjivan Small Finance Bank"),
    UNION("Union Bank of India"),
    UNITY("Unity Small Finance Bank"),
    UTKARSH("Utkarsh Small Finance Bank"),
    YES("Yes Bank");

    companion object {
        fun displayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}
