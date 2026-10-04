package com.example.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.example.data.model.Expense
import com.example.data.model.Note
import com.example.ui.viewmodel.DeleteConfirmState
import com.example.util.CurrencyUtils

@Composable
fun ConfirmDeleteDialog(
    deleteConfirmState: DeleteConfirmState,
    onDismiss: () -> Unit,
    onConfirmDeleteExpense: (Expense) -> Unit,
    onConfirmDeleteNote: (Note) -> Unit
) {
    when (deleteConfirmState) {
        DeleteConfirmState.Dismissed -> {}
        is DeleteConfirmState.ConfirmExpense -> {
            val expense = deleteConfirmState.expense
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(
                        text = "Delete Spend?",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete ${CurrencyUtils.format(expense.amount)} for \"${expense.description}\"?"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { onConfirmDeleteExpense(expense) },
                        modifier = Modifier.testTag("confirm_delete_button"),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("cancel_delete_button")
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
        is DeleteConfirmState.ConfirmNote -> {
            val note = deleteConfirmState.note
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(
                        text = "Delete Note?",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete this note: \"${note.text.take(40)}${if (note.text.length > 40) "..." else ""}\"?"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { onConfirmDeleteNote(note) },
                        modifier = Modifier.testTag("confirm_delete_note_button"),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("cancel_delete_note_button")
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
