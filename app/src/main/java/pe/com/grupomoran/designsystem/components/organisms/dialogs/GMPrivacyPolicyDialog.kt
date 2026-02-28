package pe.com.grupomoran.designsystem.components.organisms.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun GMPrivacyPolicyDialog(
    privacyPolicyUrl: String = "http://pp.m4si.com:3333/politica/POLITICAS_PRIVACIDAD_Reparto.html",
    message: String = "Bienvenido a nuestra aplicación. Para poder utilizar todas sus funcionalidades, es necesario que aceptes nuestra Política de Privacidad. Esta detalla cómo protegemos tu información, incluyendo el acceso a la ubicación y cámara.",
    linkText: String = "Ver Política de Privacidad",
    onAccept: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val annotatedText = buildAnnotatedString {
        append(message)
        append("\n\n")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("IMPORTANTE: ")
        }

        append("Puedes revisar el documento completo haciendo clic aquí: ")

        pushStringAnnotation(tag = "URL", annotation = privacyPolicyUrl)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(linkText)
        }
        pop()

        append("\n\nAl presionar 'Aceptar', confirmas que has leído y aceptas los términos.")
    }

    AlertDialog(
        onDismissRequest = { /* Bloqueado */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        shape = RoundedCornerShape(12.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Consentimiento de Privacidad",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    ClickableText(
                        text = annotatedText,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onClick = { offset ->
                            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                .firstOrNull()?.let { annotation ->
                                    uriHandler.openUri(annotation.item)
                                }
                        }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onAccept,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Aceptar y Continuar",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    )
}
