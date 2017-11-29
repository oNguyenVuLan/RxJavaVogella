package com.example.framgianguyenvulan.rxvogella.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/28/17.
 */

class Test internal constructor(context: Context) : SQLiteOpenHelper(context, "reactivestocks.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(WeatherTable.createTableQuery())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
    }
}
