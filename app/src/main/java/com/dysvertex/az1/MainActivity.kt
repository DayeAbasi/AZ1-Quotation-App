
package com.dysvertex.az1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DecimalFormat

private val Navy = Color(0xFF14243A)
private val Gold = Color(0xFFD4B45C)
private val LightBg = Color(0xFFF4F6F9)

data class QuoteItem(
    val description: String,
    val measurement: String,
    val quantity: Double,
    val rate: Double
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AZ1App()
        }
    }
}

@Composable
fun AZ1App() {
    val items = remember { mutableStateListOf<QuoteItem>() }
    var customer by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var measurement by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var rate by remember { mutableStateOf("") }
    var labour by remember { mutableStateOf("0") }
    var message by remember { mutableStateOf("") }

    val formatter = remember { DecimalFormat("#,##0.00") }
    val subtotal = items.sumOf { it.quantity * it.rate }
    val labourAmount = labour.toDoubleOrNull() ?: 0.0
    val grandTotal = subtotal + labourAmount

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Navy,
            secondary = Gold,
            background = LightBg
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBg)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Navy)
                    .padding(22.dp)
            ) {
                Text(
                    "DY'S VERTEX",
                    color = Gold,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "precision in every frame",
                    color = Color.White,
                    fontSize = 13.sp
                )
                Spacer(Modifier.height(22.dp))
                Text(
                    "Quotation System",
                    color = Color.White,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Create professional quotations",
                    color = Color.LightGray,
                    fontSize = 13.sp
                )
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SectionTitle("CUSTOMER INFORMATION")

                OutlinedTextField(
                    value = customer,
                    onValueChange = { customer = it },
                    label = { Text("Customer name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                SectionTitle("ADD QUOTATION ITEMS")

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = {
                        Text("Sliding windows using foreign materials")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = measurement,
                    onValueChange = { measurement = it },
                    label = { Text("Measurement (Width × Height)") },
                    placeholder = { Text("1000 × 1200") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = rate,
                        onValueChange = { rate = it },
                        label = { Text("Rate (₦)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Button(
                    onClick = {
                        val q = quantity.toDoubleOrNull()
                        val r = rate.toDoubleOrNull()

                        if (
                            description.isNotBlank() &&
                            measurement.isNotBlank() &&
                            q != null && q > 0 &&
                            r != null && r >= 0
                        ) {
                            items.add(
                                QuoteItem(
                                    description,
                                    measurement,
                                    q,
                                    r
                                )
                            )
                            description = ""
                            measurement = ""
                            quantity = "1"
                            rate = ""
                            message = "Item added successfully"
                        } else {
                            message = "Complete all item fields correctly"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Navy
                    )
                ) {
                    Text("＋ ADD ITEM", color = Color.White)
                }

                if (message.isNotEmpty()) {
                    Text(message, color = Navy, fontSize = 12.sp)
                }

                if (items.isNotEmpty()) {
                    SectionTitle("QUOTATION ITEMS")

                    items.forEachIndexed { index, item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${index + 1}. ${item.description}",
                                        modifier = Modifier.weight(1f),
                                        fontWeight = FontWeight.Bold
                                    )
                                    TextButton(
                                        onClick = { items.removeAt(index) }
                                    ) {
                                        Text("Remove", color = Color.Red)
                                    }
                                }

                                Text("Measurement: ${item.measurement}")
                                Text(
                                    "Qty: ${item.quantity} × ₦${formatter.format(item.rate)}"
                                )
                                Text(
                                    "Amount: ₦${formatter.format(item.quantity * item.rate)}",
                                    color = Navy,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                SectionTitle("ADDITIONAL COST")

                OutlinedTextField(
                    value = labour,
                    onValueChange = { labour = it },
                    label = { Text("Labour (₦)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Navy
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "QUOTATION SUMMARY",
                            color = Gold,
                            fontWeight = FontWeight.Bold
                        )
                        SummaryRow("Subtotal", subtotal, formatter)
                        SummaryRow("Labour", labourAmount, formatter)
                        HorizontalDivider(color = Color.Gray)
                        Text(
                            "GRAND TOTAL",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "₦${formatter.format(grandTotal)}",
                            color = Gold,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        items.clear()
                        customer = ""
                        description = ""
                        measurement = ""
                        quantity = "1"
                        rate = ""
                        labour = "0"
                        message = "Quotation form cleared"
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CLEAR QUOTATION")
                }

                Text(
                    "Dy's Vertex | Akwa Ibom State, Nigeria",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        title,
        color = Navy,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SummaryRow(
    title: String,
    amount: Double,
    formatter: DecimalFormat
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White)
        Text(
            "₦${formatter.format(amount)}",
            color = Color.White
        )
    }
}
