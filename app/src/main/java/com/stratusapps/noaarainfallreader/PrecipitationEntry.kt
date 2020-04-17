package com.stratusapps.noaarainfallreader

data class PrecipitationEntry(
    val year: String,
    val month: String,
    val day: String,
    val amount: Double,
    val stateCode: String,
    val stationId: String
) {
    override fun toString(): String {
        return "$year: ${String.format("%.2f", amount)} inches"
    }
}

data class YearPrecipitationEntry(val year: String, val amount: Double) {
    override fun toString(): String {
        return "$year: ${String.format("%.2f", amount)} inches"
    }
}


