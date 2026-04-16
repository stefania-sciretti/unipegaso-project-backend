package com.clinica.util

/**
 * Throws a [NoSuchElementException] with a descriptive message when the [Optional] is empty.
 *
 * Eliminates the repetitive `.orElseThrow { NoSuchElementException("X not found with id: $id") }`
 * pattern spread across every service layer (DRY principle).
 *
 * Usage:
 *   repository.findById(id).orEntityNotFound("Client", id)
 */
fun <T> Optional<T>.orEntityNotFound(entityName: String, id: Any): T =
    orElseThrow { NoSuchElementException("$entityName not found with id: $id") }
