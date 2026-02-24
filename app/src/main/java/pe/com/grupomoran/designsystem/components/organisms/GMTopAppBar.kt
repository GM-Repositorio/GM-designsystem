package pe.com.grupomoran.designsystem.components.organisms

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.com.grupomoran.designsystem.components.atoms.GMCircleIconButton
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

/**
 * Modelo para definir acciones rápidas en la TopAppBar.
 */
data class GMTopAppBarAction(
    val icon: ImageVector,
    val description: String,
    val onClick: () -> Unit,
    val isVisible: Boolean = true,
    val containerColor: Color? = null,
    val contentColor: Color? = null
)

/**
 * Composable interno para renderizar el título con icono opcional.
 */
@Composable
private fun TopAppBarTitle(
    title: String,
    titleIcon: ImageVector? = null,
    titleBitmap: Bitmap? = null,
    isMedium: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (titleBitmap != null) {
            Image(
                bitmap = titleBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
        } else if (titleIcon != null) {
            Icon(
                imageVector = titleIcon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = title,
            style = if (isMedium) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
            fontWeight = if (isMedium) FontWeight.SemiBold else FontWeight.Bold,
            maxLines = 1
        )
    }
}

/**
 * TopAppBar principal con icono de menú (Hamburguesa).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GMMainTopAppBar(
    title: String,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleIcon: ImageVector? = null,
    titleBitmap: Bitmap? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val finalContainerColor = containerColor ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
    val finalContentColor = contentColor ?: MaterialTheme.colorScheme.onPrimary

    CenterAlignedTopAppBar(
        title = {
            TopAppBarTitle(title, titleIcon, titleBitmap)
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Abrir menú"
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

/**
 * TopAppBar para detalles o acciones secundarias con botón de retroceso.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GMActionTopAppBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleIcon: ImageVector? = null,
    titleBitmap: Bitmap? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val finalContainerColor = containerColor ?: MaterialTheme.colorScheme.surface
    val finalContentColor = contentColor ?: MaterialTheme.colorScheme.onSurface

    CenterAlignedTopAppBar(
        title = {
            TopAppBarTitle(title, titleIcon, titleBitmap, isMedium = true)
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar"
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
        modifier = modifier
    )
}

/**
 * TopAppBar dinámica que soporta hasta 3 acciones personalizables.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GMSearchTopAppBar(
    title: String,
    actions: List<GMTopAppBarAction>,
    onMenuClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    titleIcon: ImageVector? = null,
    titleBitmap: Bitmap? = null,
    containerColor: Color? = null,
    contentColor: Color? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val finalContainerColor = containerColor ?: MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
    val finalContentColor = contentColor ?: MaterialTheme.colorScheme.onPrimary

    CenterAlignedTopAppBar(
        title = {
            TopAppBarTitle(title, titleIcon, titleBitmap)
        },
        navigationIcon = {
            if (onMenuClick != null) {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Menu, contentDescription = "Menú")
                }
            }
        },
        actions = {
            actions.filter { it.isVisible }.take(3).forEach { action ->
                GMCircleIconButton(
                    icon = action.icon,
                    onClick = action.onClick,
                    containerColor = action.containerColor ?: Color.Transparent,
                    contentColor = action.contentColor ?: finalContentColor,
                    contentDescription = action.description,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }
        },
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

// --- PREVIEWS ---

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewGMTopAppBars() {
    GMDesignSystemTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Main TopAppBar (Con Icono)", style = MaterialTheme.typography.labelLarge)
            GMMainTopAppBar(
                title = "Reparto",
                titleIcon = Icons.Default.Notifications,
                onMenuClick = {}
            )

            Text("Action TopAppBar (Normal)", style = MaterialTheme.typography.labelLarge)
            GMActionTopAppBar(
                title = "Detalle Pedido",
                onBackClick = {}
            )
        }
    }
}
