package pe.com.grupomoran.designsystem.components.organisms

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

/**
 * Modelo para los elementos de navegación inferior.
 * Soporta tanto navegación por ruta como acciones programadas.
 */
data class GMBottomNavItem(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon,
    val route: String = "",
    val badgeCount: Int? = null,
    val onClick: (() -> Unit)? = null
)

/**
 * Barra de Navegación Inferior (Tab Bar) oficial del Grupo Moran.
 * Estilo unificado tipo Facebook/Instagram con estética Apple Modern.
 * 
 * @param dynamicItems Lista de botones dinámicos (Máximo 3).
 * @param selectedRoute Ruta activa para resaltar el Tab.
 * @param identificadorPantalla ID para el sistema de VisualAids.
 * @param onHelpClick Acción de ayuda (vm.showHelp(id)).
 * @param onItemClick Acción de navegación (navController.navigate(route)).
 */
@Composable
fun GMBottomNavigationBar(
    dynamicItems: List<GMBottomNavItem>,
    selectedRoute: String,
    identificadorPantalla: String,
    onHelpClick: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val clarifiedColor = Color.White.copy(alpha = 0.35f).compositeOver(containerColor)

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = clarifiedColor,
        shadowElevation = 16.dp,
        // Bordes superiores redondeados para el toque Apple
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .height(52.dp), // Altura compacta y profesional
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TABS DINÁMICOS (Navegación o Acciones)
            dynamicItems.take(3).forEach { item ->
                BottomNavTab(
                    item = item,
                    isSelected = selectedRoute.isNotEmpty() && selectedRoute == item.route,
                    onClick = { item.onClick?.invoke() ?: onItemClick(item.route) },
                    contentColor = contentColor
                )
            }

            // TAB DE AYUDA (Fijo)
            BottomNavTab(
                item = GMBottomNavItem(label = "Ayuda", icon = Icons.Default.HelpOutline),
                isSelected = false,
                onClick = { onHelpClick(identificadorPantalla) },
                contentColor = contentColor
            )
        }
    }
}


@Composable
private fun RowScope.BottomNavTab(
    item: GMBottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    contentColor: Color
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.15f else 1f, label = "scale")
    val color by animateColorAsState(targetValue = if (isSelected) contentColor else contentColor.copy(alpha = 0.6f), label = "color")

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .weight(1f) // Ahora sí funciona porque estamos en RowScope
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = contentColor),
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BadgedBox(
            badge = {
                if (item.badgeCount != null && item.badgeCount > 0) {
                    Badge(
                        containerColor = Color.Red,
                        contentColor = Color.White,
                        modifier = Modifier.size(12.dp)
                    ) {
                        Text(text = if (item.badgeCount > 9) "+" else item.badgeCount.toString(), fontSize = 7.sp)
                    }
                }
            }
        ) {
            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.icon,
                contentDescription = item.label,
                modifier = Modifier.size(22.dp).scale(scale), // Icono compacto
                tint = color
            )
        }
        if (item.label.isNotEmpty()) {
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = item.label,
                fontSize = 8.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = color,
                maxLines = 1
            )
        }
    }
}

/**
 * Vista previa del Tab Bar estilo Producción.
 */
@Preview(showBackground = true)
@Composable
fun GMTabBarPreview() {
    val items = listOf(
        GMBottomNavItem("Recorrido", Icons.Outlined.AltRoute, Icons.Filled.AltRoute, "recorrido"),
        GMBottomNavItem("Mapa", Icons.Outlined.Map, Icons.Filled.Map, "mapa", badgeCount = 5)
    )
    GMDesignSystemTheme {
        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            GMBottomNavigationBar(
                dynamicItems = items,
                selectedRoute = "recorrido",
                identificadorPantalla = "ID_PANTALLA",
                onHelpClick = {},
                onItemClick = {}
            )
        }
    }
}
