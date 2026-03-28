package pe.com.grupomoran.designsystem.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
 * Componente de celda/ítem base para listas del Grupo Moran.
 * Diseñado bajo metodología Atomic Design (Molecule).
 * 
 * @param headline Texto principal (Título).
 * @param modifier Modificador de Compose.
 * @param supportingText Texto secundario opcional.
 * @param leadingContent Slot para icono o imagen a la izquierda.
 * @param trailingContent Slot para acciones o indicadores a la derecha.
 * @param statusColor Color para la barra lateral de estado (opcional).
 * @param onClick Acción al hacer clic.
 * @param selected Indica si el ítem está seleccionado.
 */
@Composable
fun GMListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    statusColor: Color? = null,
    onClick: (() -> Unit)? = null,
    selected: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f) 
            else containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 2.dp else 0.5.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Barra de Estado (Opcional)
            statusColor?.let { color ->
                Box(
                    modifier = Modifier
                        .size(width = 4.dp, height = 24.dp)
                        .background(color, CircleShape)
                )
                Spacer(Modifier.width(12.dp))
            }

            // 2. Contenido Izquierdo (Icono/Imagen)
            leadingContent?.let {
                it()
                Spacer(Modifier.width(12.dp))
            }

            // 3. Textos (Headline & Supporting)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = headline,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                supportingText?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // 4. Contenido Derecho (Acciones/Indicadores)
            trailingContent?.let {
                Spacer(Modifier.width(8.dp))
                it()
            } ?: run {
                if (onClick != null) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun GMListItemPreview() {
    GMDesignSystemTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Variantes de GMListItem", style = MaterialTheme.typography.titleSmall)

            // Caso 1: Simple con flecha (Navegación)
            GMListItem(
                headline = "Configuración de Perfil",
                supportingText = "Gestiona tus datos personales",
                onClick = {}
            )

            // Caso 2: Con Estado y Leading Icon (Tipo Documento)
            GMListItem(
                headline = "Factura F001-2345",
                supportingText = "Monto: S/ 1,250.00",
                statusColor = Color(0xFF4CAF50), // Verde entregado
                leadingContent = {
                    Icon(
                        Icons.Default.Info, 
                        null, 
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = {}
            )

            // Caso 3: Seleccionado
            GMListItem(
                headline = "Item Seleccionado",
                supportingText = "Este item tiene el estado 'selected = true'",
                selected = true,
                onClick = {}
            )

            // Caso 4: Con trailing content personalizado
            GMListItem(
                headline = "Notificaciones",
                trailingContent = {
                    Box(
                        modifier = Modifier
                            .background(Color.Red, CircleShape)
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("9+", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "Lista Realista")
@Composable
fun GMListItemListPreview() {
    GMDesignSystemTheme {
        Column(
            modifier = Modifier
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("BANDEJA DE ENTRADA", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            
            GMListItem(
                headline = "Guía de Remisión GR-9021",
                supportingText = "Hace 5 minutos • Pendiente de firma",
                statusColor = Color(0xFFFF9800),
                onClick = {}
            )
            
            GMListItem(
                headline = "Orden de Compra OC-552",
                supportingText = "Ayer • Entregado a almacén",
                statusColor = Color(0xFF4CAF50),
                onClick = {}
            )
            
            GMListItem(
                headline = "Reporte de Incidencia",
                supportingText = "12 Oct • Rechazado por cliente",
                statusColor = Color(0xFFF44336),
                onClick = {}
            )
        }
    }
}
