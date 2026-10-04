package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddEditExpenseDialog
import com.example.ui.components.AddEditNoteDialog
import com.example.ui.components.ConfirmDeleteDialog
import com.example.ui.viewmodel.DeleteConfirmState
import com.example.ui.viewmodel.ExpenseDialogState
import com.example.ui.viewmodel.MoneyTrackerViewModel
import com.example.ui.viewmodel.NoteDialogState

enum class ScreenTab {
    HOME,
    TRANSACTIONS,
    NOTES
}

@Composable
fun MainScreen(
    viewModel: MoneyTrackerViewModel,
    modifier: Modifier = Modifier
) {
    var currentTab by rememberSaveable { mutableStateOf(ScreenTab.HOME) }

    // Back handler: pop to Home if on another tab
    BackHandler(enabled = currentTab != ScreenTab.HOME) {
        currentTab = ScreenTab.HOME
    }

    // State collections
    val monthlyTotal by viewModel.monthlyTotal.collectAsStateWithLifecycle()
    val allTimeTotal by viewModel.allTimeTotal.collectAsStateWithLifecycle()
    val expenseCount by viewModel.expenseCount.collectAsStateWithLifecycle()
    val recentExpenses by viewModel.recentExpenses.collectAsStateWithLifecycle()
    val allExpenses by viewModel.allExpenses.collectAsStateWithLifecycle()
    val allNotes by viewModel.allNotes.collectAsStateWithLifecycle()

    val expenseDialogState by viewModel.expenseDialogState.collectAsStateWithLifecycle()
    val noteDialogState by viewModel.noteDialogState.collectAsStateWithLifecycle()
    val deleteConfirmState by viewModel.deleteConfirmState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == ScreenTab.HOME,
                    onClick = { currentTab = ScreenTab.HOME },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = {
                        Text(
                            text = "Home",
                            fontWeight = if (currentTab == ScreenTab.HOME) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                NavigationBarItem(
                    selected = currentTab == ScreenTab.TRANSACTIONS,
                    onClick = { currentTab = ScreenTab.TRANSACTIONS },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.TRANSACTIONS) Icons.Filled.ReceiptLong else Icons.Outlined.ReceiptLong,
                            contentDescription = "Transactions"
                        )
                    },
                    label = {
                        Text(
                            text = "Transactions",
                            fontWeight = if (currentTab == ScreenTab.TRANSACTIONS) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_transactions")
                )

                NavigationBarItem(
                    selected = currentTab == ScreenTab.NOTES,
                    onClick = { currentTab = ScreenTab.NOTES },
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.NOTES) Icons.Filled.Description else Icons.Outlined.Description,
                            contentDescription = "Notes"
                        )
                    },
                    label = {
                        Text(
                            text = "Notes",
                            fontWeight = if (currentTab == ScreenTab.NOTES) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("nav_notes")
                )
            }
        }
    ) { innerPadding ->
        when (currentTab) {
            ScreenTab.HOME -> {
                HomeScreen(
                    monthlyTotal = monthlyTotal,
                    recentExpenses = recentExpenses,
                    onAddSpendClick = { viewModel.showAddExpense() },
                    onEditExpense = { viewModel.showEditExpense(it) },
                    onDeleteExpense = { viewModel.confirmDeleteExpense(it) },
                    onViewAllTransactions = { currentTab = ScreenTab.TRANSACTIONS },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            ScreenTab.TRANSACTIONS -> {
                TransactionsScreen(
                    monthlyTotal = monthlyTotal,
                    allTimeTotal = allTimeTotal,
                    expenseCount = expenseCount,
                    expenses = allExpenses,
                    onAddSpendClick = { viewModel.showAddExpense() },
                    onEditExpense = { viewModel.showEditExpense(it) },
                    onDeleteExpense = { viewModel.confirmDeleteExpense(it) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            ScreenTab.NOTES -> {
                NotesScreen(
                    notes = allNotes,
                    onAddNoteClick = { viewModel.showAddNote() },
                    onEditNote = { viewModel.showEditNote(it) },
                    onDeleteNote = { viewModel.confirmDeleteNote(it) },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    // Expense Add/Edit Dialog
    when (val state = expenseDialogState) {
        ExpenseDialogState.Dismissed -> {}
        ExpenseDialogState.Add -> {
            AddEditExpenseDialog(
                expenseToEdit = null,
                onDismiss = { viewModel.dismissExpenseDialog() },
                onSave = { amount, description, category, date ->
                    viewModel.saveExpense(
                        amount = amount,
                        description = description,
                        category = category,
                        date = date
                    )
                }
            )
        }
        is ExpenseDialogState.Edit -> {
            AddEditExpenseDialog(
                expenseToEdit = state.expense,
                onDismiss = { viewModel.dismissExpenseDialog() },
                onSave = { amount, description, category, date ->
                    viewModel.saveExpense(
                        id = state.expense.id,
                        amount = amount,
                        description = description,
                        category = category,
                        date = date,
                        createdAt = state.expense.createdAt
                    )
                }
            )
        }
    }

    // Note Add/Edit Dialog
    when (val state = noteDialogState) {
        NoteDialogState.Dismissed -> {}
        NoteDialogState.Add -> {
            AddEditNoteDialog(
                noteToEdit = null,
                onDismiss = { viewModel.dismissNoteDialog() },
                onSave = { text ->
                    viewModel.saveNote(text = text)
                }
            )
        }
        is NoteDialogState.Edit -> {
            AddEditNoteDialog(
                noteToEdit = state.note,
                onDismiss = { viewModel.dismissNoteDialog() },
                onSave = { text ->
                    viewModel.saveNote(
                        id = state.note.id,
                        text = text,
                        createdAt = state.note.createdAt
                    )
                }
            )
        }
    }

    // Delete Confirmation Dialog
    ConfirmDeleteDialog(
        deleteConfirmState = deleteConfirmState,
        onDismiss = { viewModel.dismissDeleteDialog() },
        onConfirmDeleteExpense = { viewModel.deleteExpense(it) },
        onConfirmDeleteNote = { viewModel.deleteNote(it) }
    )
}
