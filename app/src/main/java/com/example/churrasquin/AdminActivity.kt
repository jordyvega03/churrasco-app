package com.example.churrasquin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import android.net.Uri
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter


class AdminActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdminScreen(onLogout = {
                finish() // cerrar activity o volver a login
            })
        }
    }
}

data class MenuItem(val label: String, val icon: @Composable () -> Unit, val route: String)

@Composable
fun AdminScreen(onLogout: () -> Unit) {
    var collapsed by remember { mutableStateOf(false) }
    var selectedRoute by remember { mutableStateOf("/administracion/inicio") }
    val context = LocalContext.current

    val menuItems = listOf(
        MenuItem("Inicio", { Icon(Icons.Default.Home, contentDescription = "Inicio") }, "/administracion/inicio"),
//        MenuItem("Dashboard", { Icon(Icons.Default.AccountBox, contentDescription = "Dashboard") }, "/administracion/dashboard"),
        MenuItem("Sucursal", { Icon(Icons.Default.Build, contentDescription = "Sucursal") }, "/administracion/sucursal"),
        MenuItem("Productos", { Icon(Icons.Default.ShoppingCart, contentDescription = "Productos") }, "/administracion/productos"),
        MenuItem("Inventario", { Icon(Icons.Default.List, contentDescription = "Inventario") }, "/administracion/inventario")
    )

    val sidebarWidth by animateDpAsState(if (collapsed) 56.dp else 240.dp)

    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar
        Column(
            modifier = Modifier
                .width(sidebarWidth)
                .fillMaxHeight()
                .background(Color.White)
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Header y hamburguesa
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!collapsed) {
                        Text(
                            text = "Admin Panel",
                            fontSize = 20.sp,
                            color = Color(0xFFD81B60),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = { collapsed = !collapsed }) {
                        Icon(Icons.Default.Menu, contentDescription = "Toggle menu", tint = Color(0xFFD81B60))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Menu items
                menuItems.forEach { item ->
                    val isActive = selectedRoute == item.route
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedRoute = item.route
                            }
                            .background(if (isActive) Color(0xFFFCE4EC) else Color.Transparent)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        item.icon()
                        if (!collapsed) {
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = item.label,
                                color = if (isActive) Color(0xFFD81B60) else Color.Black,
                                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Logout button al fondo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogout() }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.Red)
                if (!collapsed) {
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Cerrar sesión",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Área de contenido principal (para rutas distintas a dashboard)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(24.dp)
        ) {
            when (selectedRoute) {
                "/administracion/inicio" -> InicioContent()
                "/administracion/sucursal" -> SucursalContent()
                "/administracion/productos" -> ProductosContent()
                "/administracion/inventario" -> InventarioContent()
                else -> Text("Selecciona una opción del menú o ve a Dashboard.")
            }

        }
    }
}



@Composable
fun InicioContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Tarjetas resumen
        StatsGrid(
            stats = listOf(
                Triple("Ventas Diarias", "Q1,200", "💰"),
                Triple("Ventas Mensuales", "Q35,000", "📅"),
                Triple("Ganancias", "Q12,500", "📈"),
                Triple("Desperdicios", "120 kg", "🗑️")
            )
        )

        // Gráficas
        ChartsGrid()
    }
}

@Composable
fun StatsGrid(stats: List<Triple<String, String, String>>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(stats) { (title, value, icon) ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 4.dp,
                shadowElevation = 8.dp,
                shape = MaterialTheme.shapes.medium,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = icon, fontSize = 36.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = title, fontSize = 14.sp, color = Color.Gray)
                        Text(
                            text = value,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEC4899)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ChartsGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ChartLine(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            entries1 = listOf(120f, 190f, 170f, 220f, 280f, 300f, 250f),
            entries2 = listOf(150f, 180f, 160f, 200f, 260f, 280f, 240f),
            labels = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"),
            title = "Ventas Diarias y Mensuales"
        )

        ChartBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            labels = listOf("Churrasco Familiar", "Churrasco Especial", "Canillitas", "Caja Dulces"),
            values = listOf(120f, 90f, 140f, 80f),
            title = "Platos Más Vendidos"
        )

