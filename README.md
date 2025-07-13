# Churrasquin Admin App

App Android desarrollada con Jetpack Compose para administrar productos, inventario y sucursales de una tienda de churrascos y dulces típicos. Incluye dashboard con gráficos y estadísticas.

---

## Características

- **Dashboard** con gráficos de ventas, productos más vendidos, ganancias y desperdicios usando MPAndroidChart integrado en Compose.
- **Gestión de Productos**: CRUD para churrascos, dulces y combos con imágenes y detalles.
- **Inventario**: CRUD para ingredientes con cantidades y unidades.
- **Sucursal**: Gestión simulada (por implementar o según necesidad).
- Navegación lateral con menú colapsable.
- Uso de `LazyColumn` y `LazyVerticalGrid` para listas y tarjetas.
- Integración con pickers para imágenes.
- Diseño moderno con Material3 y colores personalizados.

---

## Tecnologías y librerías

- Kotlin
- Jetpack Compose (Material3)
- MPAndroidChart para gráficos
- Coil para carga de imágenes
- Kotlin Coroutines para tareas asíncronas simuladas
- Activity Result API para selección de imágenes

---

## Estructura

- `AdminActivity`: actividad principal con menú lateral y navegación entre pantallas.
- `DashboardContent`: muestra estadísticas y gráficos.
- `ProductosContent`: CRUD de productos (churrascos, dulces, combos).
- `InventarioContent`: CRUD de ingredientes en inventario.
- `Sucursal`: pantalla simulada para administración de sucursales.
- Uso de `@Composable` para UI declarativa y modular.
- Manejo de estados con `remember` y `mutableStateOf`.

---

## Cómo ejecutar

1. Clona el repositorio o descarga el código fuente.
2. Abre el proyecto en Android Studio (mínimo Arctic Fox para Compose estable).
3. Asegúrate de tener el SDK y emulador configurados para Android 6.0+.
4. Sincroniza las dependencias Gradle.
5. Ejecuta la app en emulador o dispositivo físico.

---

## Mejoras futuras

- Integrar base de datos local o remota para persistencia (Room, Firebase).
- Conectar backend real para productos e inventario.
- Implementar autenticación de usuario.
- Añadir gestión real de sucursales.
- Mejorar diseño responsivo y animaciones.
- Soporte multi-idioma.

---

## Screenshots
![Home](<img width="1080" height="2400" alt="Image" src="https://github.com/user-attachments/assets/3eecd05d-d6bf-4065-875b-1fe8838a6851" />)  
![Dashboard](docs/screenshot_dashboard.png)  
![Productos](docs/screenshot_productos.png)  
![Inventario](docs/screenshot_inventario.png)  

---

## Autor

Tu Nombre - tu.email@ejemplo.com  
[GitHub](https://github.com/tuusuario)

---

## Licencia

Este proyecto está bajo la licencia MIT - ver archivo LICENSE para más detalles.
