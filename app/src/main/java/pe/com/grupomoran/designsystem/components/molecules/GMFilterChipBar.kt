package pe.com.grupomoran.designsystem.components.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pe.com.grupomoran.designsystem.components.atoms.GMFilterChip

data class GMFilterOption(
    val id: String,
    val label: String,
    val icon: Any? = null, // ImageVector, Painter, etc.
    val color: Color? = null
)

@Composable
fun GMFilterChipBar(
    options: List<GMFilterOption>,
    selectedId: String?,
    onOptionSelected: (GMFilterOption) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = contentPadding
    ) {
        items(options, key = { it.id }) { option ->
            GMFilterChip(
                label = option.label,
                selected = option.id == selectedId,
                icon = option.icon,
                onClick = { onOptionSelected(option) },
                selectedColor = option.color ?: pe.com.grupomoran.designsystem.ui.theme.MoranBlue
            )
        }
    }
}
