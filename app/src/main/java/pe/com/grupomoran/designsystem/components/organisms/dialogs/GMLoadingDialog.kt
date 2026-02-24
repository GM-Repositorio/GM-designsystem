package pe.com.grupomoran.designsystem.components.organisms.dialogs

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun GMLoadingDialog(
    isLoading: Boolean,
    logoPainter: Painter,
    mensaje: String = "Cargando...",
) {
    if (isLoading) {
        val infiniteTransition = rememberInfiniteTransition(label = "LoadingAnimation")
        val alphaAnim by infiniteTransition.animateFloat(
            initialValue = 0.4f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
            ),
            label = "AlphaAnimation"
        )
            Dialog(onDismissRequest = { }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
            ) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = logoPainter,
                        contentDescription = "Loading",
                        modifier = Modifier
                            .size(40.dp)
                            // IMPORTANTE: Aquí aplicas el valor animado
                            .graphicsLayer(alpha = alphaAnim)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(text = mensaje, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Loading con Pulso - AppReparto")
@Composable
fun PreviewGMLoadingReparto() {
    MaterialTheme {
        GMLoadingDialog(
            isLoading = true,
            // Reemplaza 'logo' por el nombre real de tu archivo en drawable
            logoPainter = painterResource(id = pe.com.grupomoran.designsystem.R.drawable.logojmv2),
            mensaje = "Procesando reparto..."
        )
    }
}

@Preview(showBackground = true, name = "Loading con Pulso - AppVentas")
@Composable
fun PreviewGMLoadingVentas() {
    MaterialTheme {
        GMLoadingDialog(
            isLoading = true,
            logoPainter = painterResource(id = pe.com.grupomoran.designsystem.R.drawable.logojmv2),
            mensaje = "Sincronizando ventas..."
        )
    }
}