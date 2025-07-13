package com.example.churrasquin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.window.Dialog

class DulcesActivity : ComponentActivity() {

    data class Dulce(
        val id: Int,
        val nombre: String,
        val tipoDulce: String,
        val empaque: String,
        val cantidadEnCaja: Int,
        val precio: Double,
        val urlImagen: Int? = null // Drawable resource id
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aquí iría la carga real de datos con Retrofit/Flow etc.
            val dummyDulces = remember {
                listOf(
                    Dulce(1, "Dulce de Leche", "Tradicional", "Caja", 12, 25.0, R.drawable.dulce1),
                    Dulce(2, "Caramelo", "Dulce", "Bolsa", 24, 15.0, R.drawable.dulce2),
                    Dulce(3, "Chocolate", "Gourmet", "Caja", 6, 40.0, R.drawable.dulce3),
                )
            }

            DulcesScreen(
                dulces = dummyDulces,
                onBack = { finish() },
                onAgregarCarrito = { dulceId ->
                    // Aquí la acción para agregar al carrito (ejemplo: mostrar Toast o navegar)
                }
            )
        }
    }
}

@Composable
fun DulcesScreen(
    dulces: List<DulcesActivity.Dulce>,
    onBack: () -> Unit,
    onAgregarCarrito: (Int) -> Unit,
) {
    var modalDulce by remember { mutableStateOf<DulcesActivity.Dulce?>(null) }

    // Fondo con gradiente
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFED7E2), Color(0xFFFFF4D9))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
                Text("← Volver al Home")
            }

            Text(
                "Dulces",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            if (dulces.isEmpty()) {
                Text("No hay dulces disponibles.", modifier = Modifier.padding(top = 16.dp))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(dulces) { dulce ->
                        DulceCard(
                            dulce = dulce,
                            onClick = { modalDulce = dulce },
                            onAgregarCarrito = { onAgregarCarrito(dulce.id) }
                        )
                    }
                }
            }
        }

        modalDulce?.let { dulce ->
            DulceDetailDialog(
                dulce = dulce,
                onDismiss = { modalDulce = null },
                onAgregarCarrito = {
                    onAgregarCarrito(dulce.id)
                    modalDulce = null
                }
            )
        }
    }
}

@Composable
fun DulceCard(
    dulce: DulcesActivity.Dulce,
    onClick: () -> Unit,
    onAgregarCarrito: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                dulce.urlImagen?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = dulce.nombre,
                        modifier = Modifier
                            .height(100.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(dulce.nombre, fontWeight = FontWeight.Bold, color = Color(0xFFC2185B), modifier = Modifier.padding(vertical = 4.dp))
                Text("Tipo Dulce: ${dulce.tipoDulce}", fontWeight = FontWeight.SemiBold)
                Text("Empaque: ${dulce.empaque}")
                Text("Cantidad en Caja: ${dulce.cantidadEnCaja}")
                Text("Q ${String.format("%.2f", dulce.precio)}", fontWeight = FontWeight.Bold, color = Color(0xFFC2185B), modifier = Modifier.padding(top = 8.dp))
            }
            IconButton(
                onClick = onAgregarCarrito,
                modifier = Modifier.align(Alignment.TopEnd),
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Comprar ${dulce.nombre}",
                    tint = Color(0xFFD81B60)
                )
            }
        }
    }
}

@Composable
fun DulceDetailDialog(
    dulce: DulcesActivity.Dulce,
    onDismiss: () -> Unit,
    onAgregarCarrito: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                dulce.urlImagen?.let {
                    Image(
                        painter = painterResource(it),
                        contentDescription = dulce.nombre,
                        modifier = Modifier
                            .height(180.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(dulce.nombre, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFFC2185B), modifier = Modifier.padding(vertical = 8.dp))
                Text("Tipo Dulce: ${dulce.tipoDulce}", fontWeight = FontWeight.SemiBold)
                Text("Empaque: ${dulce.empaque}")
                Text("Cantidad en Caja: ${dulce.cantidadEnCaja}")
                Text("Q ${String.format("%.2f", dulce.precio)}", fontWeight = FontWeight.Bold, color = Color(0xFFC2185B), modifier = Modifier.padding(vertical = 8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onDismiss) {
                        Text("Cerrar")
                    }
                    Button(onClick = onAgregarCarrito) {
                        Text("Agregar al carrito")
                    }
                }
            }
        }
    }
}