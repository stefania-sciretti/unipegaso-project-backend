package com.clinica

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ClinicaApplication

fun main(args: Array<String>) {
    runApplication<ClinicaApplication>(*args)
}
