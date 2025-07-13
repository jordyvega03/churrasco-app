package com.example.churrasquin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modelo de datos
data class Churrasco(
    val id: Int,
    val nombre: String,
    val tipoCarne: String,
    val termino: String,
    val tamano: String,
    val porciones: Int,
    val churrascoGuarniciones: List<String>,
    val precio: Double,
    val urlImagen: Int? = null // Usamos recurso drawable local para ejemplo
)

class ChurrascosActivity : ComponentActivity() {

    private val demoChurrascos = listOf(
        Churrasco(1, "Churrasco Familiar", "Puyazo", "Término medio", "Personal", 2, listOf("Frijoles", "Ensalada"), 65.00, R.drawable.churrasco1),
        Churrasco(2, "Churrasco Especial", "Culotte", "Término tres cuartos", "Personal", 2, listOf("Papas fritas"), 55.00, R.drawable.churrasco2),
        Churrasco(3, "Churrasco Primium", "Costilla", "Bien cocido", "Personal", 2, listOf("Chimichurri"), 50.00, R.drawable.churrasco3),
        Churrasco(4, "Churrasco Primium Especial Pro max Vivo", "Preium", "Termino vivo", "Personal", 2, listOf("Ensalada", "Papas"), 75.00, R.drawable.churrasco4)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ChurrascosScreen(
                    churrascos = demoChurrascos,
                    onComprarClick = { id -> /* Navegar o manejar compra con id */ },
                    onVolverClick = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChurrascosScreen(
    churrascos: List<Churrasco>,
    onComprarClick: (Int) -> Unit,
    onVolverClick: () -> Unit
) {
    var selectedChurrasco by remember { mutableStateOf<Churrasco?>(null) }
    var modalAbierto by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDFDFD))
            .padding(16.dp)
    ) {
        Button(
            onClick = onVolverClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD81B60))
        ) {
            Text("← Volver al Home", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Churrascos",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (churrascos.isEmpty()) {
            Text(
                "No hay churrascos disponibles.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(churrascos) { c ->
                    ChurrascoCard(
                        churrasco = c,
                        onClick = {
                            selectedChurrasco = c
                            modalAbierto = true
                        },
                        onComprarClick = {
                            onComprarClick(c.id)
                        }
                    )
                }
            }
        }
    }

    if (modalAbierto && selectedChurrasco != null) {
        ChurrascoDetalleModal(
            churrasco = selectedChurrasco!!,
            onCerrar = { modalAbierto = false; selectedChurrasco = null },
            onComprarClick = { id ->
                onComprarClick(id)
                modalAbierto = false
                selectedChurrasco = null
            }
        )
    }
}

@Composable
fun ChurrascoCard(
    churrasco: Churrasco,
    onClick: () -> Unit,
    onComprarClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            churrasco.urlImagen?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = churrasco.nombre,
                    modifier = Modifier
                        .size(width = 120.dp, height = 90.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(churrasco.nombre, color = Color(0xFFD81B60), fontWeight = FontWeight.Bold)
                Text("Tipo Carne: ${churrasco.tipoCarne}")
                Text("Término: ${churrasco.termino}")
                Text("Tamaño: ${churrasco.tamano}")
                Text("Porciones: ${churrasco.porciones}")
                Text(
                    "Q ${String.format("%.2f", churrasco.precio)}",
                    color = Color(0xFFD81B60),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            IconButton(
                onClick = onComprarClick,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Comprar ${churrasco.nombre}",
                    tint = Color(0xFFD81B60)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChurrascoDetalleModal(
    churrasco: Churrasco,
    onCerrar: () -> Unit,
    onComprarClick: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onCerrar,
        confirmButton = {
            TextButton(onClick = { onComprarClick(churrasco.id) }) {
                Text("Comprar", color = Color(0xFFD81B60))
            }
        },
        dismissButton = {
            TextButton(onClick = onCerrar) {
                Text("Cerrar")
            }
        },
        title = {
            Text(churrasco.nombre, fontWeight = FontWeight.Bold, color = Color(0xFFD81B60))
        },
        text = {
            Column {
                churrasco.urlImagen?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = churrasco.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Text("Tipo de Carne: ${churrasco.tipoCarne}")
                Text("Término: ${churrasco.termino}")
                Text("Tamaño: ${churrasco.tamano}")
                Text("Porciones: ${churrasco.porciones}")
                Text("Guarniciones: ${if (churrasco.churrascoGuarniciones.isNotEmpty()) churrasco.churrascoGuarniciones.joinToString(", ") else "Sin guarniciones"}")
                Spacer(modifier = Modifier.height(12.dp))
                Text("Precio: Q ${String.format("%.2f", churrasco.precio)}", fontWeight = FontWeight.Bold, color = Color(0xFFD81B60))
            }
        }
    )
}