package com.example.teammate.ui.onboarding

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teammate.ui.navigation.HeroBoundsTransform
import com.example.teammate.ui.theme.PrimaryGreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    sharedTransitionScope: SharedTransitionScope? = null
) {
    // --- State for Entrance Animations ---
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isVisible = true
    }

    // Header entrance animation
    val headerAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "headerAlpha"
    )
    val headerOffsetY by animateDpAsState(
        targetValue = if (isVisible) 0.dp else (-24).dp,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "headerOffset"
    )

    // Staggered Cards entrance animations (sliding in from the right)
    val card1Alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, delayMillis = 150, easing = FastOutSlowInEasing),
        label = "card1Alpha"
    )
    val card1SlideX by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 50.dp,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
        label = "card1Slide"
    )

    val card2Alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, delayMillis = 300, easing = FastOutSlowInEasing),
        label = "card2Alpha"
    )
    val card2SlideX by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 50.dp,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
        label = "card2Slide"
    )

    val card3Alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, delayMillis = 450, easing = FastOutSlowInEasing),
        label = "card3Alpha"
    )
    val card3SlideX by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 50.dp,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow),
        label = "card3Slide"
    )

    // Bottom Texts entrance animation
    val bottomAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 600, delayMillis = 600, easing = FastOutSlowInEasing),
        label = "bottomAlpha"
    )
    val bottomOffsetY by animateDpAsState(
        targetValue = if (isVisible) 0.dp else 30.dp,
        animationSpec = tween(durationMillis = 600, delayMillis = 600, easing = FastOutSlowInEasing),
        label = "bottomOffset"
    )

    // Button entrance animation (bounce / scale)
    val buttonAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 500, delayMillis = 750),
        label = "buttonAlpha"
    )
    val buttonScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "buttonScale"
    )

    // --- Infinite Transitions for Continuous "Alive" Floating Motions ---
    val infiniteTransition = rememberInfiniteTransition(label = "ambientMotion")

    // Subtle floating offsets for each card with different frequencies & phases
    val card1FloatY by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card1Float"
    )

    val card2FloatY by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = -4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card2Float"
    )

    val card3FloatY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card3Float"
    )

    // Checkmark subtle breathing pulse
    val checkPulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "checkPulse"
    )

    // Arrow continuous invite nudge (shifts right & left)
    val arrowNudgeX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 750, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "arrowNudge"
    )

    // Button touch interaction
    val buttonInteractionSource = remember { MutableInteractionSource() }
    val isPressed by buttonInteractionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "pressScale"
    )

    // Beautiful vibrant emerald gradient matching the design
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2DC468),
            Color(0xFF1CAE5A),
            Color(0xFF128B45)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        // Decorative ambient glow circle in the background
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // --- Top App Header: Logo & Name ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .graphicsLayer {
                        alpha = headerAlpha
                        translationY = headerOffsetY.toPx()
                    }
            ) {
                Surface(
                    modifier = Modifier
                        .size(46.dp)
                        .then(
                            if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                                with(sharedTransitionScope) {
                                    Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "app_logo_container"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        boundsTransform = HeroBoundsTransform
                                    )
                                }
                            } else Modifier
                        ),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        TeamMateLogoIcon(
                            modifier = Modifier
                                .size(24.dp)
                                .then(
                                    if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                                        with(sharedTransitionScope) {
                                            Modifier.sharedElement(
                                                state = rememberSharedContentState(key = "app_logo_icon"),
                                                animatedVisibilityScope = animatedVisibilityScope,
                                                boundsTransform = HeroBoundsTransform
                                            )
                                        }
                                    } else Modifier
                                ),
                            tint = PrimaryGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "TeamMate",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp,
                    modifier = Modifier.then(
                        if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                            with(sharedTransitionScope) {
                                Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "header_title"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = HeroBoundsTransform
                                )
                            }
                        } else Modifier
                    )
                )
            }

            Spacer(modifier = Modifier.weight(0.9f))

            // --- Floating Cards Visualization (Staggered widths matching design) ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Card 1: "Research competitors" (widest)
                TaskCard(
                    modifier = Modifier
                        .fillMaxWidth(0.98f)
                        .graphicsLayer {
                            alpha = card1Alpha
                            translationX = card1SlideX.toPx()
                            translationY = card1FloatY
                        },
                    title = "Research competitors",
                    isChecked = true,
                    checkScale = checkPulseScale,
                    badgeText = "W1",
                    badgeColor = Color(0xFF166534)
                )

                // Card 2: "Design slides" (middle width)
                TaskCard(
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .graphicsLayer {
                            alpha = card2Alpha
                            translationX = card2SlideX.toPx()
                            translationY = card2FloatY
                        },
                    title = "Design slides",
                    isChecked = false,
                    badgeText = "W3",
                    badgeColor = Color(0xFF166534)
                )

                // Card 3: "Write report" (narrowest width)
                TaskCard(
                    modifier = Modifier
                        .fillMaxWidth(0.78f)
                        .graphicsLayer {
                            alpha = card3Alpha
                            translationX = card3SlideX.toPx()
                            translationY = card3FloatY
                        },
                    title = "Write report",
                    isChecked = false,
                    badgeText = "S4",
                    badgeColor = Color(0xFFEA580C)
                )
            }

            Spacer(modifier = Modifier.weight(1.2f))

            // --- Bottom Section: Title, Subtitle, and Get Started Button ---
            Column(
                modifier = Modifier.graphicsLayer {
                    alpha = bottomAlpha
                    translationY = bottomOffsetY.toPx()
                }
            ) {
                Text(
                    text = "Simplify Team Projects\n& Task Distribution",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Split team tasks, follow every deadline, and watch progress materialize.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    lineHeight = 21.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onGetStarted,
                interactionSource = buttonInteractionSource,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .graphicsLayer {
                        alpha = buttonAlpha
                        scaleX = buttonScale * pressScale
                        scaleY = buttonScale * pressScale
                    }
                    .then(
                        if (sharedTransitionScope != null && animatedVisibilityScope != null) {
                            with(sharedTransitionScope) {
                                Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "primary_action_button"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = HeroBoundsTransform
                                )
                            }
                        } else Modifier
                    )
                    .shadow(elevation = 6.dp, shape = CircleShape),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = PrimaryGreen
                ),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Get Started",
                        color = PrimaryGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Get Started",
                        tint = PrimaryGreen,
                        modifier = Modifier
                            .size(18.dp)
                            .graphicsLayer {
                                translationX = arrowNudgeX
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun TaskCard(
    modifier: Modifier = Modifier,
    title: String,
    isChecked: Boolean,
    checkScale: Float = 1f,
    badgeText: String,
    badgeColor: Color
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = Color.Black.copy(alpha = 0.15f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            ),
        shape = CircleShape,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkmark or Empty Circle
            if (isChecked) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer {
                            scaleX = checkScale
                            scaleY = checkScale
                        }
                        .clip(CircleShape)
                        .background(Color(0xFFDCFCE7))
                        .border(1.5.dp, Color(0xFF22C55E), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(14.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .border(2.dp, Color(0xFFCBD5E1), CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Task title
            Text(
                text = title,
                color = Color(0xFF1E293B),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            // Right badge
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeText,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun TeamMateLogoIcon(
    modifier: Modifier = Modifier,
    tint: Color = PrimaryGreen
) {
    Canvas(modifier = modifier) {
        val strokeWidth = 2.dp.toPx()
        val stroke = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )

        val headRadius = size.width * 0.15f

        // Left Person: Head (center around 33%, 30%)
        drawCircle(
            color = tint,
            radius = headRadius,
            center = Offset(size.width * 0.33f, size.height * 0.30f),
            style = stroke
        )

        // Right Person: Head (center around 67%, 30%)
        drawCircle(
            color = tint,
            radius = headRadius,
            center = Offset(size.width * 0.67f, size.height * 0.30f),
            style = stroke
        )

        // Left Person: Shoulders (arc)
        drawArc(
            color = tint,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width * 0.10f, size.height * 0.52f),
            size = Size(size.width * 0.46f, size.height * 0.46f),
            style = stroke
        )

        // Right Person: Shoulders (arc)
        drawArc(
            color = tint,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width * 0.44f, size.height * 0.52f),
            size = Size(size.width * 0.46f, size.height * 0.46f),
            style = stroke
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen(onGetStarted = {})
}
