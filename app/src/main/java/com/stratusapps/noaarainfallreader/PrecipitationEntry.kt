package com.stratusapps.noaarainfallreader

data class PrecipitationEntry(val year: String, val amount: Double, val stateCode: String) {
    override fun toString(): String {
        return "$year: ${String.format("%.2f", amount)} inches"
    }
}


