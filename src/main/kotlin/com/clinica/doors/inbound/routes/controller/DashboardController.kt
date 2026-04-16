package com.clinica.controller

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Clinical appointment management")
class AppointmentController(
    private val appointmentService: AppointmentService
) {

    @GetMapping
    @Operation(summary = "List all appointments, optionally filtered by patientId, doctorId or status")
    fun findAll(
        @RequestParam(required = false) patientId: Long?,
        @RequestParam(required = false) doctorId: Long?,
        @RequestParam(required = false) status: String?
    ): List<AppointmentResponse> =
        appointmentService.findAll(patientId, doctorId, status)

    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    fun findById(@PathVariable id: Long): AppointmentResponse =
        appointmentService.findById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new appointment")
    fun create(@Valid @RequestBody request: AppointmentRequest): AppointmentResponse =
        appointmentService.create(request)

    @PutMapping("/{id}/status")
    @Operation(summary = "Update appointment status")
    fun updateStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: AppointmentStatusRequest
    ): AppointmentResponse =
        appointmentService.updateStatus(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancel an appointment")
    fun delete(@PathVariable id: Long) =
        appointmentService.delete(id)
}