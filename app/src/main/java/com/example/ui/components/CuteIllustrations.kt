package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessAlarm
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.TaskCategory

fun TaskCategory.iconVector(): ImageVector = when (this) {
    TaskCategory.DAILY -> Icons.Rounded.TaskAlt
    TaskCategory.MEDICINE -> Icons.Rounded.Medication
    TaskCategory.PAYMENT -> Icons.Rounded.CreditCard
    TaskCategory.APPOINTMENT -> Icons.Rounded.CalendarMonth
    TaskCategory.CALL -> Icons.Rounded.Call
    TaskCategory.ALARM -> Icons.Rounded.AccessAlarm
}

@Composable
fun CategoryCircularIcon(
    category: TaskCategory,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    isSelected: Boolean = false
) {
    val bg = if (isSelected) Color.White else category.primaryColor
    val tint = if (isSelected) category.primaryColor else Color.White
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bg)
            .border(
                width = if (isSelected) 0.dp else 2.dp,
                color = Color.White.copy(alpha = 0.6f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = category.iconVector(),
            contentDescription = category.persianTitle,
            tint = tint,
            modifier = Modifier.size(size * 0.56f)
        )
    }
}

@Composable
fun CuteUserAvatar(
    avatarIndex: Int,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp
) {
    val bgColors = listOf(
        Color(0xFFD6EEFF),
        Color(0xFFE4E1FF),
        Color(0xFFD8F8F3),
        Color(0xFFFFE6EC)
    )
    val shirtColors = listOf(
        Color(0xFF4CD7B6),
        Color(0xFF6E78C8),
        Color(0xFFF4B03E),
        Color(0xFFFF7E9D)
    )
    val bgColor = bgColors[avatarIndex.mod(bgColors.size)]
    val shirtColor = shirtColors[avatarIndex.mod(shirtColors.size)]

    Canvas(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor)
            .border(2.dp, Color.White, CircleShape)
    ) {
        val w = this.size.width
        val h = this.size.height

        // Soft decorative circle behind character
        drawCircle(
            color = Color.White.copy(alpha = 0.45f),
            radius = w * 0.36f,
            center = Offset(w * 0.52f, h * 0.45f)
        )

        // Back hair
        drawRoundRect(
            color = Color(0xFF283044),
            topLeft = Offset(w * 0.26f, h * 0.20f),
            size = Size(w * 0.48f, h * 0.48f),
            cornerRadius = CornerRadius(w * 0.24f, w * 0.24f)
        )

        // Shoulders / Shirt
        val shirtPath = Path().apply {
            moveTo(w * 0.16f, h)
            quadraticTo(w * 0.5f, h * 0.64f, w * 0.84f, h)
            close()
        }
        drawPath(shirtPath, color = shirtColor)

        // Neck
        drawRoundRect(
            color = Color(0xFFFFF3E6),
            topLeft = Offset(w * 0.43f, h * 0.54f),
            size = Size(w * 0.14f, h * 0.16f),
            cornerRadius = CornerRadius(w * 0.06f, w * 0.06f)
        )

        // Face
        drawCircle(
            color = Color(0xFFFFF8F2),
            radius = w * 0.19f,
            center = Offset(w * 0.5f, h * 0.42f)
        )

        // Rosy cheeks
        drawCircle(
            color = Color(0xFFFF9EB5).copy(alpha = 0.55f),
            radius = w * 0.035f,
            center = Offset(w * 0.39f, h * 0.46f)
        )
        drawCircle(
            color = Color(0xFFFF9EB5).copy(alpha = 0.55f),
            radius = w * 0.035f,
            center = Offset(w * 0.61f, h * 0.46f)
        )

        // Cute round glasses
        drawCircle(
            color = Color(0xFF283044),
            radius = w * 0.055f,
            center = Offset(w * 0.42f, h * 0.41f),
            style = Stroke(width = w * 0.022f)
        )
        drawCircle(
            color = Color(0xFF283044),
            radius = w * 0.055f,
            center = Offset(w * 0.58f, h * 0.41f),
            style = Stroke(width = w * 0.022f)
        )
        drawLine(
            color = Color(0xFF283044),
            start = Offset(w * 0.475f, h * 0.41f),
            end = Offset(w * 0.525f, h * 0.41f),
            strokeWidth = w * 0.02f
        )

        // Smile
        drawArc(
            color = Color(0xFF283044),
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(w * 0.45f, h * 0.45f),
            size = Size(w * 0.10f, h * 0.06f),
            style = Stroke(width = w * 0.022f, cap = StrokeCap.Round)
        )

        // Front bangs
        val bangsPath = Path().apply {
            moveTo(w * 0.31f, h * 0.35f)
            quadraticTo(w * 0.45f, h * 0.18f, w * 0.69f, h * 0.35f)
            quadraticTo(w * 0.55f, h * 0.28f, w * 0.31f, h * 0.35f)
            close()
        }
        drawPath(bangsPath, color = Color(0xFF283044))
    }
}

