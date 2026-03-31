package pe.com.grupomoran.designsystem.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

/**
 * Componente especializado para mostrar productos en resultados de búsqueda.
 */
@Composable
fun GMProductListItem(
    name: String,
    sku: String,
    price: Double,
    stock: Int,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    selected: Boolean = false,
    onAddClick: (() -> Unit)? = null, // Nueva acción opcional
    onClick: () -> Unit
) {
    GMListItem(
        headline = name,
        supportingText = "SKU: $sku",
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .border(0.5.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "S/ ${"%.2f".format(price)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = if (stock > 0) Color(0xFF4CAF50) else Color.Red
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "$stock disp.",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = if (stock > 0) Color(0xFF4CAF50) else Color.Red
                        )
                    }
                }

                // Botón de Agregar Rápido
                onAddClick?.let {
                    Spacer(Modifier.width(12.dp))
                    FilledIconButton(
                        onClick = it,
                        modifier = Modifier.size(32.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar",
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true, name = "Búsqueda de Productos")
@Composable
fun GMSearchProductPreview() {
    GMDesignSystemTheme {
        Column(
            modifier = Modifier
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "RESULTADOS DE BÚSQUEDA", 
                style = MaterialTheme.typography.labelLarge, 
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            // Caso con botón de agregar directo
            GMProductListItem(
                name = "Aceite Primor Premium 1L",
                sku = "PROD-001",
                price = 12.50,
                stock = 45,
                onAddClick = {}, // Al pasarle esto, aparece el botón +
                onClick = {}
            )
            
            // Caso solo selección (para ver detalle por ejemplo)
            GMProductListItem(
                name = "Arroz Costeño Saco 5kg",
                sku = "PROD-042",
                price = 24.90,
                stock = 12,
                selected = true,
                onClick = {}
            )
            
            GMProductListItem(
                name = "Leche Gloria Azul Six Pack",
                sku = "PROD-109",
                price = 21.00,
                stock = 0,
                onAddClick = null, // Sin botón si no hay stock (lógica que puedes aplicar)
                onClick = {}
            )
        }
    }
}
