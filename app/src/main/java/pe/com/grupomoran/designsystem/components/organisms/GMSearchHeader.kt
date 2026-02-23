package pe.com.grupomoran.designsystem.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import pe.com.grupomoran.designsystem.components.atoms.GMCircleIconButton
import pe.com.grupomoran.designsystem.components.molecules.GMSearchInput

data class GMSearchAction(
    val icon: ImageVector,
    val description: String,
    val containerColor: Color,
    val contentColor: Color,
    val onClick: () -> Unit,
    val isVisible: Boolean = true
)

@Composable
fun GMSearchHeader(
    // Estado de Búsqueda
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: () -> Unit = {}, // ACCIÓN PERSONALIZABLE PARA CADA APP
    placeholder: String = "Buscar...",
    // Estado de Selección
    isSelectionMode: Boolean,
    selectedCount: Int = 0,
    onCancelSelection: () -> Unit = {},
    onSelectAll: () -> Unit = {},
    onExecuteBulkAction: (() -> Unit)? = null,
    // Botones Personalizados
    actions: List<GMSearchAction> = emptyList()
) {
    var showOverflowMenu by remember { mutableStateOf(false) }
    val visibleActions = actions.filter { it.isVisible }

    Row(
        Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (isSelectionMode) {
            // ... (Código de modo selección se mantiene igual)
            GMCircleIconButton(
                icon = Icons.Default.Close,
                onClick = onCancelSelection,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = "$selectedCount seleccionados",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f).padding(start = 8.dp)
            )
            GMCircleIconButton(
                icon = Icons.Default.SelectAll,
                onClick = onSelectAll,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
            if (onExecuteBulkAction != null) {
                GMCircleIconButton(
                    icon = Icons.Default.Check,
                    onClick = onExecuteBulkAction,
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White,
                    enabled = selectedCount > 0
                )
            }
        } else {
            // MODO BÚSQUEDA NORMAL
            GMSearchInput(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                onSearch = onSearch, // PASAMOS LA ACCIÓN AQUÍ
                placeholder = placeholder,
                modifier = Modifier.weight(1f)
            )

            if (visibleActions.size <= 3) {
                visibleActions.forEach { action ->
                    GMCircleIconButton(
                        icon = action.icon,
                        onClick = action.onClick,
                        containerColor = action.containerColor,
                        contentColor = action.contentColor,
                        contentDescription = action.description
                    )
                }
            } else {
                visibleActions.take(2).forEach { action ->
                    GMCircleIconButton(
                        icon = action.icon,
                        onClick = action.onClick,
                        containerColor = action.containerColor,
                        contentColor = action.contentColor,
                        contentDescription = action.description
                    )
                }
                Box {
                    GMCircleIconButton(
                        icon = Icons.Default.MoreVert,
                        onClick = { showOverflowMenu = true },
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    DropdownMenu(
                        expanded = showOverflowMenu,
                        onDismissRequest = { showOverflowMenu = false }
                    ) {
                        visibleActions.drop(2).forEach { action ->
                            DropdownMenuItem(
                                text = { Text(action.description) },
                                onClick = {
                                    showOverflowMenu = false
                                    action.onClick()
                                },
                                leadingIcon = { Icon(action.icon, null) }
                            )
                        }
                    }
                }
            }
        }
    }
}
