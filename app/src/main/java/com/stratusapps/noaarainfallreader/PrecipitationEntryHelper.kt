package com.stratusapps.noaarainfallreader

import java.lang.IndexOutOfBoundsException

const val AZ_STATE_CODE = "02"
const val TOTAL_AMT_IDENTIFIER = "2500 "
const val ENTRIES_BEGIN_INDEX = 30
const val SUSPECT_TIMES_FLAG = "R"

object PrecipitationEntryHelper {
    fun createPrecipitationEntries(fileContents: List<String>): List<YearPrecipitationEntry> {
        return fileContents.fold(emptyList()) { sum: List<String>, content: String ->
            sum + content.split("\n") // breaks up each file by new lines and joins that list with the overall list
        }.filter {
            it != "" // Filters any empty lines
        }.map {
            createPrecipitationEntry(it) // Creates the precipitation entry from this row in the file
        }.filter {
            it.stateCode == AZ_STATE_CODE // Filters the list down to only Arizona entries
        }.groupBy {
            it.year // Groups the list of entries by year to allow for totaling by year
        }.map {
            // Totals the precipitation and creates an entry for that year with the overall total
            val total = it.value.totalPrecipitationByDay()
            YearPrecipitationEntry(it.key, total)
        }
    }

    private fun createPrecipitationEntry(textRecord: String): PrecipitationEntry {
        // Only creates an entry with the details we care about
        return PrecipitationEntry(
            year = extractEntrySection(textRecord, EntryPositions.YEAR),
            day = extractEntrySection(textRecord, EntryPositions.DAY),
            month = extractEntrySection(textRecord, EntryPositions.MONTH),
            amount = getFinalPrecipitationAmtForEntry(textRecord.substring(ENTRIES_BEGIN_INDEX)),
            stateCode = extractEntrySection(textRecord, EntryPositions.STATE_CODE),
            stationId = extractEntrySection(textRecord, EntryPositions.STATION_ID)
        )
    }

    // Extracts the specific section of the record that is required
    private fun extractEntrySection(textRecord: String, position: EntryPositions): String {
        return try {
            textRecord.substring(position.begin, position.end)
        } catch (ex: IndexOutOfBoundsException) {
            ""
        }
    }

    // Retrieves the final section of the record where the actual total for that day is saved
    private fun getFinalPrecipitationAmtForEntry(textRecord: String): Double {
        val index = textRecord.indexOf(TOTAL_AMT_IDENTIFIER)
        return try {
            // Parses the remaining part of the text record and removes the SUSPECT_TIMES_FLAG which just
            // indicates some of the times for entries on that the time of
            // day may in incorrect, but the total is still correct and should be used
            val precipitationTotalBlock = textRecord.substring(index + TOTAL_AMT_IDENTIFIER.length)
                .replace(SUSPECT_TIMES_FLAG, "").trim()
            precipitationTotalBlock.toDouble() / 100.0
        } catch (ex: Exception) {
            0.0
        }
    }
}

// Helpful extension method to get the total precipitation for a list of PrecipitationEntry
// It will take the max precipitation amount for that day as the total for the day
fun List<PrecipitationEntry>.totalPrecipitationByDay(): Double {
    var total = 0.0
    this.groupBy { Pair(it.month, it.day) }
        .forEach {
            total += it.value.maxBy { entry ->
                entry.amount
            }?.amount ?: 0.0
        }
    return total
}

// Helpful extension method to get the total precipitation for a list of YearPrecipitationEntry
fun List<YearPrecipitationEntry>.totalPreciptation(): Double {
    return this.fold(0.0) { sum: Double, record: YearPrecipitationEntry ->
        sum + record.amount
    }
}

