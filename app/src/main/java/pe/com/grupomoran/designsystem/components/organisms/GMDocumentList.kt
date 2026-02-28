package pe.com.grupomoran.designsystem.components.organisms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

/**
 * Estado del documento para visualización.
 */
enum class GMDocListStatus { PENDIENTE, ENTREGADO, RECHAZO }

/**
 * Modelo genérico para un documento individual.
 */
data class GMDocumentItemModel(
    val id: Int,
    val code: String,
    val amount: Double? = null,
    val hasBoleta: Boolean = false,
    val hasGre: Boolean = false,
    val status: GMDocListStatus = GMDocListStatus.PENDIENTE,
    val isSynced: Boolean = true,
    val wasOffline: Boolean = false
)

/**
 * Modelo genérico para un subgrupo de documentos (ej. por referencia).
 */
data class GMDocumentSubGroupModel(
    val title: String,
    val documents: List<GMDocumentItemModel>
)

/**
 * Modelo genérico para un grupo de documentos (ej. por cliente).
 */
data class GMDocumentGroupModel(
    val id: String,
    val title: String,
    val subtitle: String?,
    val order: Int?,
    val totalDocuments: Int,
    val totalAmount: Double,
    val subGroups: List<GMDocumentSubGroupModel>
)

@Composable
fun GMDocumentList(
    groups: List<GMDocumentGroupModel>,
    isLoading: Boolean = false,
    isSelectionMode: Boolean = false,
    selectedIds: Set<Int> = emptySet(),
    onDocumentClick: (GMDocumentItemModel) -> Unit,
    onViewBoleta: (GMDocumentItemModel) -> Unit,
    onViewGre: (GMDocumentItemModel) -> Unit,
    onToggleSelection: (Int) -> Unit = {},
    onToggleGroupSelection: (GMDocumentGroupModel, Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    emptyStateContent: @Composable () -> Unit = { GMDefaultEmptyState() }
) {
    if (groups.isEmpty() && !isLoading) {
        emptyStateContent()
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(groups, key = { it.id }) { group ->
                GMExpandableDocumentGroupCard(
                    group = group,
                    isSelectionMode = isSelectionMode,
                    selectedIds = selectedIds,
                    onDocumentClick = onDocumentClick,
                    onViewBoleta = onViewBoleta,
                    onViewGre = onViewGre,
                    onToggleSelection = onToggleSelection,
                    onToggleGroupSelection = onToggleGroupSelection
                )
            }
        }
    }
}

