package com.capstone.temantanam.ui.screen.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.capstone.temantanam.R
import com.capstone.temantanam.ui.navigation.Screen
import com.capstone.temantanam.ui.theme.TemanTanamTheme

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val registerViewModel: RegisterViewModel = viewModel()

    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    val showPopup = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title_register_page),
            textAlign = TextAlign.Start, // Align text to the left
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.message_register_page),
            textAlign = TextAlign.Start, // Align text to the left
            fontSize = 16.sp,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.name),
            textAlign = TextAlign.Start, // Align text to the left
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        )

        // Name Text Field
        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text(stringResource(R.string.name)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.email),
            textAlign = TextAlign.Start, // Align text to the left
            modifier = Modifier
                .padding(top = 8.dp)
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
                val name = nameState.value
                val email = emailState.value
                val password = passwordState.value

                registerViewModel.createUser(name, email, password)
                showPopup.value = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Text(text = stringResource(R.string.register))
        }
    }

    if (showPopup.value) {
        AlertDialog(
            onDismissRequest = { showPopup.value = false },
            title = { Text(text = stringResource(R.string.register_popup_title)) },
            text = { Text(text = stringResource(R.string.register_popup_message)) },
            confirmButton = {
                Button(
                    onClick = {
                        showPopup.value = false
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    content = { Text(text = stringResource(R.string.register_popup_confirm_button)) }
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    TemanTanamTheme {
        RegisterScreen(
            modifier = modifier,
            navController = navController,
        )
    }
}