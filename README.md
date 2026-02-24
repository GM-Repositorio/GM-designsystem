# 📘 Sistema de Diseño - Grupo Moran

Librería de componentes de UI construida con **Jetpack Compose** y **Material3**, diseñada para estandarizar la experiencia de usuario en las aplicaciones de **Reparto** y **Ventas** de Grupo Moran.

## 🚀 Instalación (JitPack)

Añade el repositorio de JitPack a tu archivo `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Añade la dependencia a tu `build.gradle.kts` (app):

```kotlin
dependencies {
    implementation("com.github.GM-Repositorio:designsystem:1.0.4")
}
```

---

## 🎨 Configuración del Tema

Envuelve tu aplicación en el `GMDesignSystemTheme` para aplicar los colores corporativos y la tipografía estándar.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GMDesignSystemTheme {
                // Tu contenido aquí
            }
        }
    }
}
```

---

## 🏗️ Estructura de Componentes (Atomic Design)

### ⚛️ Átomos (Atoms)
- **`GMCircleIconButton`**: Botón circular estandarizado para acciones rápidas.
- **`GMFilterChip`**: Chip individual para filtros. Soporta iconos dinámicos (`ImageVector` o `Painter`).

### 🧩 Moléculas (Molecules)
- **`GMSearchInput`**: Campo de búsqueda con elevación, icono de limpieza y soporte para búsqueda desde el teclado (`ImeAction.Search`).
- **`GMFilterChipBar`**: Barra de desplazamiento horizontal para filtros dinámicos que provienen del servidor.

### 🏗️ Organismos (Organisms)
- **`GMSearchHeader`**: El componente principal de las pantallas de listado.
    - **Modo Búsqueda**: Soporta hasta 3 botones de acción rápida. Si se superan los 3, agrupa el resto automáticamente en un menú de desbordamiento (`MoreVert`).
    - **Modo Selección**: Cambia la UI para mostrar contador de selección y acciones masivas (Ej: Entregar en bloque).
- **`BaseLoginScreen`**: Estructura base para pantallas de inicio de sesión.
- **`Dialogs`**: Colección de diálogos preconfigurados:
    - `GMLoadingDialog`, `GMConfirmDialog`, `GMErrorDialog`, `GMInfoDialog`.

---

## 🛠️ Ejemplos de Uso

### Header con Búsqueda y Filtros Dinámicos
```kotlin
val dynamicFilters = listOf(
    GMFilterOption("1", "Pendientes", Icons.Default.Schedule, Color.Gray),
    GMFilterOption("2", "Entregados", Icons.Default.CheckCircle, Color(0xFF4CAF50))
)

Column {
    GMSearchHeader(
        searchQuery = text,
        onSearchQueryChange = { text = it },
        onSearch = { viewModel.doSearch(text) },
        isSelectionMode = isSelection,
        actions = listOf(
            GMSearchAction(Icons.Default.QrCodeScanner, "QR", Color.Gray, Color.White, { /* QR Logic */ })
        )
    )
    
    GMFilterChipBar(
        options = dynamicFilters,
        selectedId = selectedId,
        onOptionSelected = { selectedId = it.id }
    )
}
```

---

## 📋 Requisitos Técnicos
- **Java:** 11 (Bytecode compatible con aplicaciones legacy).
- **Compile SDK:** 35.
- **Min SDK:** 29.
- **Kotlin:** 1.9.24.
- **Compose Compiler:** 1.5.14.

---

## 🛠️ Mantenimiento
Para publicar una nueva versión:
1. Sube los cambios a `master`.
2. Crea un nuevo Tag/Release en GitHub.
3. Verifica el estado en [JitPack](https://jitpack.io).
