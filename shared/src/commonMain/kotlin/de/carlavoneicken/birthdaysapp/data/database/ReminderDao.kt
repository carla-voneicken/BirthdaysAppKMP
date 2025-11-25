package de.carlavoneicken.birthdaysapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.datetime.LocalDate

@Dao
interface ReminderDao {
    @NativeCoroutines
    @Insert
    suspend fun insertAll(reminders: List<ReminderEntity>)

    @NativeCoroutines
    @Query("DELETE FROM reminders WHERE birthdayId = :birthdayId")
    suspend fun deleteByBirthdayId(birthdayId: Long)

    @NativeCoroutines
    @Query("""
    UPDATE reminders 
    SET lastTriggeredDate = :date 
    WHERE id = :id
    """)
    suspend fun updateLastTriggeredData(id: Long, date: LocalDate)
}
