package com.example.teammate.ui.auth

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.teammate.ui.navigation.HeroBoundsTransform
import com.example.teammate.ui.onboarding.TeamMateLogoIcon
import com.example.teammate.ui.theme.BackgroundGray
import com.example.teammate.ui.theme.BorderLight
import com.example.teammate.ui.theme.CardWhite
import com.example.teammate.ui.theme.PrimaryGreen
import com.example.teammate.ui.theme.TeamMateTheme
import com.example.teammate.ui.theme.TextHint
import com.example.teammate.ui.theme.TextOnPrimary
import com.example.teammate.ui.theme.TextPrimary
import com.example.teammate.ui.theme.TextSecondary

private val PillShape = RoundedCornerShape(percent = 50)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun LoginScreen(
    onNavigateBack: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onCreateAccount: () -> Unit = {},
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    sharedTransitionScope: SharedTransitionScope? = null
) {
    var email by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = BackgroundGray
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Green App Logo with TeamMate Logo Icon (Hero Shared Element)
            Box(
                modifier = Modifier
                    .size(48.dp)
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
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(PrimaryGreen),
                contentAlignment = Alignment.Center
            ) {
                TeamMateLogoIcon(
                    modifier = Modifier
                        .size(26.dp)
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
                    tint = TextOnPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Welcome Text & Subtitle (Hero Shared Element Title)
            Text(
                text = "Welcome back 👋",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
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

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sign in to continue with your team.",
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(28.dp))

            // University Email Field
            LoginInputField(
                label = "University Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "you@estvendty.edu",
                leadingIcon = Icons.Outlined.MailOutline,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Student ID Field
            LoginInputField(
                label = "Student ID",
                value = studentId,
                onValueChange = { studentId = it },
                placeholder = "e.g. 20201018",
                leadingIcon = Icons.Outlined.Badge,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            LoginInputField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                placeholder = "••••••••",
                leadingIcon = Icons.Outlined.Lock,
                isPassword = true,
                isPasswordVisible = isPasswordVisible,
                onPasswordToggle = { isPasswordVisible = !isPasswordVisible },
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onImeAction = {
                    focusManager.clearFocus()
                    onLoginSuccess()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Forgot password? Link
//            Box(
//                modifier = Modifier.fillMaxWidth(),
//                contentAlignment = Alignment.CenterEnd
//            ) {
//                Text(
//                    text = "Forgot password?",
//                    color = PrimaryGreen,
//                    fontSize = 13.sp,
//                    fontWeight = FontWeight.Medium,
//                    modifier = Modifier.clickable { onForgotPassword() }
//                )
//            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Primary Button (Hero Shared Element)
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
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
                    ),
                shape = PillShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = TextOnPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text(
                    text = "Sign in",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer: New here? Create an account
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New here? ",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = "Create an account",
                    color = PrimaryGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onCreateAccount() }
                )
            }
        }
    }
}

@Composable
private fun LoginInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordToggle: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = TextHint,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = TextHint,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .size(20.dp)
                )
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(
                        onClick = onPasswordToggle,
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                            tint = TextHint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            } else null,
            shape = PillShape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardWhite,
                unfocusedContainerColor = CardWhite,
                focusedBorderColor = PrimaryGreen,
                unfocusedBorderColor = BorderLight,
                cursorColor = PrimaryGreen,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true,
            visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { onImeAction() },
                onDone = { onImeAction() }
            )
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    TeamMateTheme {
        LoginScreen()
    }
}
