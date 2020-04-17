package com.stratusapps.noaarainfallreader

enum class EntryPositions(val begin: Int, val end: Int) {
    STATE_CODE(3, 5),
    YEAR(17, 21),
    MONTH(21, 23),
    DAY(23, 27),
    STATION_ID(3, 11)
}