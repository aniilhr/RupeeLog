package com.example.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Expense
import com.example.util.DateUtils
import java.util.Calendar

private val STANDARD_CATEGORIES = listOf("Food", "Travel", "Shopping", "Other")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditExpenseDialog(
    expenseToEdit: Expense? = null,
    onDismiss: () -> Unit,
    onSave: (amount: Double, description: String, category: String?, date: Long) -> Unit
) {
    val context = LocalContext.current
    val isEditing = expenseToEdit != null

    var amountText by remember {
        mutableStateOf(
            if (expenseToEdit != null) {
                if (expenseToEdit.amount % 1.0 == 0.0) {
                    expenseToEdit.amount.toLong().toString()
                } else {
                    expenseToEdit.amount.toString()
                }
            } else ""
        )
    }
    var descriptionText by remember { mutableStateOf(expenseToEdit?.description ?: "") }
    var selectedCategory by remember { mutableStateOf(expenseToEdit?.category) }
    var selectedDateMillis by remember {
        mutableLongStateOf(expenseToEdit?.date ?: System.currentTimeMillis())
    }

    var amountError by remember { mutableStateOf<String?>(null) }
    var descError by remember { mutableStateOf<String?>(null) }

    fun showDatePicker() {
        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val newCal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                selectedDateMillis = newCal.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEditing) "Edit Spend" else "Add Spend",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("dialog_close_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Amount
                Text(
                    text = "Amount",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { input ->
                        // Allow digits and at most one decimal point
                        if (input.isEmpty() || input.matches(Regex("""^\d*(\.\d{0,2})?$"""))) {
                            amountText = input
                            if (amountError != null) amountError = null
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("amount_input"),
                    leadingIcon = {
                        Text(
                            text = "₹",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    },
                    placeholder = { Text("0") },
                    singleLine = true,
                    isError = amountError != null,
                    supportingText = amountError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Description
                Text(
                    text = "What did you spend on?",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = descriptionText,
                    onValueChange = {
                        descriptionText = it
                        if (descError != null) descError = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("description_input"),
                    placeholder = { Text("e.g. Lunch, Tea, Groceries") },
                    singleLine = true,
                    isError = descError != null,
                    supportingText = descError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(14.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category (Optional)
                Text(
                    text = "Category (optional)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    STANDARD_CATEGORIES.forEach { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCategory = if (isSelected) null else category
                            },
                            label = { Text(category) },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.testTag("category_chip_$category")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date
                Text(
                    text = "Date",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker() }
                        .testTag("date_picker_button"),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Select Date",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = DateUtils.formatExpenseDate(selectedDateMillis),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("cancel_expense_button"),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val parsedAmount = amountText.toDoubleOrNull()
                            if (parsedAmount == null || parsedAmount <= 0) {
                                amountError = "Enter a valid amount"
                                return@Button
                            }
                            if (descriptionText.trim().isBlank()) {
                                descError = "Enter a description"
                                return@Button
                            }

                            onSave(
                                parsedAmount,
                                descriptionText.trim(),
                                selectedCategory,
                                selectedDateMillis
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("save_expense_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Save",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
