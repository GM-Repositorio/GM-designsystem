package pe.com.grupomoran.designsystem.components.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.com.grupomoran.designsystem.components.molecules.GMFilterChipBar
import pe.com.grupomoran.designsystem.components.molecules.GMFilterOption
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

@Composable
fun GMExtendedHeaderPreview() {
    var query by remember { mutableStateOf("") }
    var selectedFilterId by remember { mutableStateOf("pending") }

    // Simulamos opciones dinámicas con ICONOS que vienen del servidor
    val dynamicFilters = listOf(
        GMFilterOption("pending", "Pendientes", Icons.Default.Schedule, Color(0xFF757575)),
        GMFilterOption("delivered", "Entregados", Icons.Default.CheckCircle, Color(0xFF4CAF50)),
        GMFilterOption("rejected", "Rechazados", Icons.Default.Warning, Color(0xFFD83448)),
        GMFilterOption("info", "Info", Icons.Default.Info, Color(0xFF2196F3)),
        GMFilterOption("history", "Historial", Icons.Default.History, Color(0xFF673AB7))
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Header con Iconos Dinámicos", 
            modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        
        GMSearchHeader(
            searchQuery = query,
            onSearchQueryChange = { query = it },
            isSelectionMode = false,
            actions = listOf(
                GMSearchAction(Icons.Default.QrCodeScanner, "QR", Color.LightGray, Color.Black, {}),
                GMSearchAction(Icons.Default.SwapVert, "Ordenar", Color.LightGray, Color.Black, {})
            )
        )

        // BARRA DE CHIPS CON ICONOS
        GMFilterChipBar(
            options = dynamicFilters,
            selectedId = selectedFilterId,
            onOptionSelected = { selectedFilterId = it.id }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Filtro activo: ${dynamicFilters.find { it.id == selectedFilterId }?.label}",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}