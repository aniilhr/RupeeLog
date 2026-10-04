package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC, createdAt DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses ORDER BY date DESC, createdAt DESC LIMIT :limit")
    fun getRecentExpenses(limit: Int): Flow<List<Expense>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :startDate AND date <= :endDate")
    fun getMonthlyTotal(startDate: Long, endDate: Long): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :startDate AND date <= :endDate")
    suspend fun getMonthlyTotalDirect(startDate: Long, endDate: Long): Double?

    @Query("SELECT SUM(amount) FROM expenses")
    fun getAllTimeTotal(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM expenses")
    fun getExpenseCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)
}
