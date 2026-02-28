package pe.com.grupomoran.designsystem.components.organisms

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Unarchive
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.tooling.preview.Preview
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

data class GMDeliveryOption(
    val id: Int,
    val label: String,
    val icon: ImageVector,
    val iconColor: Color
)

data class GMMotivoRechazo(
    val id: Int,
    val descripcion: String,
    val requiereFoto: Boolean
)

enum class GMProductDetailTab(val title: String) {
    ACCIONES("Acciones"),
    DOCUMENTOS("Documentos")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GMProductDetailDialog(
    productName: String,
    totalQuantity: Int,
    gre: String? = null,
    boleta: String? = null,
    motivosRechazo: List<GMMotivoRechazo> = emptyList(),
    options: List<GMDeliveryOption> = listOf(
        GMDeliveryOption(1, "Entregar", Icons.Default.Upload, Color(0xFF4CAF50)),
        GMDeliveryOption(3, "Rechazar", Icons.Default.Unarchive, Color(0xFFB71C1C)),
        GMDeliveryOption(4, "Otros", Icons.Default.CloudUpload, Color(0xFF9E9E9E))
    ),
    visibleTabs: List<GMProductDetailTab> = listOf(GMProductDetailTab.ACCIONES, GMProductDetailTab.DOCUMENTOS),
    onDismiss: () -> Unit,
    onViewPdf: (String) -> Unit = {},
    onTakePhoto: (onCaptured: (Uri, Bitmap) -> Unit) -> Unit = { _ -> },
    onConfirm: (quantity: Int, optionId: Int, motivo: GMMotivoRechazo?, photoUri: Uri?) -> Unit
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val currentTab = visibleTabs.getOrNull(selectedTabIndex) ?: GMProductDetailTab.ACCIONES

    var quantity by remember { mutableStateOf(totalQuantity.toString()) }
    var selectedOption by remember { mutableIntStateOf(1) }
    
    // Estado para el dropdown de motivos
    var expandedMotive by remember { mutableStateOf(false) }
    var selectedMotive by remember { mutableStateOf<GMMotivoRechazo?>(null) }

    // Estado para la foto
    var currentPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Estado para "Otros"
    var otherObservation by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = productName.uppercase(),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                if (visibleTabs.size > 1) {
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                            )
                        }
                    ) {
                        visibleTabs.forEachIndexed { index, tab ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(tab.title, style = MaterialTheme.typography.labelLarge) }
                            )
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    if (currentTab == GMProductDetailTab.ACCIONES) {
                        // --- PESTAÑA DE ACCIONES ---
                        options.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = (selectedOption == option.id),
                                        onClick = { selectedOption = option.id }
                                    )
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (selectedOption == option.id),
                                    onClick = null
                                )
                                Icon(
                                    imageVector = option.icon,
                                    contentDescription = null,
                                    tint = option.iconColor,
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .size(28.dp)
                                )
                                Text(
                                    text = option.label,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (option.id == 4) Color.Gray else Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (selectedOption == 3) {
                            ExposedDropdownMenuBox(
                                expanded = expandedMotive,
                                onExpandedChange = { expandedMotive = !expandedMotive },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = selectedMotive?.descripcion ?: "",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Motivo de rechazo") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMotive) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.LightGray
                                    ),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedMotive,
                                    onDismissRequest = { expandedMotive = false }
                                ) {
                                    if (motivosRechazo.isEmpty()) {
                                        DropdownMenuItem(
                                            text = { Text("No hay motivos cargados") },
                                            onClick = { expandedMotive = false }
                                        )
                                    } else {
                                        motivosRechazo.forEach { motivo ->
                                            DropdownMenuItem(
                                                text = { Text(motivo.descripcion) },
                                                onClick = {
                                                    selectedMotive = motivo
                                                    expandedMotive = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            
                            val requiresPhoto = selectedMotive?.requiereFoto == true
                            if (requiresPhoto) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "Evidencia fotográfica requerida:", 
                                    style = MaterialTheme.typography.labelMedium, 
                                    color = Color(0xFFB71C1C)
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                if (selectedBitmap != null) {
                                    Box(contentAlignment = Alignment.TopEnd) {
                                        Image(
                                            bitmap = selectedBitmap!!.asImageBitmap(),
                                            contentDescription = "Foto evidencia",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(150.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        )
                                        androidx.compose.material3.IconButton(
                                            onClick = { 
                                                selectedBitmap = null
                                                currentPhotoUri = null 
                                            },
                                            modifier = Modifier.background(
                                                Color.White.copy(alpha = 0.7f), 
                                                androidx.compose.foundation.shape.CircleShape
                                            )
                                        ) {
                                            Icon(Icons.Default.CameraAlt, contentDescription = "Retomar")
                                        }
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            onTakePhoto { uri, bitmap ->
                                                currentPhotoUri = uri
                                                selectedBitmap = bitmap
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                    ) {
                                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                                        Spacer(Modifier.width(8.dp))
                                        Text("Tomar Foto")
                                    }
                                }
                            }
                        } else if (selectedOption == 4) {
                            OutlinedTextField(
                                value = otherObservation,
                                onValueChange = { otherObservation = it },
                                label = { Text("Ingrese información adicional") },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 5
                            )
                        } else {
                            Spacer(modifier = Modifier.height(56.dp))
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(), 
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB1465B)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("CANCELAR", fontWeight = FontWeight.Bold)
                            }
                            
                            val requiresPhoto = (selectedOption == 3) && (selectedMotive?.requiereFoto == true)
                            val isPhotoValid = !requiresPhoto || (selectedBitmap != null)
                            val isRejectionValid = (selectedOption == 3) && (selectedMotive != null) && isPhotoValid
                            val isEnabled = when (selectedOption) {
                                1 -> true; 3 -> isRejectionValid; 4 -> true; else -> false
                            }
                            
                            Button(
                                onClick = { 
                                    if (selectedOption == 4) {
                                        Toast.makeText(context, "Próximamente se habilitará", Toast.LENGTH_SHORT).show()
                                    } else {
                                        onConfirm(
                                            quantity.toIntOrNull() ?: 0, 
                                            selectedOption, 
                                            selectedMotive, 
                                            currentPhotoUri
                                        ) 
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isEnabled) MaterialTheme.colorScheme.primary else Color(0xFFE0E0E0)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                enabled = isEnabled
                            ) {
                                Text(
                                    "ACEPTAR", 
                                    color = if (isEnabled) Color.White else Color.DarkGray, 
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        // --- PESTAÑA DE DOCUMENTOS (PDF) ---
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (!boleta.isNullOrEmpty()) {
                                OutlinedButton(
                                    onClick = { onViewPdf(boleta) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Receipt,
                                        contentDescription = "Ver PDF",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(Modifier.width(12.dp))
                                    Text("Ver Comprobante", style = MaterialTheme.typography.bodyLarge)
                                }
                            }

                            if (!gre.isNullOrEmpty()) {
                                OutlinedButton(
                                    onClick = { onViewPdf(gre) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
                                ) {
                                    Icon(Icons.Outlined.PictureAsPdf, null)
                                    Spacer(Modifier.width(12.dp))
                                    Text("Ver Guía Remisión", style = MaterialTheme.typography.bodyLarge)
                                }
                            }

                            if (gre.isNullOrEmpty() && boleta.isNullOrEmpty()) {
                                Icon(
                                    Icons.Outlined.PictureAsPdf, 
                                    null, 
                                    modifier = Modifier.size(64.dp), 
                                    tint = Color.LightGray
                                )
                                Text("No hay documentos PDF disponibles", color = Color.Gray)
                            }

                            Spacer(Modifier.height(32.dp))
                            
                            Button(
                                onClick = onDismiss,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("CERRAR", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GMProductDetailDialogPreview() {
    GMDesignSystemTheme {
        GMProductDetailDialog(
            productName = "Product de Prueba",
            totalQuantity = 600,
            gre = "https://example.com/gre.pdf",
            boleta = "https://example.com/boleta.pdf",
            motivosRechazo = listOf(
                GMMotivoRechazo(1, "Dañado", true),
                GMMotivoRechazo(2, "Incorrecto", false)
            ),
            onDismiss = {},
            onConfirm = { _, _, _, _ -> }
        )
    }
}
