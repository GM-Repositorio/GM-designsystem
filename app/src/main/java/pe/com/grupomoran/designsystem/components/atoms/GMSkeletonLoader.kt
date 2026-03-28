package pe.com.grupomoran.designsystem.components.atoms

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.com.grupomoran.designsystem.ui.theme.GMDesignSystemTheme

/**
 * Modifier que aplica un efecto de brillo (shimmer) animado.
 */
fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    background(brush)
}

/**
 * Componente atómico para representar un bloque de carga.
 */
@Composable
fun GMSkeletonItem(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(4.dp)
) {
    Box(
        modifier = modifier
            .shimmerEffect()
            .background(Color.LightGray.copy(alpha = 0.3f), shape)
    )
}

/**
 * Representación de carga para una tarjeta de documento (GMDocumentGroupCard).
 */
@Composable
fun GMSkeletonDocumentGroup(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo de orden
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .shimmerEffect()
                    .background(Color.LightGray.copy(alpha = 0.3f), CircleShape)
            )

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                // Título
                GMSkeletonItem(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                )
                
                Spacer(Modifier.height(8.dp))
                
                // Subtítulo
                GMSkeletonItem(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(10.dp)
                )
            }

            // Icono flecha
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .shimmerEffect()
                    .background(Color.LightGray.copy(alpha = 0.3f), CircleShape)
            )
        }
    }
}

/**
 * Lista de esqueletos para simular carga de múltiples elementos.
 */
@Composable
fun GMSkeletonDocumentList(
    count: Int = 5,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(count) {
            GMSkeletonDocumentGroup()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun GMSkeletonLoaderPreview() {
    GMDesignSystemTheme {
        Column(Modifier.padding(16.dp)) {
            androidx.compose.material3.Text(
                "Skeleton Item Individual",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            GMSkeletonItem(
                modifier = Modifier
                    .size(width = 100.dp, height = 20.dp)
                    .padding(top = 4.dp)
            )

            Spacer(Modifier.height(24.dp))

            androidx.compose.material3.Text(
                "Skeleton Document Group",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Spacer(Modifier.height(8.dp))
            GMSkeletonDocumentGroup()

            Spacer(Modifier.height(24.dp))

            androidx.compose.material3.Text(
                "Lista de Carga (Skeleton List)",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            GMSkeletonDocumentList(count = 3)
        }
    }
}
