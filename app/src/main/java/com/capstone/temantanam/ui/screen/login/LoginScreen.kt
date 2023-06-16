package com.capstone.temantanam.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.capstone.temantanam.R
import com.capstone.temantanam.ui.theme.TemanTanamTheme
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.temantanam.response.LoginData
import com.capstone.temantanam.ui.navigation.Screen
import com.capstone.temantanam.ui.session.UserSessionViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    userSessionViewModel: UserSessionViewModel
) {
    val loginViewModel: LoginViewModel = viewModel()

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    val result: LoginData? by loginViewModel.result.observeAsState()
    val isLoggedIn by loginViewModel.isLoggedIn.observeAsState(false)

    val isLoading by loginViewModel.isLoading.observeAsState(false)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.temantanam_logo),
            contentDescription = "App logo",
            modifier = Modifier
                .size(200.dp),
            contentScale = ContentScale.Fit,
        )

        Text(
            text = stringResource(R.string.title_login_page),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            modifier = Modifier
        )

        Text(
            text = stringResource(R.string.message_login_page),
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = stringResource(R.string.email),
            textAlign = TextAlign.Start, // Align text to the left
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        // Email Text Field
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text(stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.password),
            textAlign = TextAlign.Start, // Align text to the left
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        )

        // Password Text Field
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val email = emailState.value
                val password = passwordState.value
                loginViewModel.authenticateUser(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White, // Adjust the color if necessary
                    modifier = Modifier
                        .size(24.dp) // Adjust the size as needed
                ) // Show progress indicator when loading
            } else {
                Text(text = stringResource(R.string.login))
            }
        }

        ClickableText(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(textDecoration = TextDecoration.Underline)
                ) {
                    append(stringResource(R.string.register_here))
                }
            },
            onClick = {
                navController.navigate(Screen.Register.route)
            },
            modifier = Modifier.padding(top = 8.dp),
        )
    }

    // Navigate when login is successful
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            val resultData = result // Store the result in a local variable
            userSessionViewModel.setUserData(resultData?.id ?: "", resultData?.token ?: "")
            navController.navigate(Screen.Camera.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    userSessionViewModel: UserSessionViewModel = viewModel()
) {
    TemanTanamTheme {
        LoginScreen(
            modifier = modifier,
            navController = navController,
            userSessionViewModel = userSessionViewModel
        )
    }
}