package de.carlavoneicken.birthdaysapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BirthdayDao {

    @Query("SELECT * FROM birthdays WHERE id = :id")
    fun observeSingleBirthday(id: Long): Flow<BirthdayEntity?>

    @Query("SELECT * FROM birthdays")
    fun observeAllBirthdays(): Flow<List<BirthdayEntity>>

    @Query("SELECT * FROM birthdays ORDER BY name")
    fun observeBirthdaysSortedByName(): Flow<List<BirthdayEntity>>

    @Insert
    suspend fun createBirthday(birthday: BirthdayEntity): Long

    @Update
    suspend fun updateBirthday(birthday: BirthdayEntity)

    @Query("DELETE FROM birthdays WHERE id = :id")
    suspend fun deleteById(id: Long): Int

}