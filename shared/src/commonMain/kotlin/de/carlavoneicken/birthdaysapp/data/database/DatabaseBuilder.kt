package de.carlavoneicken.birthdaysapp.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun getAppDatabase(builder: RoomDatabase.Builder<BirthdaysDatabase>): BirthdaysDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        // database should use Dispatchers.IO for executing asynchronous queries
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}