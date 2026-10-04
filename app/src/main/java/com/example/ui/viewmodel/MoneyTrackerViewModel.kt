package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.Expense
import com.example.data.model.Note
import com.example.data.repository.MoneyTrackerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ExpenseDialogState {
    data object Dismissed : ExpenseDialogState
    data object Add : ExpenseDialogState
    data class Edit(val expense: Expense) : ExpenseDialogState
}

sealed interface NoteDialogState {
    data object Dismissed : NoteDialogState
    data object Add : NoteDialogState
    data class Edit(val note: Note) : NoteDialogState
}

sealed interface DeleteConfirmState {
    data object Dismissed : DeleteConfirmState
    data class ConfirmExpense(val expense: Expense) : DeleteConfirmState
    data class ConfirmNote(val note: Note) : DeleteConfirmState
}

class MoneyTrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MoneyTrackerRepository

    init {
        val database = AppDatabase.getInstance(application)
        repository = MoneyTrackerRepository(
            context = application,
            expenseDao = database.expenseDao(),
            noteDao = database.noteDao()
        )
    }

    val monthlyTotal: StateFlow<Double> = repository.currentMonthTotal.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val allTimeTotal: StateFlow<Double> = repository.allTimeTotal.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val expenseCount: StateFlow<Int> = repository.expenseCount.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val recentExpenses: StateFlow<List<Expense>> = repository.getRecentExpenses(limit = 10).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allExpenses: StateFlow<List<Expense>> = repository.allExpenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allNotes: StateFlow<List<Note>> = repository.allNotes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Dialog States
    private val _expenseDialogState = MutableStateFlow<ExpenseDialogState>(ExpenseDialogState.Dismissed)
    val expenseDialogState: StateFlow<ExpenseDialogState> = _expenseDialogState.asStateFlow()

    private val _noteDialogState = MutableStateFlow<NoteDialogState>(NoteDialogState.Dismissed)
    val noteDialogState: StateFlow<NoteDialogState> = _noteDialogState.asStateFlow()

    private val _deleteConfirmState = MutableStateFlow<DeleteConfirmState>(DeleteConfirmState.Dismissed)
    val deleteConfirmState: StateFlow<DeleteConfirmState> = _deleteConfirmState.asStateFlow()

    fun showAddExpense() {
        _expenseDialogState.value = ExpenseDialogState.Add
    }

    fun showEditExpense(expense: Expense) {
        _expenseDialogState.value = ExpenseDialogState.Edit(expense)
    }

    fun dismissExpenseDialog() {
        _expenseDialogState.value = ExpenseDialogState.Dismissed
    }

    fun showAddNote() {
        _noteDialogState.value = NoteDialogState.Add
    }

    fun showEditNote(note: Note) {
        _noteDialogState.value = NoteDialogState.Edit(note)
    }

    fun dismissNoteDialog() {
        _noteDialogState.value = NoteDialogState.Dismissed
    }

    fun confirmDeleteExpense(expense: Expense) {
        _deleteConfirmState.value = DeleteConfirmState.ConfirmExpense(expense)
    }

    fun confirmDeleteNote(note: Note) {
        _deleteConfirmState.value = DeleteConfirmState.ConfirmNote(note)
    }

    fun dismissDeleteDialog() {
        _deleteConfirmState.value = DeleteConfirmState.Dismissed
    }

    fun saveExpense(
        id: Long = 0,
        amount: Double,
        description: String,
        category: String?,
        date: Long,
        createdAt: Long = System.currentTimeMillis()
    ) {
        viewModelScope.launch {
            if (id == 0L) {
                repository.addExpense(
                    Expense(
                        amount = amount,
                        description = description.trim(),
                        category = category?.ifBlank { null },
                        date = date,
                        createdAt = createdAt
                    )
                )
            } else {
                repository.updateExpense(
                    Expense(
                        id = id,
                        amount = amount,
                        description = description.trim(),
                        category = category?.ifBlank { null },
                        date = date,
                        createdAt = createdAt
                    )
                )
            }
            _expenseDialogState.value = ExpenseDialogState.Dismissed
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
            _deleteConfirmState.value = DeleteConfirmState.Dismissed
        }
    }

    fun saveNote(id: Long = 0, text: String, createdAt: Long = System.currentTimeMillis()) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (id == 0L) {
                repository.addNote(
                    Note(
                        text = text.trim(),
                        createdAt = now,
                        updatedAt = now
                    )
                )
            } else {
                repository.updateNote(
                    Note(
                        id = id,
                        text = text.trim(),
                        createdAt = createdAt,
                        updatedAt = now
                    )
                )
            }
            _noteDialogState.value = NoteDialogState.Dismissed
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
            _deleteConfirmState.value = DeleteConfirmState.Dismissed
        }
    }
}
