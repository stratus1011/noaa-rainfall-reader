package com.stratusapps.noaarainfallreader

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    // Launches the setup on a background thread to not block the UI
    private fun setup() = GlobalScope.launch(Dispatchers.Default) {
        val contents = FileHelper.readFiles(this@MainActivity)
        val precipitations: List<YearPrecipitationEntry> =
            PrecipitationEntryHelper.createPrecipitationEntries(contents)
        showPrecipitationDetails(precipitations)
    }

    // Takes the precipitation entry data and displays it in the UI
    private suspend fun showPrecipitationDetails(precipitations: List<YearPrecipitationEntry>) =
        withContext(Dispatchers.Main) {
            val resultsListView = findViewById<ListView>(R.id.results_lv)
            val total = precipitations.totalPreciptation()

            findViewById<TextView>(R.id.total_tv).text = getString(R.string.overall_total_text, total)
            resultsListView.adapter = ArrayAdapter<String>(
                this@MainActivity,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                precipitations.sortedBy { it.year }.map { it.toString() }
            )
        }
}
