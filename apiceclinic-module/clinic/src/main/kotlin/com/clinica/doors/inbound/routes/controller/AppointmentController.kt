//package com.clinica.controller
//
//import com.clinica.application.service.AppointmentService
//import com.clinica.application.service.FitnessAppointmentService
//import com.clinica.dto.FitnessAppointmentRequest
//import com.clinica.dto.FitnessAppointmentResponse
//import com.clinica.dto.FitnessAppointmentStatusRequest
//import io.swagger.v3.oas.annotations.Operation
//import io.swagger.v3.oas.annotations.responses.ApiResponse
//import io.swagger.v3.oas.annotations.responses.ApiResponses
//import io.swagger.v3.oas.annotations.tags.Tag
//import jakarta.validation.Valid
//import org.springframework.http.HttpStatus
//import org.springframework.web.bind.annotation.*
//
//@RestController
//@RequestMapping("/api/appointments")
//@Tag(name = "Appointments", description = "Appointment booking and management")
//class AppointmentController(
//    private val fitnessAppointmentService: AppointmentService,
//    private val fitnessAppointmentService: FitnessAppointmentService) {
//
//    @GetMapping
//    @Operation(summary = "List appointments")
//    @ApiResponses(value = [
//        ApiResponse(responseCode = "200", description = "List returned"),
//        ApiResponse(responseCode = "401", description = "Unauthorized")
//    ])
//    fun findAll(
//        @RequestParam(required = false) patientId: Long?,
//        @RequestParam(required = false) specialistId: Long?,
//        @RequestParam(required = false) status: String?
//    ): List<FitnessAppointmentResponse> = fitnessAppointmentService.findAll(patientId, specialistId, status)
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(summary = "Book a new appointment")
//    @ApiResponses(value = [
//        ApiResponse(responseCode = "201", description = "Appointment booked"),
//        ApiResponse(responseCode = "400", description = "Validation error"),
//        ApiResponse(responseCode = "401", description = "Unauthorized")
//    ])
//    fun create(@Valid @RequestBody request: FitnessAppointmentRequest): FitnessAppointmentResponse =
//        fitnessAppointmentService.create(request)
//
//    //TODO("se rimane tempo implementiamo, altrimenti ciao")
//    @GetMapping("/{id}")
//    @Operation(summary = "Get appointment by ID")
//    @ApiResponses(value = [
//        ApiResponse(responseCode = "200", description = "Appointment found"),
//        ApiResponse(responseCode = "404", description = "Appointment not found"),
//        ApiResponse(responseCode = "401", description = "Unauthorized")
//    ])
//    fun findById(@PathVariable id: Long): FitnessAppointmentResponse =
//        fitnessAppointmentService.findById(id)
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @Operation(summary = "Cancel an appointment")
//    @ApiResponses(value = [
//        ApiResponse(responseCode = "204", description = "Appointment cancelled"),
//        ApiResponse(responseCode = "404", description = "Appointment not found"),
//        ApiResponse(responseCode = "401", description = "Unauthorized")
//    ])
//    fun delete(@PathVariable id: Long) = fitnessAppointmentService.delete(id)
//
//    @PutMapping("/{id}/status")
//    @Operation(summary = "Update appointment status")
//    @ApiResponses(value = [
//        ApiResponse(responseCode = "200", description = "Status updated"),
//        ApiResponse(responseCode = "400", description = "Validation error"),
//        ApiResponse(responseCode = "404", description = "Appointment not found"),
//        ApiResponse(responseCode = "401", description = "Unauthorized")
//    ])
//    fun updateStatus(
//        @PathVariable id: Long,
//        @Valid @RequestBody request: FitnessAppointmentStatusRequest
//    ): FitnessAppointmentResponse = fitnessAppointmentService.updateStatus(id, request)
//}
