package com.example.data.repository

import android.content.Context
import com.example.data.local.ExpenseDao
import com.example.data.local.NoteDao
import com.example.data.model.Expense
import com.example.data.model.Note
import com.example.util.DateUtils
import com.example.widget.MoneyTrackerWidgetProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoneyTrackerRepository(
    private val context: Context,
    private val expenseDao: ExpenseDao,
    private val noteDao: NoteDao
) {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    fun getRecentExpenses(limit: Int = 10): Flow<List<Expense>> =
        expenseDao.getRecentExpenses(limit)

    val currentMonthTotal: Flow<Double>
        get() {
            val (start, end) = DateUtils.getCurrentMonthRange()
            return expenseDao.getMonthlyTotal(start, end).map { it ?: 0.0 }
        }

    val allTimeTotal: Flow<Double> =
        expenseDao.getAllTimeTotal().map { it ?: 0.0 }

    val expenseCount: Flow<Int> = expenseDao.getExpenseCount()

    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun addExpense(expense: Expense): Long {
        val id = expenseDao.insertExpense(expense)
        updateWidget()
        return id
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
        updateWidget()
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
        updateWidget()
    }

    suspend fun addNote(note: Note): Long {
        return noteDao.insertNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    private fun updateWidget() {
        try {
            MoneyTrackerWidgetProvider.updateAllWidgets(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
