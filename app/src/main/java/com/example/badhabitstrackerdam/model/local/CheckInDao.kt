package com.example.badhabitstrackerdam.model.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckInDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckIn(checkIn: CheckInEntity)

    @Query("SELECT * FROM check_ins WHERE habitId = :habitId ORDER BY timestamp DESC")
    fun getCheckInsForHabit(habitId: Int): Flow<List<CheckInEntity>>

    @Query("SELECT COUNT(*) FROM check_ins WHERE habitId = :habitId")
    suspend fun getCheckInCount(habitId: Int): Int

    // Verifică dacă există check-in pentru astăzi
    @Query("""
        SELECT COUNT(*) > 0 FROM check_ins 
        WHERE habitId = :habitId 
        AND timestamp >= :dayStartTimestamp 
        AND timestamp < :dayEndTimestamp
    """)
    suspend fun hasCheckInToday(habitId: Int, dayStartTimestamp: Long, dayEndTimestamp: Long): Boolean

    @Delete
    suspend fun deleteCheckIn(checkIn: CheckInEntity)

    // Șterge TOATE check-in-urile de astăzi (folosit pentru cleanup)
    @Query("""
        DELETE FROM check_ins 
        WHERE habitId = :habitId 
        AND timestamp >= :dayStartTimestamp 
        AND timestamp < :dayEndTimestamp
    """)
    suspend fun deleteAllCheckInsToday(habitId: Int, dayStartTimestamp: Long, dayEndTimestamp: Long)

    // Șterge DOAR primul check-in găsit pentru astăzi (pentru undo single check-in)
    @Query("""
        DELETE FROM check_ins 
        WHERE id IN (
            SELECT id FROM check_ins 
            WHERE habitId = :habitId 
            AND timestamp >= :dayStartTimestamp 
            AND timestamp < :dayEndTimestamp
            ORDER BY timestamp DESC
            LIMIT 1
        )
    """)
    suspend fun deleteOneCheckInToday(habitId: Int, dayStartTimestamp: Long, dayEndTimestamp: Long)

    // CLEANUP: Păstrează doar 1 check-in pe zi (cel mai recent)
    // Util pentru a curăța duplicate-urile vechi create prin spam
    @Query("""
        DELETE FROM check_ins 
        WHERE id NOT IN (
            SELECT MAX(id) FROM check_ins 
            WHERE habitId = :habitId
            GROUP BY date(timestamp / 1000, 'unixepoch')
        )
        AND habitId = :habitId
    """)
    suspend fun cleanupDuplicateCheckIns(habitId: Int)

    // Curăță duplicate-uri pentru toate habit-urile
    @Query("""
        DELETE FROM check_ins 
        WHERE id NOT IN (
            SELECT MAX(id) FROM check_ins 
            GROUP BY habitId, date(timestamp / 1000, 'unixepoch')
        )
    """)
    suspend fun cleanupAllDuplicates()

    // Count check-ins unice (1 pe zi) - folosit pentru calculul corect al progresului
    @Query("""
        SELECT COUNT(DISTINCT date(timestamp / 1000, 'unixepoch'))
        FROM check_ins 
        WHERE habitId = :habitId
    """)
    suspend fun getUniqueDaysCheckInCount(habitId: Int): Int
}