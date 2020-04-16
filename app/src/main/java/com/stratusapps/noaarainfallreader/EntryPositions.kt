package com.stratusapps.noaarainfallreader

enum class EntryPositions(val begin: Int, val end: Int) {
    STATE_CODE(3, 5),
    YEAR(17, 21),
}