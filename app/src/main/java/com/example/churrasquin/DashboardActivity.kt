package com.example.churrasquin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.graphics.toArgb

//class DashboardActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            DashboardScreen()
//        }
//    }
//}

//@Composable
//fun DashboardScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF9FAFB))
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        StatsGrid(
//            stats = listOf(
//                Triple("Ventas Diarias", "Q1,200", "💰"),
//                Triple("Ventas Mensuales", "Q35,000", "📅"),
//                Triple("Ganancias", "Q12,500", "📈"),
//                Triple("Desperdicios", "120 kg", "🗑️"),
//            )
//        )
//        ChartsGrid()
//    }
//}

//@Composable
//fun StatsGrid(stats: List<Triple<String, String, String>>) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        modifier = Modifier.height(180.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp),
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        items(stats) { (title, value, icon) ->
//            Surface(
//                modifier = Modifier.fillMaxWidth(),
//                tonalElevation = 4.dp,
//                shadowElevation = 8.dp,
//                shape = MaterialTheme.shapes.medium,
//                color = Color.White
//            ) {
//                Row(
//                    modifier = Modifier.padding(16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(text = icon, fontSize = 36.sp)
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Column {
//                        Text(text = title, fontSize = 14.sp, color = Color.Gray)
//                        Text(
//                            text = value,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFFEC4899)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ChartsGrid() {
//    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//        ChartLine(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp),
//            entries1 = listOf(120f, 190f, 170f, 220f, 280f, 300f, 250f),
//            entries2 = listOf(150f, 180f, 160f, 200f, 260f, 280f, 240f),
//            labels = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"),
//            title = "Ventas Diarias y Mensuales"
//        )
//
//        ChartBar(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp),
//            labels = listOf("Churrasco Familiar", "Churrasco Especial", "Canillitas", "Caja Dulces"),
//            values = listOf(120f, 90f, 140f, 80f),
//            title = "Platos Más Vendidos"
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
//    }
//}
//
//@Composable
//fun ChartLine(
//    modifier: Modifier = Modifier,
//    entries1: List<Float>,
//    entries2: List<Float>,
//    labels: List<String>,
//    title: String
//) {
//    val context = LocalContext.current
//
//    AndroidView(
//        modifier = modifier,
//        factory = {
//            LineChart(context).apply {
//                description = Description().apply { text = title }
//                setTouchEnabled(true)
//                isDragEnabled = true
//                setScaleEnabled(true)
//                setPinchZoom(true)
//
//                val values1 = entries1.mapIndexed { index, value ->
//                    Entry(index.toFloat(), value)
//                }
//                val values2 = entries2.mapIndexed { index, value ->
//                    Entry(index.toFloat(), value)
//                }
//
//                val set1 = LineDataSet(values1, "Ventas Diarias").apply {
//                    color = ColorTemplate.rgb("#EC4899")
//                    setDrawFilled(true)
//                    fillAlpha = 50
//                    fillColor = ColorTemplate.rgb("#EC4899")
//                    lineWidth = 2f
//                    circleRadius = 4f
//                }
//
//                val set2 = LineDataSet(values2, "Ventas Mensuales (promedio diario)").apply {
//                    color = ColorTemplate.rgb("#F43F5E")
//                    setDrawFilled(true)
//                    fillAlpha = 50
//                    fillColor = ColorTemplate.rgb("#F43F5E")
//                    lineWidth = 2f
//                    circleRadius = 4f
//                }
//
//                data = LineData(set1, set2)
//
//                xAxis.apply {
//                    granularity = 1f
//                    valueFormatter = object : ValueFormatter() {
//                        override fun getFormattedValue(value: Float): String {
//                            return labels.getOrNull(value.toInt()) ?: value.toString()
//                        }
//                    }
//                }
//
//                invalidate()
//            }
//        }
//    )
//}
//
//@Composable
//fun ChartBar(
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
//                description = Description().apply { text = title }
//                setTouchEnabled(false)
//                setDrawBarShadow(false)
//
//                val entries = values.mapIndexed { index, value ->
//                    BarEntry(index.toFloat(), value)
//                }
//                val dataSet = BarDataSet(entries, "Ventas").apply {
//                    color = ColorTemplate.rgb("#F43F5E")
//                    valueTextColor = android.graphics.Color.BLACK
//                }
//                data = BarData(dataSet)
//                xAxis.valueFormatter = object : ValueFormatter() {
//                    override fun getFormattedValue(value: Float): String {
//                        return labels.getOrNull(value.toInt()) ?: value.toString()
//                    }
//                }
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
//                description = Description().apply { text = title }
//                isDrawHoleEnabled = true
//                holeRadius = 58f
//                transparentCircleRadius = 61f
//                setEntryLabelColor(android.graphics.Color.BLACK)
//
//                val entries = values.mapIndexed { index, value ->
//                    PieEntry(value, labels.getOrElse(index) { "" })
//                }
//                val dataSet = PieDataSet(entries, "").apply {
//                    setColors(colors.map { it.toArgb() }.toIntArray(), context)
//                    valueTextColor = android.graphics.Color.BLACK
//                }
//                data = PieData(dataSet)
//                invalidate()
//            }
//        }
//    )
//}