//        ChartBar(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp),
//            labels = listOf("Canillitas", "Caja Dulces", "Galletas", "Dulces Tradicionales"),
//            values = listOf(100f, 110f, 90f, 70f),
//            title = "Dulces Más Populares"
//        )
//
//        ChartBarHorizontal(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp),
//            labels = listOf("Frijoles + Tortillas", "Chile de Árbol + Cebollín", "Frijoles + Chirmol", "Tortillas + Cebollín"),
//            values = listOf(50f, 40f, 30f, 20f),
//            title = "Combinaciones de Guarniciones Frecuentes"
//        )
//
//        ChartPie(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp),
//            labels = listOf("Churrascos", "Dulces", "Combos"),
//            values = listOf(5000f, 3000f, 4500f),
//            colors = listOf(Color(0xFFEC4899), Color(0xFFF43F5E), Color(0xFFFBB6CE)),
//            title = "Ganancias por Categoría"
//        )
//
//        ChartBar(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp),
//            labels = listOf("Churrascos", "Dulces", "Combos"),
//            values = listOf(50f, 30f, 40f),
//            title = "Desperdicios y Mermas (kg)"
//        )
    }
}

//// Implementaciones de las gráficas usando MPAndroidChart, ya las tienes pero las dejo aquí para referencia
//
@Composable
fun ChartLine(
    modifier: Modifier = Modifier,
    entries1: List<Float>,
    entries2: List<Float>,
    labels: List<String>,
    title: String
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            LineChart(context).apply {
                description.text = title
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)

                val set1 = LineDataSet(
                    entries1.mapIndexed { index, value -> Entry(index.toFloat(), value) },
                    "Ventas Diarias"
                ).apply {
                    color = ColorTemplate.rgb("#EC4899")
                    setDrawFilled(true)
                    fillAlpha = 50
                    fillColor = ColorTemplate.rgb("#EC4899")
                    lineWidth = 2f
                    circleRadius = 4f
                }

                val set2 = LineDataSet(
                    entries2.mapIndexed { index, value -> Entry(index.toFloat(), value) },
                    "Ventas Mensuales (promedio diario)"
                ).apply {
                    color = ColorTemplate.rgb("#F43F5E")
                    setDrawFilled(true)
                    fillAlpha = 50
                    fillColor = ColorTemplate.rgb("#F43F5E")
                    lineWidth = 2f
                    circleRadius = 4f
                }

                data = LineData(set1, set2)

                xAxis.granularity = 1f
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels.getOrNull(value.toInt()) ?: value.toString()
                    }
                }

                invalidate()
            }
        }
    )
}

@Composable
fun ChartBar(
    modifier: Modifier = Modifier,
    labels: List<String>,
    values: List<Float>,
    title: String
) {
    val context = LocalContext.current
    AndroidView(
        modifier = modifier,
        factory = {
            BarChart(context).apply {
                description.text = title
                setTouchEnabled(false)
                setDrawBarShadow(false)

                val entries = values.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
                val dataSet = BarDataSet(entries, "Ventas").apply {
                    color = ColorTemplate.rgb("#F43F5E")
                    valueTextColor = android.graphics.Color.BLACK
                }

                data = BarData(dataSet)

                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labels.getOrNull(value.toInt()) ?: value.toString()
                    }
                }

                invalidate()
            }
        }
    )
}
//
//@Composable
//fun ChartBarHorizontal(
//    modifier: Modifier = Modifier,
//    labels: List<String>,
//    values: List<Float>,
//    title: String
//) {
//    val context = LocalContext.current
//    AndroidView(
//        modifier = modifier,
//        factory = {
//            BarChart(context).apply {
//                description.text = title
//                setTouchEnabled(false)
//                setDrawBarShadow(false)
//
//                axisLeft.isEnabled = false
//                axisRight.isEnabled = false
//                xAxis.isEnabled = true
//                xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
//                xAxis.granularity = 1f
//                xAxis.setDrawGridLines(false)
//                axisLeft.setDrawGridLines(false)
//                axisRight.setDrawGridLines(false)
//
//                xAxis.valueFormatter = object : ValueFormatter() {
//                    override fun getFormattedValue(value: Float): String {
//                        return labels.getOrNull(value.toInt()) ?: value.toString()
//                    }
//                }
//
//                xAxis.labelRotationAngle = -45f
//
//                val entries = values.mapIndexed { index, value -> BarEntry(value, index.toFloat()) }
//                val dataSet = BarDataSet(entries, "Frecuencia").apply {
//                    color = ColorTemplate.rgb("#F43F5E")
//                    valueTextColor = android.graphics.Color.BLACK
//                }
//
//                data = BarData(dataSet)
//
//                invalidate()
//            }
//        }
//    )
//}
//
//@Composable
//fun ChartPie(
//    modifier: Modifier = Modifier,
//    labels: List<String>,
//    values: List<Float>,
//    colors: List<Color>,
//    title: String
//) {
//    val context = LocalContext.current
//    AndroidView(
//        modifier = modifier,
//        factory = {
//            PieChart(context).apply {
//                description.text = title
//                isDrawHoleEnabled = true
//                holeRadius = 58f
//                transparentCircleRadius = 61f
//                setEntryLabelColor(android.graphics.Color.BLACK)
//
//                val entries = values.mapIndexed { index, value -> PieEntry(value, labels.getOrElse(index) { "" }) }
//                val dataSet = PieDataSet(entries, "").apply {
//                    setColors(colors.map { it.toArgb() }.toIntArray(), context)
//                    valueTextColor = android.graphics.Color.BLACK
//                }
//
//                data = PieData(dataSet)
//                invalidate()
//            }
//        }
//    )
//}



