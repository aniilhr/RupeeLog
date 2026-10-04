package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Expense
import com.example.util.CurrencyUtils

@Composable
fun TransactionsScreen(
    monthlyTotal: Double,
    allTimeTotal: Double,
    expenseCount: Int,
    expenses: List<Expense>,
    onAddSpendClick: () -> Unit,
    onEditExpense: (Expense) -> Unit,
    onDeleteExpense: (Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

    val filteredExpenses by remember(expenses, selectedCategoryFilter) {
        derivedStateOf {
            if (selectedCategoryFilter == null) {
                expenses
            } else {
                expenses.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
            }
        }
    }

    val filterOptions = listOf("All", "Food", "Travel", "Shopping", "Other")

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("transactions_screen"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddSpendClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("transactions_fab_add")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Spend"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Screen Header
            item {
                Column(modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)) {
                    Text(
                        text = "Transactions",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "All your spending history",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Totals Summary Header (Main focus: This Month's Total)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // This Month (Primary focus)
                    Card(
                        modifier = Modifier
                            .weight(1.2f)
                            .testTag("trans_month_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "This Month",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = CurrencyUtils.format(monthlyTotal),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.testTag("trans_monthly_total")
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Monthly spending",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // All-Time
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .testTag("trans_all_time_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "All-Time",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = CurrencyUtils.format(allTimeTotal),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.testTag("trans_all_time_total")
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$expenseCount spends",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Quick Category Filters
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filterOptions) { filter ->
                        val isSelected = if (filter == "All") {
                            selectedCategoryFilter == null
                        } else {
                            selectedCategoryFilter == filter
                        }

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCategoryFilter = if (filter == "All") null else filter
                            },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.testTag("filter_$filter")
                        )
                    }
                }
            }

            // Transactions List
            if (filteredExpenses.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp, horizontal = 20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (selectedCategoryFilter != null) "No spends in $selectedCategoryFilter" else "No transactions yet",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap + to add a new spend",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredExpenses, key = { it.id }) { expense ->
                    SpendItemCard(
                        expense = expense,
                        onEdit = { onEditExpense(expense) },
                        onDelete = { onDeleteExpense(expense) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
