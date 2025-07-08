package com.sorrybro.ccstore.data

enum class CardNetwork(val displayName: String) {
    AMERICAN_EXPRESS("American Express"),
    DINERS_CLUB("Diners Club"),
    DISCOVER("Discover"),
    JCB("JCB"),
    MASTERCARD("Mastercard"),
    RUPAY("RuPay"),
    VISA("Visa");

    companion object {
        fun displayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}
