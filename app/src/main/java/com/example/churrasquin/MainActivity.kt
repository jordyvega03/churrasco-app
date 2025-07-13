package com.example.churrasquin


import android.content.Intent
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

class MainActivity : ComponentActivity() {

    data class ModuleLinkData(
        val route: String,
        val title: String,
        val emoji: String,
        val description: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen(
                modules = listOf(
                    ModuleLinkData("churrascos", "Churrascos", "🥩", ""),
                    ModuleLinkData("dulces", "Dulces", "🍬", ""),
                    ModuleLinkData("combos", "Combos", "🧺", ""),
                    ModuleLinkData("administracion", "Admin", "⚙️", "")
                ),
                onModuleClick = { route ->
                    when(route) {
                        "churrascos" -> {
                            val intent = Intent(this, ChurrascosActivity::class.java)
                            startActivity(intent)
                        }
                        "dulces" ->{
                            val intent = Intent(this, DulcesActivity::class.java)
                            startActivity(intent)
                        }
                        "combos" ->{
                            val intent = Intent(this, CombosActivity::class.java)
                            startActivity(intent)
                        }
                        "administracion" -> {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                        else -> {
                            // Nada o un Toast
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen(modules: List<MainActivity.ModuleLinkData>, onModuleClick: (String) -> Unit) {
    // Fondo con gradiente rosa a amarillo (similar tailwind "bg-gradient-to-br from-pink-100 to-yellow-100")
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFED7E2), Color(0xFFFFF4D9)) // pink-100 to yellow-100 approx
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()      // Cambio importante: toma toda la altura disponible
                .shadow(12.dp, RoundedCornerShape(24.dp)),
            color = Color.White.copy(alpha = 0.95f),
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxSize(),     // Cambio: que la columna ocupe todo el espacio
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Imagen logo
                Image(
                    painter = painterResource(id = R.drawable.churrasco),
                    contentDescription = "Logo tienda churrascos y dulces",
                    modifier = Modifier
                        .size(112.dp)
                        .padding(bottom = 32.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "Bienvenido al Sistema de Gestión\nChurrascos & Dulces Típicos",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1F2937), // gray-900
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp),
                    lineHeight = 36.sp
                )

                Text(
                    text = "Administra tus productos, combos, ventas y recibe recomendaciones inteligentes para hacer crecer tu negocio.",
                    fontSize = 16.sp,
                    color = Color(0xFF4B5563), // gray-600
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp),
                    maxLines = 3
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),       // Esto permite que la grid crezca y ocupe espacio disponible y pueda scrollear
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = true  // Permitimos scroll para que no se corte contenido
                ) {
                    items(modules) { module ->
                        ModuleLink(
                            title = module.title,
                            emoji = module.emoji,
                            description = module.description,
                            onClick = { onModuleClick(module.route) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ModuleLink(
    title: String,
    emoji: String,
    description: String,
    onClick: () -> Unit
) {
    // Gradiente blanco a rosa muy suave
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(Color.White, Color(0xFFFFF1F6)) // white to pink-50 approx
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 140.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        contentColor = Color.Unspecified,
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .background(backgroundGradient, shape = RoundedCornerShape(16.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = emoji, fontSize = 40.sp)
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color(0xFF374151) // gray-800
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280), // gray-500
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}