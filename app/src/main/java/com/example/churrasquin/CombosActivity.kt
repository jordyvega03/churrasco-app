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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

data class Combo(
    val id: Int,
    val nombre: String,
    val tipo: String,
    val tipoCombo: String,
    val precio: Double,
    val observaciones: String?,
    val churrascos: List<String>,
    val dulces: List<String>,
    val urlImagen: Int? = null // Drawable resource id
)

class CombosActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CombosScreen(
                onNavigateBack = { finish() },
                onComprar = { comboId ->
                    // Aquí la navegación, por ejemplo abrir otra Activity
                    // startActivity(Intent(this, VentaActivity::class.java).apply {
                    //     putExtra("productoId", comboId)
                    //     putExtra("tipo", "combo")
                    // })
                }
            )
        }
    }
}

@Composable
fun CombosScreen(onNavigateBack: () -> Unit, onComprar: (Int) -> Unit) {
    var combos by remember { mutableStateOf<List<Combo>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    var modalOpen by remember { mutableStateOf(false) }
    var selectedCombo by remember { mutableStateOf<Combo?>(null) }

    // Simular carga datos (reemplazar con llamada real)
    LaunchedEffect(Unit) {
        try {
            delay(1000) // Simular retraso
            combos = sampleCombos() // Aquí va llamada API real
            loading = false
        } catch (e: Exception) {
            error = "Error cargando combos"
            loading = false
        }
    }

    // Fondo con gradiente rosa a amarillo
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFED7E2), Color(0xFFFFF4D9))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            color = Color.White.copy(alpha = 0.95f),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onNavigateBack,
                    modifier = Modifier.align(Alignment.Start),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD81B60))
                ) {
                    Text(text = "← Volver al Home", color = Color.White)
                }

                Text(
                    text = "Combos",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                when {
                    loading -> Text("Cargando...", color = Color.Gray)
                    error != null -> Text(error ?: "", color = Color.Red)
                    combos.isEmpty() -> Text("No hay combos disponibles.", color = Color.Gray)
                    else -> LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(combos) { combo ->
                            ComboCard(combo, onClick = {
                                selectedCombo = combo
                                modalOpen = true
                            }, onComprar = { onComprar(combo.id) })
                        }
                    }
                }
            }
        }
    }

    if (modalOpen && selectedCombo != null) {
        ComboDetailDialog(
            combo = selectedCombo!!,
            onClose = { modalOpen = false },
            onComprar = {
                onComprar(it)
                modalOpen = false
            }
        )
    }
}

@Composable
fun ComboCard(combo: Combo, onClick: () -> Unit, onComprar: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                combo.urlImagen?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = combo.nombre,
                        modifier = Modifier
                            .size(width = 160.dp, height = 112.dp)
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = combo.nombre,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color(0xFFC2185B),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "Tipo: ${combo.tipoCombo}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Q %.2f".format(combo.precio),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFC2185B),
                    fontSize = 16.sp
                )
            }

            IconButton(
                onClick = {
                    onComprar()
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Comprar ${combo.nombre}",
                    tint = Color(0xFFD81B60)
                )
            }
        }
    }
}

@Composable
fun ComboDetailDialog(combo: Combo, onClose: () -> Unit, onComprar: (Int) -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(text = combo.nombre, color = Color(0xFFC2185B), fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text("Tipo de Combo: ${combo.tipoCombo}")
                combo.observaciones?.let {
                    Text("Observaciones: $it")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Churrascos:")
                if (combo.churrascos.isNotEmpty()) {
                    combo.churrascos.forEach { churrasco ->
                        Text("• $churrasco")
                    }
                } else {
                    Text("Sin churrascos asignados")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Dulces:")
                if (combo.dulces.isNotEmpty()) {
                    combo.dulces.forEach { dulce ->
                        Text("• $dulce")
                    }
                } else {
                    Text("Sin dulces asignados")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Precio: Q %.2f".format(combo.precio), fontWeight = FontWeight.Bold, color = Color(0xFFC2185B))
            }
        },
        confirmButton = {
            TextButton(onClick = { onComprar(combo.id) }) {
                Text("Agregar al carrito")
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Cerrar")
            }
        }
    )
}

// Datos de ejemplo, cambia por tu fuente real
fun sampleCombos() = listOf(
    Combo(
        id = 1,
        nombre = "Combo Familiar",
        tipo = "Combo",
        tipoCombo = "Familiar",
        precio = 65.00,
        observaciones = "Incluye churrascos y dulces",
        churrascos = listOf("Churrasco Familiar", "Churrasco Especial"),
        dulces = listOf("Caja de Canillitas"),
        urlImagen = R.drawable.combo_familiar // Pon tu drawable
    ),
    Combo(
        id = 2,
        nombre = "Combo Especial",
        tipo = "Combo",
        tipoCombo = "Especial",
        precio = 75.00,
        observaciones = null,
        churrascos = listOf("Churrasco Especial"),
        dulces = listOf(),
        urlImagen = R.drawable.combo_especial // Pon tu drawable
    )
)