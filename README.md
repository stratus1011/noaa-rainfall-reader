 # NOAA Rainfall Reader
  
 ### How it works
 *Note: The rainfall data files for are stored in the `assets` folder.*
  
 1) Each file is read in asynchronously on a background thread and the contents of each file are placed as an entry in a list
 2) This list of file contents is then fed to a function that parses the data into a list of usable data objects of type `PrecipitationEntry`
    * This function will filter out any unparseable and non-Arizona entries
    * Returns a list of `PrecipationEntry` where each entry is the total rainfall for a given year
 4) The data returned from the previous function is then given to a function to present it in the UI


### Files
* `MainActivity.kt` - It's main purpose is to initiate the logic to interpret the files and then to display them. In a full application some of the logical code would be pushed to a ViewModel so the view only has responsibility over displaying the data, but this app is very simple so I left it in there.
* `FileHelper.kt` - Provides a method to read the contents of the files in the `assets` foler
* `PrecipitationEntryHelper.kt` - The main logic for parsing the data files lies in this helper class
* `EntryPositions.kt` - This enum abstracts any index ranges that are defined for different data elements in the file entries
* `PrecipitationEntry.kt` - The main data class that contains the parsed data.


### Notes
I took a more simple approach to parsing only the data that was necessary. There were a lot of data points and a lot of different rules. After reading through them all it boiled down to need to parse out the Year, State Code, and  the `2500` time entry, which contains the rainfall total for that entire day. If the `2500` time entry was anything other than a parseable number it meant there was something wrong with the data for that day so I just skipped reading that information.

  
