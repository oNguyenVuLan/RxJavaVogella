package com.example.framgianguyenvulan.rxvogella.db

import android.content.Context
import android.database.Cursor
import com.example.framgianguyenvulan.rxvogella.model.Weather
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/28/17.
 */
class StorIOFactory {
    companion object {
        var INSTANCE: StorIOSQLite? = null

        fun get(context: Context): StorIOSQLite? {
            if (INSTANCE != null) return INSTANCE

            INSTANCE = DefaultStorIOSQLite.builder()
                    .sqliteOpenHelper(StorIOHelper(context))
                    .addTypeMapping(Weather::class.java, SQLiteTypeMapping.builder<Weather>()
                            .putResolver(WeatherPutResolver())
                            .getResolver(createGetResolver())
                            .deleteResolver(createDeleteResolver())
                            .build())
                    .build()
            return INSTANCE
        }

        fun createDeleteResolver(): DeleteResolver<Weather> {
            return object : DefaultDeleteResolver<Weather>() {
                override fun mapToDeleteQuery(`object`: Weather): DeleteQuery {
                    return null
                }
            }
        }

        fun createGetResolver(): GetResolver<Weather> {
            return object : DefaultGetResolver<Weather>() {
                override fun mapFromCursor(cursor: Cursor): Weather {
                    return null
                }

            }
        }
    }
}