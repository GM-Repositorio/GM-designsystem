package pe.com.grupomoran.designsystem.components.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.ripple.rememberRipple
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

/**
 * Vista previa del diseño Ultra-Compacto (48dp).
 */
@Preview(showBackground = true, backgroundColor = 0xFFFDFBFF)
@Composable
fun GMCompactBottomNavPreview() {
    val items = listOf(
        GMBottomNavItem("Recorrido", Icons.Outlined.AltRoute, Icons.Filled.AltRoute, "recorrido"),
        GMBottomNavItem("Mapa", Icons.Outlined.Map, Icons.Filled.Map, "mapa", badgeCount = 5)
    )
    GMDesignSystemTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.Transparent),
            contentAlignment = Alignment.BottomCenter
        ) {
            GMBottomNavigationBar(
                dynamicItems = items,
                selectedRoute = "recorrido",
                identificadorPantalla = "ID_PANTALLA_PRUEBA",
                onHelpClick = {},
                onItemClick = {}
            )
        }
    }
}

/**
 * Modelo para los elementos de navegación inferior.
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
 * Navegación inferior Dual-Dock Ultra-Compacta oficial del Grupo Moran.
 * 
 * @param dynamicItems Lista de botones (Máximo 3).
 * @param selectedRoute Ruta activa.
 * @param identificadorPantalla ID de la pantalla para el sistema de Ayuda.
 * @param onHelpClick Callback para disparar la ayuda.
 * @param onItemClick Callback general de navegación.
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
    val hasDynamicItems = dynamicItems.isNotEmpty()
    val clarifiedColor = Color.White.copy(alpha = 0.35f).compositeOver(containerColor)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp), // Reducción de margen inferior
        horizontalArrangement = if (hasDynamicItems) Arrangement.spacedBy(10.dp) else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // CÁPSULA 1: ACCIONES DINÁMICAS (Compacta: 48dp alto)
        AnimatedVisibility(
            visible = hasDynamicItems,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally(),
            modifier = Modifier.weight(1f)
        ) {
            CapsuleContainer(color = clarifiedColor, modifier = Modifier.height(48.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    dynamicItems.take(3).forEach { item ->
                        BottomNavTab(
                            item = item,
                            isSelected = selectedRoute.isNotEmpty() && selectedRoute == item.route,
                            onClick = { item.onClick?.invoke() ?: onItemClick(item.route) },
                            contentColor = contentColor
                        )
                    }
                }
            }
        }

        // CÁPSULA 2: AYUDA (Compacta: 48dp alto x 60dp ancho)
        CapsuleContainer(
            color = clarifiedColor,
            modifier = Modifier.width(60.dp).height(48.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                BottomNavTab(
                    item = GMBottomNavItem(label = "Ayuda", icon = Icons.Default.HelpOutline),
                    isSelected = false,
                    onClick = { onHelpClick(identificadorPantalla) },
                    contentColor = contentColor
                )
            }
        }
    }
}

@Composable
private fun CapsuleContainer(color: Color, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .shadow(
                6.dp,
                RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(color)
            .border(0.8.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center
    ) { content() }
}

@Composable
private fun BottomNavTab(item: GMBottomNavItem, isSelected: Boolean, onClick: () -> Unit, contentColor: Color) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.1f else 1f, label = "scale")
    val color by animateColorAsState(targetValue = if (isSelected) contentColor else contentColor.copy(alpha = 0.6f), label = "color")

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth()
            .clip(RoundedCornerShape(12.dp)) // Área del efecto de clic
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = contentColor),
                onClick = onClick
            )
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BadgedBox(
            badge = {
                if (item.badgeCount != null && item.badgeCount > 0) {
                    Badge(containerColor = Color.Red, contentColor = Color.White, modifier = Modifier.size(12.dp)) {
                        Text(text = if (item.badgeCount > 9) "+" else item.badgeCount.toString(), fontSize = 7.sp)
                    }
                }
            }
        ) {
            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.icon, 
                contentDescription = item.label, 
                modifier = Modifier.size(20.dp).scale(scale), 
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
