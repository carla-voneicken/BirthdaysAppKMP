package de.carlavoneicken.birthdaysapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow

@Dao
interface BirthdayDao {

    @NativeCoroutines
    @Query("SELECT * FROM birthdays WHERE id = :id")
    fun observeSingleBirthday(id: Long): Flow<BirthdayEntity?>

    @NativeCoroutines
    @Query("SELECT * FROM birthdays")
    fun observeAllBirthdays(): Flow<List<BirthdayEntity>>

    @NativeCoroutines
    @Query("SELECT * FROM birthdays ORDER BY name")
    fun observeBirthdaysSortedByName(): Flow<List<BirthdayEntity>>

    @NativeCoroutines
    @Insert
    suspend fun createBirthday(birthday: BirthdayEntity): Long

    @NativeCoroutines
    @Update
    suspend fun updateBirthday(birthday: BirthdayEntity)

    @NativeCoroutines
    @Query("DELETE FROM birthdays WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Transaction
    @Query("SELECT * FROM birthdays")
    fun getAllBirthdaysWithReminders(): List<BirthdayWithRemindersEntity>


    @NativeCoroutines
    @Transaction
    @Query("SELECT * FROM birthdays WHERE id = :id")
    fun observeBirthdayWithReminders(id: Long): Flow<BirthdayWithRemindersEntity?>
}