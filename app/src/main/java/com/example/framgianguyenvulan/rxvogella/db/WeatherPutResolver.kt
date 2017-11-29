package com.example.framgianguyenvulan.rxvogella.db

import android.content.ContentValues
import android.support.annotation.NonNull
import com.example.framgianguyenvulan.rxvogella.model.Weather
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver
import com.pushtorefresh.storio.sqlite.queries.InsertQuery
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery

/**
 * Created by FRAMGIA\nguyen.vu.lan on 11/27/17.
 */
class WeatherPutResolver : DefaultPutResolver<Weather>() {
    override fun mapToInsertQuery(`object`: Weather): InsertQuery {
        return InsertQuery.builder()
                .table(WeatherTable.COLUMNS.TABLE)
                .build()
    }

    override fun mapToContentValues(entity: Weather): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(WeatherTable.COLUMNS.ID,entity.id.toString())
        contentValues.put(WeatherTable.COLUMNS.DESCRIPTION, entity.description)
        contentValues.put(WeatherTable.COLUMNS.ICON, entity.icon)
        contentValues.put(WeatherTable.COLUMNS.MAIN,entity.main)
        return contentValues
    }

    @NonNull
    override fun mapToUpdateQuery(`object`: Weather): UpdateQuery {
        return UpdateQuery.builder()
                .table(WeatherTable.COLUMNS.TABLE)
                .where(WeatherTable.COLUMNS.ID + "=?")
                .build()
    }
}