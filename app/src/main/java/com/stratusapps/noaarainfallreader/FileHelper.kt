package com.stratusapps.noaarainfallreader

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import java.io.FileNotFoundException
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FileHelper {
    /* Launches the file reading to a background thread with a coroutine and reads each file asynchronously
     *  but waits until all files are read to return the results back
     * @return each files contents return in a list
     */
    suspend fun readFiles(context: Context): List<String> = suspendCoroutine {
        GlobalScope.launch(Dispatchers.IO) {
            val fileContents = context.assets.list("")?.map { fileName: String ->
                async {
                    try {
                        Log.d(javaClass.simpleName, "Reading File: $fileName")

                        val inputStream: InputStream = context.assets.open(fileName)
                        val reader = inputStream.reader()
                        val content = reader.readText()
                        content
                    } catch (ex: FileNotFoundException) {
                        ""
                    }
                }
            }?.awaitAll() ?: emptyList()
            it.resume(fileContents)
        }
    }
}