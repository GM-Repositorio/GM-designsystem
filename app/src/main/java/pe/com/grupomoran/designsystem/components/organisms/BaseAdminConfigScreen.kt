package pe.com.grupomoran.designsystem.components.organisms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.tooling.preview.Preview
import pe.com.grupomoran.designsystem.R
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

@Composable
fun BaseAdminConfigScreen(
    logoRes: Int,
    titleLine1: String,
    titleLine2: String,
    subtitle: String,
    urlEditValue: String,
    onUrlEditChange: (String) -> Unit,
    urlObtainedValue: String,
    onSaveClick: () -> Unit,
    isSaveEnabled: Boolean = true,
    contentOverlay: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        // Cabecera
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(id = logoRes),
                    contentDescription = null,
                    modifier = Modifier.size(72.dp)
                )

                Column {
                    Text(
                        text = titleLine1,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = titleLine2,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            }
        }

        // Formulario (centro)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "URL",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = urlEditValue,
                onValueChange = onUrlEditChange,
                label = { Text("URL Editar") },
                placeholder = { Text("Ingrese la URL") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "URL Obtenida",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = urlObtainedValue,
                onValueChange = {},
                enabled = false,
                label = { Text("URL del Servidor") },
                placeholder = { Text("URL no disponible") },
                singleLine = true
            )
        }

        // Pie de página (Botón)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = isSaveEnabled,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "GUARDAR CONFIGURACIÓN",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        // Overlay para diálogos
        contentOverlay()
    }
}

@Preview(showBackground = true)
@Composable
fun BaseAdminConfigScreenPreview() {
    GMDesignSystemTheme {
        BaseAdminConfigScreen(
            logoRes = R.drawable.logojmv2,
            titleLine1 = "J. MORAN",
            titleLine2 = "DISTRIBUCIONES",
            subtitle = "Configuración de Administrador",
            urlEditValue = "http://api.m4si.com:8080",
            onUrlEditChange = {},
            urlObtainedValue = "http://api.m4si.com:8080",
            onSaveClick = {},
            isSaveEnabled = true
        )
    }
}
