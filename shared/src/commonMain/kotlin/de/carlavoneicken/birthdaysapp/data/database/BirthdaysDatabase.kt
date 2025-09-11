package de.carlavoneicken.birthdaysapp.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(
    entities = [BirthdayEntity::class],
    version = 1
)
@ConstructedBy(BirthdaysDatabaseConstructor::class)
abstract class BirthdaysDatabase: RoomDatabase() {
    abstract fun getBirthdayDao(): BirthdayDao
}

@Suppress("KotlinNoActualForExpect")
expect object BirthdaysDatabaseConstructor : RoomDatabaseConstructor<BirthdaysDatabase> {
    override fun initialize(): BirthdaysDatabase
}

