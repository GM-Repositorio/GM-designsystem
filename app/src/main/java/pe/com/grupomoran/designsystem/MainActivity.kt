package pe.com.grupomoran.designsystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import pe.com.grupomoran.designsystem.components.organisms.BaseLoginScreen
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GMDesignSystemTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseLoginScreen(
                        logoRes = android.R.drawable.ic_menu_gallery,
                        titleLine1 = "GRUPO",
                        titleLine2 = "MORAN",
                        subtitle = "SISTEMA DE DISEÑO",
                        usernameValue = "",
                        onUsernameChange = {},
                        passwordValue = "",
                        onPasswordChange = {},
                        isLoading = false,
                        versionText = "v1.0.0",
                        deviceIdText = "ID: DEFAULT",
                        onLoginClick = {}
                    )
                }
            }
        }
    }
}
