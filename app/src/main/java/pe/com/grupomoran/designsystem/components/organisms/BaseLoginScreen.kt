package pe.com.grupomoran.designsystem.components.organisms

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BaseLoginScreen (
    logoRes: Int,
    titleLine1: String,
    titleLine2: String,
    subtitle: String,

    usernameValue:String,
    onUsernameChange:(String)->Unit,
    passwordValue:String,
    onPasswordChange:(String)->Unit,
    isLoading:Boolean,

    versionText: String,
    deviceIdText: String,

    onLoginClick: ()->Unit,

    contentOverlay: @Composable () -> Unit = {}
){
    Box(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
    Column(
        modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        // CABECERA DINÁMICA
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
                        fontSize = 22.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Text(
                    text = titleLine2,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp, fontWeight = FontWeight.Bold,
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

        Spacer(modifier = Modifier.height(32.dp))

        // CAMPOS (Se mantienen genéricos)
        OutlinedTextField(
            value = usernameValue,
            onValueChange = onUsernameChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Usuario") },
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = passwordValue,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginClick,
            enabled = usernameValue.isNotBlank() && passwordValue.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("INICIAR SESIÓN")
        }
    }

    // FOOTER DINÁMICO
    Row(
        modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
            .navigationBarsPadding().padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = versionText, style = MaterialTheme.typography.bodySmall)
        Text(text = deviceIdText, style = MaterialTheme.typography.bodySmall)
    }

    // Aquí se mostrarán los diálogos que inyectes
    contentOverlay()
}
}

@Preview(showBackground = true, name = "Login Grupo Moran")
@Composable
fun PreviewLoginMoran() {
    // Usamos un MaterialTheme envolvente para que los colores se apliquen correctamente
    MaterialTheme {
        BaseLoginScreen(
            logoRes = R.drawable.ic_menu_gallery, // Usamos uno de Android para el ejemplo
            titleLine1 = "GRUPO",
            titleLine2 = "MORAN",
            subtitle = "REPARTO",
            usernameValue = "Usuario123",
            onUsernameChange = {},
            passwordValue = "123456",
            onPasswordChange = {},
            isLoading = false,
            versionText = "v1.0.24",
            deviceIdText = "ID: 8a7b6c5d",
            onLoginClick = {}
        )
    }
}