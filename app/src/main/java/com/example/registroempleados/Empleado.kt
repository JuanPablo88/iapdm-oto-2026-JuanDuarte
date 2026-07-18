package com.example.registroempleados

import java.util.UUID

data class Empleado(
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val cargo: String,
    val departamento: String,
    val salario: Double,
    val fechaContratacion: String
)