/**
 * Rich custom illustration matching the Dribbble/Behance reference cards:
 * - Character with laptop & speech bubbles (DAILY)
 * - Cute capsule & medicine kit (MEDICINE)
 * - Character with golden coins & card (PAYMENT)
 * - Character with planner & chart (APPOINTMENT)
 * - Characters chatting & calling (CALL)
 * - Cute ringing twin-bell alarm clock & sparkles (ALARM)
 */
@Composable
fun CategoryCharacterIllustration(
    category: TaskCategory,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val darkInk = Color(0xFF222B45)

        // Soft translucent backdrop blobs like the reference cards
        drawCircle(
            color = Color.White.copy(alpha = 0.22f),
            radius = minOf(w, h) * 0.42f,
            center = Offset(w * 0.50f, h * 0.52f)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.14f),
            radius = minOf(w, h) * 0.24f,
            center = Offset(w * 0.24f, h * 0.30f)
        )

        when (category) {
            TaskCategory.DAILY -> {
                // Speech bubbles top right (just like "Digital Design Thinking" card!)
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.62f, h * 0.10f),
                    size = Size(w * 0.32f, h * 0.16f),
                    cornerRadius = CornerRadius(12f, 12f)
                )
                drawLine(
                    color = darkInk.copy(alpha = 0.5f),
                    start = Offset(w * 0.67f, h * 0.18f),
                    end = Offset(w * 0.88f, h * 0.18f),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.62f, h * 0.30f),
                    size = Size(w * 0.32f, h * 0.15f),
                    cornerRadius = CornerRadius(12f, 12f)
                )
                drawLine(
                    color = darkInk.copy(alpha = 0.5f),
                    start = Offset(w * 0.67f, h * 0.37f),
                    end = Offset(w * 0.86f, h * 0.37f),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )

                // Cute character hair & head
                drawRoundRect(
                    color = darkInk,
                    topLeft = Offset(w * 0.33f, h * 0.22f),
                    size = Size(w * 0.30f, h * 0.36f),
                    cornerRadius = CornerRadius(32f, 32f)
                )
                // Shirt
                drawRoundRect(
                    color = Color(0xFF2898F0),
                    topLeft = Offset(w * 0.26f, h * 0.52f),
                    size = Size(w * 0.44f, h * 0.32f),
                    cornerRadius = CornerRadius(26f, 26f)
                )
                // Face
                drawCircle(
                    color = Color.White,
                    radius = w * 0.10f,
                    center = Offset(w * 0.48f, h * 0.38f)
                )
                // Glasses
                drawCircle(
                    color = darkInk,
                    radius = w * 0.032f,
                    center = Offset(w * 0.44f, h * 0.38f),
                    style = Stroke(width = 2.5f)
                )
                drawCircle(
                    color = darkInk,
                    radius = w * 0.032f,
                    center = Offset(w * 0.52f, h * 0.38f),
                    style = Stroke(width = 2.5f)
                )

                // Little potted plant on left
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.12f, h * 0.68f),
                    size = Size(w * 0.13f, h * 0.16f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawCircle(
                    color = Color(0xFF90E0B6),
                    radius = w * 0.06f,
                    center = Offset(w * 0.18f, h * 0.62f)
                )

                // White laptop in front
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.30f, h * 0.58f),
                    size = Size(w * 0.38f, h * 0.26f),
                    cornerRadius = CornerRadius(10f, 10f)
                )
                drawRoundRect(
                    color = darkInk,
                    topLeft = Offset(w * 0.30f, h * 0.58f),
                    size = Size(w * 0.38f, h * 0.26f),
                    cornerRadius = CornerRadius(10f, 10f),
                    style = Stroke(width = 2.5f)
                )
                // Desk line
                drawLine(
                    color = darkInk,
                    start = Offset(w * 0.10f, h * 0.85f),
                    end = Offset(w * 0.88f, h * 0.85f),
                    strokeWidth = 3f,
                    cap = StrokeCap.Round
                )
            }

            TaskCategory.MEDICINE -> {
                // Cute pill bottle on left
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.18f, h * 0.34f),
                    size = Size(w * 0.30f, h * 0.48f),
                    cornerRadius = CornerRadius(16f, 16f)
                )
                drawRoundRect(
                    color = Color(0xFFFFB74D),
                    topLeft = Offset(w * 0.21f, h * 0.24f),
                    size = Size(w * 0.24f, h * 0.11f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                // Red/Coral cross on bottle
                drawRoundRect(
                    color = Color(0xFFFF7E9D),
                    topLeft = Offset(w * 0.30f, h * 0.48f),
                    size = Size(w * 0.06f, h * 0.18f),
                    cornerRadius = CornerRadius(4f, 4f)
                )
                drawRoundRect(
                    color = Color(0xFFFF7E9D),
                    topLeft = Offset(w * 0.24f, h * 0.54f),
                    size = Size(w * 0.18f, h * 0.06f),
                    cornerRadius = CornerRadius(4f, 4f)
                )

                // Cute smiling capsule on right
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.54f, h * 0.28f),
                    size = Size(w * 0.26f, h * 0.52f),
                    cornerRadius = CornerRadius(40f, 40f)
                )
                drawRoundRect(
                    color = Color(0xFFFFD54F),
                    topLeft = Offset(w * 0.54f, h * 0.54f),
                    size = Size(w * 0.26f, h * 0.26f),
                    cornerRadius = CornerRadius(36f, 36f)
                )
                // Eyes & smile on capsule
                drawCircle(color = darkInk, radius = 3.5f, center = Offset(w * 0.62f, h * 0.42f))
                drawCircle(color = darkInk, radius = 3.5f, center = Offset(w * 0.72f, h * 0.42f))
                drawArc(
                    color = darkInk,
                    startAngle = 20f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(w * 0.63f, h * 0.44f),
                    size = Size(w * 0.08f, h * 0.05f),
                    style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                )
            }

            TaskCategory.PAYMENT -> {
                // Cute wallet / bank card & golden coins
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.16f, h * 0.34f),
                    size = Size(w * 0.56f, h * 0.42f),
                    cornerRadius = CornerRadius(18f, 18f)
                )
                // Magnetic stripe
                drawRoundRect(
                    color = darkInk.copy(alpha = 0.8f),
                    topLeft = Offset(w * 0.16f, h * 0.44f),
                    size = Size(w * 0.56f, h * 0.09f)
                )
                // Gold chip
                drawRoundRect(
                    color = Color(0xFFFFCA28),
                    topLeft = Offset(w * 0.24f, h * 0.58f),
                    size = Size(w * 0.14f, h * 0.10f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                // Floating golden coin with star
                drawCircle(
                    color = Color(0xFFFFD54F),
                    radius = w * 0.16f,
                    center = Offset(w * 0.72f, h * 0.34f)
                )
                drawCircle(
                    color = Color.White,
                    radius = w * 0.16f,
                    center = Offset(w * 0.72f, h * 0.34f),
                    style = Stroke(width = 3f)
                )
                drawCircle(
                    color = Color(0xFFFFA000),
                    radius = w * 0.09f,
                    center = Offset(w * 0.72f, h * 0.34f)
                )
            }

            TaskCategory.APPOINTMENT -> {
                // Character with notebook & chart (like "Creative Art Design" card!)
                drawCircle(
                    color = Color.White.copy(alpha = 0.3f),
                    radius = w * 0.14f,
                    center = Offset(w * 0.22f, h * 0.36f)
                )
                // Character hair
                drawRoundRect(
                    color = darkInk,
                    topLeft = Offset(w * 0.34f, h * 0.24f),
                    size = Size(w * 0.30f, h * 0.34f),
                    cornerRadius = CornerRadius(28f, 28f)
                )
                // Yellow shirt
                drawRoundRect(
                    color = Color(0xFFF6C358),
                    topLeft = Offset(w * 0.26f, h * 0.52f),
                    size = Size(w * 0.44f, h * 0.36f),
                    cornerRadius = CornerRadius(24f, 24f)
                )
                // Face
                drawCircle(
                    color = Color.White,
                    radius = w * 0.095f,
                    center = Offset(w * 0.49f, h * 0.39f)
                )
                // Open notebook in hands
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.44f, h * 0.58f),
                    size = Size(w * 0.36f, h * 0.24f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawLine(
                    color = darkInk,
                    start = Offset(w * 0.62f, h * 0.58f),
                    end = Offset(w * 0.62f, h * 0.82f),
                    strokeWidth = 2.5f
                )
            }

            TaskCategory.CALL -> {
                // Two happy characters with phones & speech bubble (like "Quilting Courses" card!)
                // Left character
                drawCircle(color = darkInk, radius = w * 0.10f, center = Offset(w * 0.34f, h * 0.38f))
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.20f, h * 0.52f),
                    size = Size(w * 0.28f, h * 0.34f),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                drawCircle(color = Color.White, radius = w * 0.08f, center = Offset(w * 0.34f, h * 0.40f))

                // Right character
                drawCircle(color = darkInk, radius = w * 0.10f, center = Offset(w * 0.64f, h * 0.36f))
                drawRoundRect(
                    color = Color(0xFF3E95FF),
                    topLeft = Offset(w * 0.48f, h * 0.50f),
                    size = Size(w * 0.32f, h * 0.36f),
                    cornerRadius = CornerRadius(20f, 20f)
                )
                drawCircle(color = Color.White, radius = w * 0.08f, center = Offset(w * 0.64f, h * 0.38f))

                // Cute idea/message bubble above
                drawCircle(
                    color = Color(0xFFFFD54F),
                    radius = w * 0.07f,
                    center = Offset(w * 0.50f, h * 0.18f)
                )
            }

            TaskCategory.ALARM -> {
                // Cute twin-bell alarm clock
                // Left & Right bells
                drawCircle(
                    color = Color(0xFFFFD54F),
                    radius = w * 0.11f,
                    center = Offset(w * 0.28f, h * 0.28f)
                )
                drawCircle(
                    color = Color(0xFFFFD54F),
                    radius = w * 0.11f,
                    center = Offset(w * 0.72f, h * 0.28f)
                )
                // Main clock body
                drawCircle(
                    color = Color.White,
                    radius = w * 0.28f,
                    center = Offset(w * 0.50f, h * 0.56f)
                )
                drawCircle(
                    color = darkInk,
                    radius = w * 0.28f,
                    center = Offset(w * 0.50f, h * 0.56f),
                    style = Stroke(width = 3f)
                )
                // Clock hands
                drawLine(
                    color = darkInk,
                    start = Offset(w * 0.50f, h * 0.56f),
                    end = Offset(w * 0.50f, h * 0.40f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFFFF7E9D),
                    start = Offset(w * 0.50f, h * 0.56f),
                    end = Offset(w * 0.63f, h * 0.56f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

/**
 * Hero Banner illustration inspired by the right-side screen in the uploaded reference image:
 * A smartphone frame with two golden star cards and a hand tapping it.
 */
@Composable
fun HeroPhoneBannerIllustration(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val darkInk = Color(0xFF1E293B)

        // Phone outer body
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(w * 0.24f, h * 0.06f),
            size = Size(w * 0.52f, h * 0.84f),
            cornerRadius = CornerRadius(24f, 24f)
        )
        drawRoundRect(
            color = darkInk,
            topLeft = Offset(w * 0.24f, h * 0.06f),
            size = Size(w * 0.52f, h * 0.84f),
            cornerRadius = CornerRadius(24f, 24f),
            style = Stroke(width = 3f)
        )

        // Top yellow card inside phone (like the reference banner!)
        drawRoundRect(
            color = Color(0xFFF6C344),
            topLeft = Offset(w * 0.30f, h * 0.16f),
            size = Size(w * 0.56f, h * 0.24f),
            cornerRadius = CornerRadius(14f, 14f)
        )
        drawRoundRect(
            color = darkInk,
            topLeft = Offset(w * 0.30f, h * 0.16f),
            size = Size(w * 0.56f, h * 0.24f),
            cornerRadius = CornerRadius(14f, 14f),
            style = Stroke(width = 2.5f)
        )
        // White circle + star on top card
        drawCircle(
            color = Color.White,
            radius = w * 0.08f,
            center = Offset(w * 0.44f, h * 0.28f)
        )
        drawCircle(
            color = Color.White,
            radius = w * 0.09f,
            center = Offset(w * 0.72f, h * 0.28f)
        )

        // Bottom blue card inside phone
        drawRoundRect(
            color = Color(0xFF66BFFF),
            topLeft = Offset(w * 0.34f, h * 0.46f),
            size = Size(w * 0.56f, h * 0.24f),
            cornerRadius = CornerRadius(14f, 14f)
        )
        drawRoundRect(
            color = darkInk,
            topLeft = Offset(w * 0.34f, h * 0.46f),
            size = Size(w * 0.56f, h * 0.24f),
            cornerRadius = CornerRadius(14f, 14f),
            style = Stroke(width = 2.5f)
        )
        drawCircle(
            color = Color.White,
            radius = w * 0.07f,
            center = Offset(w * 0.48f, h * 0.58f)
        )

        // Cute hand tapping the card from bottom-left
        val handPath = Path().apply {
            moveTo(w * 0.10f, h * 0.96f)
            quadraticTo(w * 0.26f, h * 0.65f, w * 0.44f, h * 0.56f)
            quadraticTo(w * 0.50f, h * 0.58f, w * 0.40f, h * 0.76f)
            lineTo(w * 0.34f, h * 0.96f)
            close()
        }
        drawPath(handPath, color = Color.White)
        drawPath(handPath, color = darkInk, style = Stroke(width = 2.5f))
    }
}
