package com.example.registroempleados

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.registroempleados.ui.theme.RegistroEmpleadosTheme

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivityLifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Se utiliza el tema con el nombre de tu proyecto
            RegistroEmpleadosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GestionEmpleadosApp()
                }
            }
        }
    }

    // --- Métodos del Ciclo de Vida Solicitados ---
    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionEmpleadosApp() {
    var listaEmpleados by remember { mutableStateOf(listOf<Empleado>()) }

    var nombre by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var salario by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Empleados") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Registrar Empleado", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = cargo, onValueChange = { cargo = it }, label = { Text("Cargo") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = departamento, onValueChange = { departamento = it }, label = { Text("Departamento") }, modifier = Modifier.fillMaxWidth())

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = salario,
                    onValueChange = { salario = it },
                    label = { Text("Salario") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha Contratación") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank() && cargo.isNotBlank() && departamento.isNotBlank() && salario.isNotBlank() && fecha.isNotBlank()) {
                        val nuevoEmpleado = Empleado(
                            nombre = nombre,
                            cargo = cargo,
                            departamento = departamento,
                            salario = salario.toDoubleOrNull() ?: 0.0,
                            fechaContratacion = fecha
                        )
                        listaEmpleados = listaEmpleados + nuevoEmpleado

                        nombre = ""
                        cargo = ""
                        departamento = ""
                        salario = ""
                        fecha = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Empleados Registrados", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(listaEmpleados, key = { it.id }) { empleado ->
                    EmpleadoCard(
                        empleado = empleado,
                        onDeleteClick = { listaEmpleados = listaEmpleados.filter { it.id != empleado.id } }
                    )
                }
            }
        }
    }
}

@Composable
fun EmpleadoCard(empleado: Empleado, onDeleteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = empleado.nombre,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item { SuggestionChip(onClick = {}, label = { Text("Cargo: ${empleado.cargo}") }) }
                item { SuggestionChip(onClick = {}, label = { Text("Depto: ${empleado.departamento}") }) }
                item { SuggestionChip(onClick = {}, label = { Text("Salario: $${empleado.salario}") }) }
                item { SuggestionChip(onClick = {}, label = { Text("Fecha: ${empleado.fechaContratacion}") }) }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onDeleteClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Eliminar")
            }
        }
    }
}