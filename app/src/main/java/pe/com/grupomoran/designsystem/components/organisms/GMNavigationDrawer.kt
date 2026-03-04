package pe.com.grupomoran.designsystem.components.organisms

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.com.grupomoran.designsystem.R
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme
import java.io.File
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow

/**
 * Modelo para las opciones del menú lateral.
 * @param icon Puede ser un ImageVector, un Bitmap, un Painter o un String (Ruta absoluta).
 */
data class GMNavigationItem(
    val id: String,
    val label: String,
    val icon: Any, 
    val onClick: () -> Unit,
    val isSelected: Boolean = false
)

/**
 * Composable interno para resolver el icono dinámico.
 */
@Composable
fun rememberGMIconPainter(icon: Any): Painter {
    return when (icon) {
        is ImageVector -> rememberVectorPainter(icon)
        is Bitmap -> BitmapPainter(icon.asImageBitmap())
        is Int -> painterResource(id = icon)
        is Painter -> icon
        is String -> {
            remember(icon) {
                val iconFile = File(icon)
                if (iconFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(iconFile.absolutePath)
                    if (bitmap != null) {
                        BitmapPainter(bitmap.asImageBitmap())
                    } else {
                        null
                    }
                } else {
                    null
                }
            } ?: rememberVectorPainter(Icons.Default.HelpOutline)
        }
        else -> rememberVectorPainter(Icons.Default.HelpOutline)
    }
}

/**
 * Componente de Navegación Lateral (Modal Navigation Drawer) estandarizado.
 */
@Composable
fun GMModalNavigationDrawer(
    drawerState: DrawerState,
    items: List<GMNavigationItem>,
    modifier: Modifier = Modifier,
    gesturesEnabled: Boolean = false,
    headerContent: @Composable () -> Unit = { GMDefaultDrawerHeader() },
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,

        modifier = modifier,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 24.dp,
                            bottomEnd = 24.dp
                        )
                    ),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.onSurface
            ) {
                headerContent()
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { 
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = if (item.isSelected) FontWeight.Bold else FontWeight.Medium
                            ) 
                        },
                        selected = item.isSelected,
                        onClick = item.onClick,
                        icon = { 
                            Icon(
                                painter = rememberGMIconPainter(item.icon), 
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                // Mantenemos colores originales para Bitmaps e Imágenes externas
                                tint = if (item.icon is String || item.icon is Bitmap || item.icon is Int) Color.Unspecified else LocalContentColor.current
                            ) 
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        content = content
    )
}

/**
 * Header por defecto para el Drawer con el Logo del Grupo Morán.
 */
@Composable
fun GMDefaultDrawerHeader(
    title: String = "SISTEMA DE DISEÑO",
    subtitle: String = "Grupo Morán",
    userName: String = "Usuario Invitado", // Nuevo parámetro para el nombre del usuario
    icon: Any = R.drawable.logojmv2,
    backgroundPattern: Any = R.drawable.bg_pattern_geometric
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Aumentamos un poco el alto para que quepa todo cómodamente
            .background(Color.White)
    ) {
        // 1. PATRÓN DINÁMICO DE FONDO
        Image(
            painter = rememberGMIconPainter(backgroundPattern),
            contentDescription = null,
            modifier = Modifier
                .size(240.dp)
                .align(Alignment.TopEnd)
                .offset(x = 40.dp, y = (-40).dp)
                .alpha(0.15f),
            contentScale = ContentScale.Fit
        )

        // 2. CONTENIDO
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            // Logo
            Image(
                painter = rememberGMIconPainter(icon),
                contentDescription = null,
                modifier = Modifier.height(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // NOMBRE DEL USUARIO (Resaltado)
            Text(
                text = userName.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Título del Sistema
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )

            // Subtítulo / Empresa
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
