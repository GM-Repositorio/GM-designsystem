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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.com.grupomoran.designsystem.R

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
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 20.dp)) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter),
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
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = passwordValue,
            onValueChange = onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Contraseña") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            enabled = !isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    if (usernameValue.isNotBlank() && passwordValue.isNotBlank()) {
                        onLoginClick()
                    }
                }
            ),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            singleLine = true
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
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .navigationBarsPadding()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = versionText, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold )
        Text(text = deviceIdText, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold )
    }

    // Aquí se mostrarán los diálogos que inyectes
    contentOverlay()
}
}

@Preview(showBackground = true)
@Composable
fun BaseLoginScreenPreview() {
    BaseLoginScreen(
        logoRes = R.drawable.logojmv2, // Reemplaza con tu recurso de logo
        titleLine1 = "GRUPO",
        titleLine2 = "MORAN",
        subtitle = "REPARTO",

        usernameValue = "usuario_demo",
        onUsernameChange = {},
        passwordValue = "123456",
        onPasswordChange = {},

        isLoading = false,

        versionText = "v1.0.0",
        deviceIdText = "DEVICE12345",

        onLoginClick = {}
    )
}