data class Sucursal(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val telefono: String,
    val correo: String
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SucursalContent() {
    var sucursales by remember { mutableStateOf(sampleSucursales()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    var modalOpen by remember { mutableStateOf(false) }
    var editingSucursal by remember { mutableStateOf<Sucursal?>(null) }

    // Campos de formulario
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    fun openModal(sucursal: Sucursal?) {
        if (sucursal != null) {
            editingSucursal = sucursal
            nombre = sucursal.nombre
            direccion = sucursal.direccion
            telefono = sucursal.telefono
            correo = sucursal.correo
        } else {
            editingSucursal = null
            nombre = ""
            direccion = ""
            telefono = ""
            correo = ""
        }
        modalOpen = true
        error = null
    }

    fun closeModal() {
        modalOpen = false
    }

    fun saveSucursal() {
        if (nombre.isBlank() || direccion.isBlank() || telefono.isBlank() || correo.isBlank()) {
            error = "Completa todos los campos obligatorios"
            return
        }

        val newSucursal = Sucursal(
            id = editingSucursal?.id ?: (sucursales.maxOfOrNull { it.id } ?: 0) + 1,
            nombre = nombre,
            direccion = direccion,
            telefono = telefono,
            correo = correo
        )

        sucursales = if (editingSucursal != null) {
            sucursales.map { if (it.id == editingSucursal!!.id) newSucursal else it }
        } else {
            sucursales + newSucursal
        }

        closeModal()
    }

    fun deleteSucursal(sucursal: Sucursal) {
        sucursales = sucursales.filter { it.id != sucursal.id }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Administrar Sucursales", fontWeight = FontWeight.Bold, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { openModal(null) }, modifier = Modifier.align(Alignment.End)) {
            Text("Agregar Sucursal")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            Text("Cargando...", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (error != null) {
            Text("Error: $error", color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (sucursales.isEmpty()) {
            Text("No hay sucursales registradas.", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn {
                items(sucursales) { sucursal ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                                Text(sucursal.nombre, fontWeight = FontWeight.Bold)
                                Text("Dirección: ${sucursal.direccion}")
                                Text("Teléfono: ${sucursal.telefono}")
                                Text("Correo: ${sucursal.correo}")
                            }
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(
                                    "Editar",
                                    color = Color(0xFFD81B60),
                                    modifier = Modifier
                                        .clickable { openModal(sucursal) }
                                        .padding(8.dp)
                                )
                                Text(
                                    "Eliminar",
                                    color = Color.Red,
                                    modifier = Modifier
                                        .clickable { deleteSucursal(sucursal) }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (modalOpen) {
        AlertDialog(
            onDismissRequest = { closeModal() },
            title = {
                Text(
                    if (editingSucursal != null) "Editar Sucursal" else "Nueva Sucursal",
                    color = Color(0xFFD81B60)
                )
            },
            text = {
                Column {
                    TextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre*") },
                        singleLine = true
                    )
                    TextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección*") },
                        singleLine = true
                    )
                    TextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono*") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                    TextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo*") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { saveSucursal() }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { closeModal() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


private fun sampleSucursales() = listOf(
    Sucursal(1, "Sucursal Central", "Av. Principal 123", "555-1234", "central@sucursales.com"),
    Sucursal(2, "Sucursal Norte", "Calle Norte 456", "555-5678", "norte@sucursales.com"),
    Sucursal(3, "Sucursal Sur", "Calle Sur 789", "555-9012", "sur@sucursales.com"),
)


// Modelos de datos simplificados
sealed class Producto(open val id: Int, open val nombre: String, open val precio: Double, open val tipo: String) {
    data class Churrasco(
        override val id: Int,
        override val nombre: String,
        override val precio: Double,
        override val tipo: String,
        val tipoCarne: String,
        val termino: String,
        val tamaño: String,
        val porciones: Int,
        val urlImagen: String? = null
    ) : Producto(id, nombre, precio, tipo)

    data class Dulce(
        override val id: Int,
        override val nombre: String,
        override val precio: Double,
        override val tipo: String,
        val urlImagen: String? = null
    ) : Producto(id, nombre, precio, tipo)

    data class Combo(
        override val id: Int,
        override val nombre: String,
        override val precio: Double,
        override val tipo: String,
        val urlImagen: String? = null
    ) : Producto(id, nombre, precio, tipo)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosContent() {
    var tipoProducto by remember { mutableStateOf("churrascos") }
    var productos by remember { mutableStateOf(sampleProductos()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    var modalOpen by remember { mutableStateOf(false) }
    var editingProducto by remember { mutableStateOf<Producto?>(null) }

    // Campos de formulario
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(tipoProducto) }
    var tipoCarne by remember { mutableStateOf("") }
    var termino by remember { mutableStateOf("") }
    var tamaño by remember { mutableStateOf("") }
    var porciones by remember { mutableStateOf("") }
    var urlImagen by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        urlImagen = uri?.toString()
    }

    fun openModal(producto: Producto?) {
        if (producto != null) {
            editingProducto = producto
            nombre = producto.nombre
            precio = producto.precio.toString()
            tipo = producto.tipo
            when (producto) {
                is Producto.Churrasco -> {
                    tipoCarne = producto.tipoCarne
                    termino = producto.termino
                    tamaño = producto.tamaño
                    porciones = producto.porciones.toString()
                    urlImagen = producto.urlImagen
                }
                else -> {
                    tipoCarne = ""
                    termino = ""
                    tamaño = ""
                    porciones = ""
                    urlImagen = (producto as? Producto.Dulce)?.urlImagen ?: (producto as? Producto.Combo)?.urlImagen
                }
            }
        } else {
            editingProducto = null
            nombre = ""
            precio = ""
            tipo = tipoProducto
            tipoCarne = ""
            termino = ""
            tamaño = ""
            porciones = ""
            urlImagen = null
        }
        modalOpen = true
        error = null
    }

    fun closeModal() {
        modalOpen = false
    }

    fun saveProducto() {
        if (nombre.isBlank() || precio.isBlank() || tipo.isBlank()) {
            error = "Completa todos los campos obligatorios"
            return
        }
        val precioVal = precio.toDoubleOrNull()
        if (precioVal == null) {
            error = "Precio debe ser número válido"
            return
        }

        val newProducto: Producto = when (tipo) {
            "churrascos" -> Producto.Churrasco(
                id = editingProducto?.id ?: (productos.maxOfOrNull { it.id } ?: 0) + 1,
                nombre = nombre,
                precio = precioVal,
                tipo = tipo,
                tipoCarne = tipoCarne,
                termino = termino,
                tamaño = tamaño,
                porciones = porciones.toIntOrNull() ?: 0,
                urlImagen = urlImagen
            )
            "dulces" -> Producto.Dulce(
                id = editingProducto?.id ?: (productos.maxOfOrNull { it.id } ?: 0) + 1,
                nombre = nombre,
                precio = precioVal,
                tipo = tipo,
                urlImagen = urlImagen
            )
            "combos" -> Producto.Combo(
                id = editingProducto?.id ?: (productos.maxOfOrNull { it.id } ?: 0) + 1,
                nombre = nombre,
                precio = precioVal,
                tipo = tipo,
                urlImagen = urlImagen
            )
            else -> throw IllegalArgumentException("Tipo no soportado")
        }

        productos = if (editingProducto != null) {
            productos.map {
                if (it.id == editingProducto!!.id) newProducto else it
            }
        } else {
            productos + newProducto
        }

        closeModal()
    }

    fun deleteProducto(producto: Producto) {
        productos = productos.filter { it.id != producto.id }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tipo producto: ", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            listOf("churrascos", "dulces", "combos").forEach { tipo ->
                FilterChip(
                    selected = tipo == tipoProducto,
                    onClick = {
                        tipoProducto = tipo
                        // Opcional: limpiar modal si está abierto
                        if (modalOpen) closeModal()
                    },
                    label = { Text(tipo.replaceFirstChar { it.uppercase() }) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        if (loading) {
            Text("Cargando...", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (error != null) {
            Text("Error: $error", color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn {
                items(productos.filter { it.tipo == tipoProducto }) { producto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                                Text(producto.nombre, fontWeight = FontWeight.Bold)
                                Text("Precio: Q${producto.precio}")
                                if (tipoProducto == "churrascos" && producto is Producto.Churrasco) {
                                    Text("Tipo Carne: ${producto.tipoCarne}")
                                    Text("Término: ${producto.termino}")
                                    Text("Tamaño: ${producto.tamaño}")
                                    Text("Porciones: ${producto.porciones}")
                                }
                            }
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(
                                    "Editar",
                                    color = Color(0xFFD81B60),
                                    modifier = Modifier
                                        .clickable { openModal(producto) }
                                        .padding(8.dp)
                                )
                                Text(
                                    "Eliminar",
                                    color = Color.Red,
                                    modifier = Modifier
                                        .clickable { deleteProducto(producto) }
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        if (modalOpen) {
            AlertDialog(
                onDismissRequest = { closeModal() },
                title = {
                    Text(
                        if (editingProducto != null) "Editar Producto" else "Nuevo Producto",
                        color = Color(0xFFD81B60)
                    )
                },
                text = {
                    Column {
                        TextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre*") },
                            singleLine = true
                        )
                        TextField(
                            value = precio,
                            onValueChange = { precio = it },
                            label = { Text("Precio*") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        TextField(
                            value = tipo,
                            onValueChange = { tipo = it },
                            label = { Text("Tipo*") },
                            singleLine = true
                        )
                        if (tipoProducto == "churrascos") {
                            TextField(
                                value = tipoCarne,
                                onValueChange = { tipoCarne = it },
                                label = { Text("Tipo Carne*") },
                                singleLine = true
                            )
                            TextField(
                                value = termino,
                                onValueChange = { termino = it },
                                label = { Text("Término*") },
                                singleLine = true
                            )
                            TextField(
                                value = tamaño,
                                onValueChange = { tamaño = it },
                                label = { Text("Tamaño*") },
                                singleLine = true
                            )
                            TextField(
                                value = porciones,
                                onValueChange = { porciones = it },
                                label = { Text("Porciones*") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Imagen y botón para seleccionar
                        Text("Imagen:")
                        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                            Text("Seleccionar imagen")
                        }
                        urlImagen?.let {
                            Image(
                                painter = rememberAsyncImagePainter(it),
                                contentDescription = "Imagen seleccionada",
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(top = 8.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { saveProducto() }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    Button(onClick = { closeModal() }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

// Datos ejemplo
private fun sampleProductos() = listOf(
    Producto.Churrasco(
        id = 1,
        nombre = "Churrasco Familiar",
        precio = 150.0,
        tipo = "churrascos",
        tipoCarne = "Res",
        termino = "Tres Cuartos",
        tamaño = "Grande",
        porciones = 5,
        urlImagen = "https://via.placeholder.com/64"
    ),
    Producto.Dulce(
        id = 2,
        nombre = "Caja Dulces",
        precio = 90.0,
        tipo = "dulces",
        urlImagen = "https://via.placeholder.com/64"
    ),
    Producto.Combo(
        id = 3,
        nombre = "Combo Especial",
        precio = 200.0,
        tipo = "combos",
        urlImagen = "https://via.placeholder.com/64"
    )
)

data class InventarioItem(
    val id: String,
    val nombreIngrediente: String,
    val tipoIngrediente: String,
    val cantidad: Int,
    val unidad: String
)

@Composable
fun InventarioContent() {
    var items by remember { mutableStateOf(sampleItems.toMutableList()) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    var modalOpen by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<InventarioItem?>(null) }

    var alertMessage by remember { mutableStateOf<String?>(null) }
    var confirmOpen by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<InventarioItem?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Inventario", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

        Button(onClick = {
            editingItem = null
            modalOpen = true
        }, modifier = Modifier.align(Alignment.End)) {
            Text("Agregar Ingrediente")
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (loading) {
            Text("Cargando...", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (error != null) {
            Text("Error: $error", color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (items.isEmpty()) {
            Text("No hay ingredientes registrados.", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items) { item ->
                    InventarioRow(
                        item = item,
                        onEdit = {
                            editingItem = it
                            modalOpen = true
                        },
                        onDelete = {
                            itemToDelete = it
                            confirmOpen = true
                        }
                    )
                }
            }
        }
    }

    // Modal de agregar/editar
    if (modalOpen) {
        InventarioDialog(
            item = editingItem,
            onDismiss = { modalOpen = false },
            onSave = { newItem ->
                loading = true
                coroutineScope.launch {
                    delay(500) // simular llamada API
                    loading = false
                    if (editingItem == null) {
                        // agregar nuevo item
                        items = (items + newItem.copy(id = (items.size + 1).toString())).toMutableList()
                        alertMessage = "Ingrediente agregado correctamente"
                    } else {
                        // editar item
                        items = items.map { if (it.id == newItem.id) newItem else it }.toMutableList()
                        alertMessage = "Ingrediente actualizado correctamente"
                    }
                    modalOpen = false
                }
            }
        )
    }

    // Confirm dialog para eliminar
    if (confirmOpen && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { confirmOpen = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Seguro que deseas eliminar el ingrediente ${itemToDelete?.nombreIngrediente}?") },
            confirmButton = {
                TextButton(onClick = {
                    loading = true
                    confirmOpen = false
                    coroutineScope.launch {
                        delay(500) // simular llamada API
                        loading = false
                        items = items.filter { it.id != itemToDelete?.id }.toMutableList()
                        alertMessage = "Ingrediente eliminado correctamente"
                        itemToDelete = null
                    }
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmOpen = false }) { Text("Cancelar") }
            }
        )
    }

    // Alerta simple abajo derecha
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        alertMessage?.let {
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                action = {
                    TextButton(onClick = { alertMessage = null }) {
                        Text("Cerrar", color = Color.White)
                    }
                }
            ) { Text(it) }
        }
    }

}

@Composable
fun InventarioRow(item: InventarioItem, onEdit: (InventarioItem) -> Unit, onDelete: (InventarioItem) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFFCE4EC))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(item.nombreIngrediente, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Tipo: ${item.tipoIngrediente}")
            Text("Cantidad: ${item.cantidad} ${item.unidad}")
        }
        Row {
            IconButton(onClick = { onEdit(item) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")
            }
            IconButton(onClick = { onDelete(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}

@Composable
fun InventarioDialog(item: InventarioItem?, onDismiss: () -> Unit, onSave: (InventarioItem) -> Unit) {
    var nombre by remember { mutableStateOf(item?.nombreIngrediente ?: "") }
    var tipo by remember { mutableStateOf(item?.tipoIngrediente ?: "") }
    var cantidad by remember { mutableStateOf(item?.cantidad?.toString() ?: "0") }
    var unidad by remember { mutableStateOf(item?.unidad ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item == null) "Agregar Ingrediente" else "Editar Ingrediente") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Ingrediente") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = tipo,
                    onValueChange = { tipo = it },
                    label = { Text("Tipo Ingrediente") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = cantidad,
                    onValueChange = { cantidad = it.filter { c -> c.isDigit() } },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = unidad,
                    onValueChange = { unidad = it },
                    label = { Text("Unidad") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val cant = cantidad.toIntOrNull() ?: 0
                    if (nombre.isBlank() || tipo.isBlank() || cant <= 0 || unidad.isBlank()) {
                        // Aquí puedes mostrar error o alertar
                        return@TextButton
                    }
                    onSave(
                        InventarioItem(
                            id = item?.id ?: "",
                            nombreIngrediente = nombre,
                            tipoIngrediente = tipo,
                            cantidad = cant,
                            unidad = unidad
                        )
                    )
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private val sampleItems = listOf(
    InventarioItem("1", "Tomate", "Vegetal", 50, "Kg"),
    InventarioItem("2", "Carne de Res", "Carne", 30, "Kg"),
    InventarioItem("3", "Cebolla", "Vegetal", 40, "Kg"),
)