@Composable
private fun GMExpandableDocumentGroupCard(
    group: GMDocumentGroupModel,
    isSelectionMode: Boolean,
    selectedIds: Set<Int>,
    onDocumentClick: (GMDocumentItemModel) -> Unit,
    onViewBoleta: (GMDocumentItemModel) -> Unit,
    onViewGre: (GMDocumentItemModel) -> Unit,
    onToggleSelection: (Int) -> Unit,
    onToggleGroupSelection: (GMDocumentGroupModel, Boolean) -> Unit
) {
    var expanded by remember { mutableStateOf(isSelectionMode) }

    LaunchedEffect(isSelectionMode) {
        if (isSelectionMode) expanded = true
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.animateContentSize()
    ) {
        Column {
            Row(
                Modifier
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSelectionMode) {
                    val allDocIds = group.subGroups.flatMap { it.documents.map { d -> d.id } }
                    val isAllSelected = allDocIds.isNotEmpty() && allDocIds.all { selectedIds.contains(it) }
                    
                    Checkbox(
                        checked = isAllSelected,
                        onCheckedChange = { onToggleGroupSelection(group, it) },
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = group.order?.toString() ?: "-",
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = group.title,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 14.sp,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        // Indicadores de Sincronización
                        val firstDoc = group.subGroups.firstOrNull()?.documents?.firstOrNull()
                        if (firstDoc != null && firstDoc.status != GMDocListStatus.PENDIENTE) {
                            Spacer(Modifier.width(6.dp))
                            GMSyncStatusIndicator(
                                isSynced = firstDoc.isSynced,
                                wasOffline = firstDoc.wasOffline
                            )
                        }
                    }

                    group.subtitle?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            lineHeight = 12.sp
                        )
                    }
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(Modifier.padding(bottom = 4.dp)) {
                    HorizontalDivider(Modifier.padding(horizontal = 12.dp), thickness = 0.5.dp)

                    group.subGroups.forEach { subGroup ->
                        Column(Modifier.padding(top = 2.dp)) {
                            subGroup.documents.forEach { doc ->
                                Box(modifier = Modifier.padding(start = 16.dp)) {
                                    GMDocumentItemRow(
                                        doc = doc,
                                        isSelectionMode = isSelectionMode,
                                        isSelected = selectedIds.contains(doc.id),
                                        onClick = { onDocumentClick(doc) },
                                        onToggleSelection = { onToggleSelection(doc.id) },
                                        onViewBoleta = { onViewBoleta(doc) },
                                        onViewGre = { onViewGre(doc) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GMDocumentItemRow(
    doc: GMDocumentItemModel,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onToggleSelection: () -> Unit,
    onViewBoleta: () -> Unit,
    onViewGre: () -> Unit
) {
    val statusColor = when (doc.status) {
        GMDocListStatus.PENDIENTE -> MaterialTheme.colorScheme.outlineVariant
        GMDocListStatus.ENTREGADO -> Color(0xFF4CAF50)
        GMDocListStatus.RECHAZO -> Color(0xFFD83448)
    }

    OutlinedCard(
        onClick = if (isSelectionMode) onToggleSelection else onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            0.5.dp, 
            if (isSelected) MaterialTheme.colorScheme.primary 
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isSelected) 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f) 
            else 
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.05f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onToggleSelection() },
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
            }

            Box(
                modifier = Modifier
                    .size(width = 3.dp, height = 14.dp)
                    .background(statusColor, CircleShape)
            )

            Spacer(Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                modifier = Modifier.size(16.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = doc.code,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (doc.amount != null) {
                Text(
                    text = "S/ ${"%.2f".format(doc.amount)}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            if (doc.hasBoleta) {
                IconButton(
                    onClick = onViewBoleta,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ReceiptLong,
                        contentDescription = "Ver Boleta",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (doc.hasGre) {
                IconButton(
                    onClick = onViewGre,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PictureAsPdf,
                        contentDescription = "Ver Guía",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun GMSyncStatusIndicator(
    isSynced: Boolean,
    wasOffline: Boolean
) {
    Box(contentAlignment = Alignment.Center) {
        if (wasOffline) {
            Icon(
                imageVector = if (isSynced) Icons.Default.CloudDone else Icons.Default.CloudOff,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (isSynced) Color(0xFF4CAF50) else Color(0xFFFF9800)
            )
            Icon(
                imageVector = Icons.Default.WifiOff,
                contentDescription = null,
                modifier = Modifier.size(7.dp).align(Alignment.BottomEnd).offset(x = 1.dp, y = 1.dp),
                tint = if (isSynced) Color(0xFF4CAF50) else Color(0xFFFF9800)
            )
        } else {
            Icon(
                imageVector = Icons.Default.CloudDone,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF2196F3)
            )
        }
    }
}

@Composable
fun GMDefaultEmptyState() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Outlined.Description,
                null,
                Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "No se encontraron documentos",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, name = "Vista Normal (Contraída)")
@Composable
fun GMDocumentListPreview() {
    val sampleDocs = listOf(
        GMDocumentItemModel(1, "B001-0001", 150.0, hasBoleta = true, hasGre = true),
        GMDocumentItemModel(2, "B001-0002", 85.50, hasBoleta = true, status = GMDocListStatus.ENTREGADO, wasOffline = true, isSynced = false),
        GMDocumentItemModel(3, "B001-0003", 200.0, hasGre = true, status = GMDocListStatus.RECHAZO)
    )

    val sampleGroups = listOf(
        GMDocumentGroupModel(
            id = "cli_1",
            title = "CORPORACION ALIMENTARIA S.A.C.",
            subtitle = "AV. LOS PROCERES 123 - JR. LIMA 456 - CERCADO DE LIMA",
            order = 1,
            totalDocuments = 3,
            totalAmount = 435.50,
            subGroups = listOf(
                GMDocumentSubGroupModel("REF-999", sampleDocs)
            )
        )
    )

    GMDesignSystemTheme {
        GMDocumentList(
            groups = sampleGroups,
            onDocumentClick = {},
            onViewBoleta = {},
            onViewGre = {}
        )
    }
}

@Preview(showBackground = true, name = "Vista Expandida (Selección)")
@Composable
fun GMDocumentListExpandedPreview() {
    val sampleDocs = listOf(
        GMDocumentItemModel(1, "B001-0001", 150.0, hasBoleta = true, hasGre = true),
        GMDocumentItemModel(2, "B001-0002", 85.50, hasBoleta = true, status = GMDocListStatus.ENTREGADO, wasOffline = true, isSynced = false)
    )

    val sampleGroups = listOf(
        GMDocumentGroupModel(
            id = "cli_1",
            title = "CORPORACION ALIMENTARIA S.A.C.",
            subtitle = "AV. LOS PROCERES 123 - LIMA",
            order = 1,
            totalDocuments = 2,
            totalAmount = 235.50,
            subGroups = listOf(
                GMDocumentSubGroupModel("REF-999", sampleDocs)
            )
        )
    )

    GMDesignSystemTheme {
        GMDocumentList(
            groups = sampleGroups,
            isSelectionMode = true,
            selectedIds = setOf(1),
            onDocumentClick = {},
            onViewBoleta = {},
            onViewGre = {}
        )
    }
}
