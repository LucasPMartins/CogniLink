package com.example.cognilink.data.datebase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cognilink.data.datebase.entities.UserStatsEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserStatsDao {
    @Query("SELECT * FROM users_stats WHERE userId = :userId")
    fun getUserStats(userId: String): Flow<UserStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: UserStatsEntity)

    @Update
    suspend fun updateStats(stats: UserStatsEntity)

    @Query("DELETE FROM users_stats WHERE userId = :userId")
    suspend fun deleteStatsForUser(userId: String)
}
