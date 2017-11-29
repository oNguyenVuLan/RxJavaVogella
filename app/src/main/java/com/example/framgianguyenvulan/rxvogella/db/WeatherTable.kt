package com.example.framgianguyenvulan.rxvogella.db

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/27/17.
 */
class WeatherTable {

    companion object COLUMNS {
        val TABLE: String = "weather"
        val ID: String = ""
        val MAIN: String = "main"
        val DESCRIPTION: String = "description"
        val ICON: String = "icon"

        fun createTableQuery(): String = "CREATE TABLE" + TABLE +
                "(" + ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MAIN + " TEXT NOT NULL, " +
                DESCRIPTION + " LONG NOT NULL, " +
                ICON + " LONG NOT NULL" + ");"
    }
}