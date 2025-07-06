package com.sorrybro.ccstore.data

enum class CardNetwork(val displayName: String) {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    AMERICAN_EXPRESS("American Express"),
    RUPAY("RuPay"),
    DINERS_CLUB("Diners Club"),
    DISCOVER("Discover"),
    JCB("JCB");

    companion object {
        fun displayNames(): List<String> {
            return entries.map { it.displayName }
        }
    }
}
