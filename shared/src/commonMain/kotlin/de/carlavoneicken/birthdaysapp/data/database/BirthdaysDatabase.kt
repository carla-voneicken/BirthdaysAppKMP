package de.carlavoneicken.birthdaysapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.carlavoneicken.birthdaysapp.data.utils.LocalDateConverters

@Database(
    entities = [BirthdayEntity::class, ReminderEntity::class],
    version = 1
)
@TypeConverters(LocalDateConverters::class)
// Not used because of Koin (says ChatGPT)
//@ConstructedBy(BirthdaysDatabaseConstructor::class)
abstract class BirthdaysDatabase: RoomDatabase() {
    abstract fun getBirthdayDao(): BirthdayDao
    abstract fun getReminderDao(): ReminderDao
}

// Not used because of Koin (says ChatGPT)
//@Suppress("KotlinNoActualForExpect")
//expect object BirthdaysDatabaseConstructor : RoomDatabaseConstructor<BirthdaysDatabase> {
//    override fun initialize(): BirthdaysDatabase
//}

