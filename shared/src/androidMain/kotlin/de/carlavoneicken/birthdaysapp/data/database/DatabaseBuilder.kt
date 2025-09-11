package de.carlavoneicken.birthdaysapp.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<BirthdaysDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("birthdays_database.db")

    return Room.databaseBuilder<BirthdaysDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}