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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.Map
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

/**
 * Modelo para los elementos de navegación inferior.
 * Soporta tanto navegación por ruta como acciones personalizadas.
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
 * Navegación inferior Dual-Dock oficial del Grupo Moran.
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
            .padding(bottom = 16.dp),
        horizontalArrangement = if (hasDynamicItems) Arrangement.spacedBy(14.dp) else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // CÁPSULA 1: ACCIONES DINÁMICAS
        AnimatedVisibility(
            visible = hasDynamicItems,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally(),
            modifier = Modifier.weight(1f)
        ) {
            CapsuleContainer(color = clarifiedColor, modifier = Modifier.height(64.dp)) {
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

        // CÁPSULA 2: AYUDA (Integrada con Identificador de Pantalla)
        CapsuleContainer(
            color = clarifiedColor,
            modifier = Modifier.width(72.dp).height(64.dp)
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
            .shadow(10.dp, RoundedCornerShape(32.dp), ambientColor = Color.Black.copy(alpha = 0.15f), spotColor = Color.Black.copy(alpha = 0.25f))
            .clip(RoundedCornerShape(32.dp))
            .background(color)
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(32.dp)),
        contentAlignment = Alignment.Center
    ) { content() }
}

@Composable
private fun BottomNavTab(item: GMBottomNavItem, isSelected: Boolean, onClick: () -> Unit, contentColor: Color) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.15f else 1f, label = "scale")
    val color by animateColorAsState(targetValue = if (isSelected) contentColor else contentColor.copy(alpha = 0.6f), label = "color")

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth()
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClick = onClick)
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BadgedBox(
            badge = {
                if (item.badgeCount != null && item.badgeCount > 0) {
                    Badge(containerColor = Color.Red, contentColor = Color.White, modifier = Modifier.size(14.dp)) {
                        Text(text = if (item.badgeCount > 9) "+" else item.badgeCount.toString(), fontSize = 8.sp)
                    }
                }
            }
        ) {
            Icon(imageVector = if (isSelected) item.selectedIcon else item.icon, contentDescription = item.label, modifier = Modifier.size(24.dp).scale(scale), tint = color)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = item.label, fontSize = 9.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, color = color, maxLines = 1)
    }
}

