package pe.com.grupomoran.designsystem.components.organisms

import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.io.File
import androidx.compose.ui.graphics.Color
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GMMainTopAppBar(
    title: String,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    contentColor: Color? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val finalContainerColor = containerColor ?: MaterialTheme.colorScheme.primary
    val finalContentColor = contentColor ?: MaterialTheme.colorScheme.onPrimary

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Abrir navegación"
                )
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = finalContainerColor,
            titleContentColor = finalContentColor,
            navigationIconContentColor = finalContentColor,
            actionIconContentColor = finalContentColor
        ),
        scrollBehavior = scrollBehavior,
        modifier = modifier
    )
}

@Composable
fun GMActionTopAppBar () {

}

@Composable
fun GMSearchTopAppBar () {

}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Menú Lateral Abierto - Claro")
@Preview(showBackground = true, name = "Menú Lateral Abierto - Oscuro", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewGMDashBoardDrawer() {
    // 1. Simulamos la lista que enviaría el servidor
    val mockOptions = listOf(
        GMMenuOption(id = "1", label = "Inicio de Ruta", iconName = "inicio.png"),
        GMMenuOption(id = "2", label = "Pedidos Pendientes", iconName = "delivery.png"),
        GMMenuOption(id = "3", label = "Cerrar Sesión", iconName = "exit.png")
    )

    GMDesignSystemTheme { // Asegúrate de que use el tema de tu librería
        // 2. Forzamos el estado 'Open' para que la Preview siempre muestre el menú
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "GRUPO MORÁN",
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    HorizontalDivider()

                    // 3. Iteración dinámica de las opciones
                    mockOptions.forEach { option ->
                        NavigationDrawerItem(
                            label = { Text(option.label) },
                            selected = option.label == "Inicio de Ruta",
                            onClick = { },
                            icon = {
                                // Cargamos el pintor. En Preview verás el icono de Menú/Help
                                // porque los archivos .png no existen en la PC
                                val iconPainter = rememberLocalIconPainter(option.iconName)
                                Icon(
                                    painter = iconPainter,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Unspecified // Mantiene colores del PNG
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        ) {
            // 4. El Scaffold de fondo para dar contexto visual
            Scaffold(
                topBar = {
                    GMMainTopAppBar(
                        title = "Vista Previa Librería",
                        onMenuClick = { }
                    )
                }
            ) { padding ->
                Box(
                    modifier = Modifier.padding(padding).fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("El contenido queda detrás del menú")
                }
            }
        }
    }
}

@Composable
fun rememberLocalIconPainter(fileName: String, context: Context = LocalContext.current): Painter {
    return remember(fileName) {
        val iconFile = File(context.filesDir, "icons/$fileName")
        if (iconFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(iconFile.absolutePath)
            if (bitmap != null) {
                BitmapPainter(bitmap.asImageBitmap())
            } else {
                BitmapPainter(ImageBitmap(1, 1))
            }
        } else {
            null
        }
    } ?: rememberVectorPainter(image = Icons.Default.Menu)
}

data class GMMenuOption(
    val id: String,
    val label: String,
    val iconName: String
)