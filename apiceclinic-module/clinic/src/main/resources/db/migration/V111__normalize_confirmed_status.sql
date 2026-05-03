-- V111: Normalize legacy 'CONFIRMED' appointment status to 'BOOKED'
-- The AppointmentStatusEnum no longer includes CONFIRMED; map it to BOOKED.
UPDATE appointment SET status = 'BOOKED' WHERE status = 'CONFIRMED';
