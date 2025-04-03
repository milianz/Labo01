package Hospital.Service;

import Hospital.Entity.Cita;
import Hospital.Entity.Doctor;
import Hospital.Entity.Paciente;
import Hospital.Util.GeneradorCodigos;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CitaService {
    private List<Cita> citas = new ArrayList<>();

    // Agendar una nueva cita
    public boolean agendarCita(Cita nuevaCita) {
        // Validación de parámetro de entrada
        if (nuevaCita == null) {
            throw new IllegalArgumentException("La cita no puede ser nula");
        }

        // Validación de componentes esenciales
        if (nuevaCita.getDoctor() == null || nuevaCita.getPaciente() == null) {
            System.out.println("Error: La cita debe tener asociado un doctor y un paciente");
            return false;
        }

        LocalDateTime fechaHora = nuevaCita.getFechaHora();
        LocalDate fechaCita = fechaHora.toLocalDate();
        LocalTime horaCita = fechaHora.toLocalTime();
        String idPaciente = nuevaCita.getPaciente().getCodigoPaciente();
        String idDoctor = nuevaCita.getDoctor().getCodigoDoctor();

        // 1. Validar horario de atención (8:00-16:00)
        if (horaCita.isBefore(LocalTime.of(8, 0))) {
            System.out.println("Error: La hora mínima de atención es 8:00 AM");
            return false;
        }
        if (horaCita.isAfter(LocalTime.of(16, 0).minusMinutes(1))) {
            System.out.println("Error: La última cita debe comenzar antes de las 4:00 PM");
            return false;
        }

        // 2. Validar disponibilidad del doctor
        boolean doctorDisponible = citas.stream()
                .noneMatch(c ->
                        c.getDoctor().getCodigoDoctor().equals(idDoctor) &&
                                c.getFechaHora().equals(fechaHora) &&
                                (nuevaCita.getId() == null || !c.getId().equals(nuevaCita.getId()))
                );

        if (!doctorDisponible) {
            System.out.println("Error: El doctor ya tiene una cita programada en ese horario");
            return false;
        }

        // 3. Validar disponibilidad del paciente (mismo día)
        boolean pacienteDisponible = citas.stream()
                .noneMatch(c ->
                        c.getPaciente().getCodigoPaciente().equals(idPaciente) &&
                                c.getFechaHora().toLocalDate().equals(fechaCita) &&
                                (nuevaCita.getId() == null || !c.getId().equals(nuevaCita.getId()))
                );

        if (!pacienteDisponible) {
            System.out.println("Error: El paciente ya tiene una cita programada para este día");
            return false;
        }

        // 4. Validar duración mínima de la cita (30 minutos)
        boolean citaProxima = citas.stream()
                .anyMatch(c ->
                        c.getDoctor().getCodigoDoctor().equals(idDoctor) &&
                                Math.abs(Duration.between(c.getFechaHora(), fechaHora).toMinutes()) < 30
                );

        if (citaProxima) {
            System.out.println("Error: Debe haber al menos 30 minutos entre citas del mismo doctor");
            return false;
        }

        // Asignar ID si no tiene
        if (nuevaCita.getId() == null || nuevaCita.getId().isEmpty()) {
            nuevaCita.setId(GeneradorCodigos.generarCodigo("CTI-", 5));
        }

        citas.add(nuevaCita);
        return true;
    }

    // Listar todas las citas
    public List<Cita> listarCitas() {
        return new ArrayList<>(citas);
    }

    // Filtrar citas por fecha
    public List<Cita> filtrarCitasPorFecha(LocalDate fecha) {
        return citas.stream()
                .filter(c -> c.getFechaHora().toLocalDate().equals(fecha))
                .collect(Collectors.toList());
    }

    // Filtrar citas del día actual
    public List<Cita> filtrarCitasDelDia() {
        LocalDate hoy = LocalDate.now();
        return filtrarCitasPorFecha(hoy);
    }

    // Filtrar citas futuras
    public List<Cita> filtrarCitasFuturas() {
        LocalDate hoy = LocalDate.now();
        return citas.stream()
                .filter(c -> c.getFechaHora().toLocalDate().isAfter(hoy))
                .collect(Collectors.toList());
    }

    // Filtrar citas por especialidad
    public List<Cita> filtrarCitasPorEspecialidad(String especialidad) {
        return citas.stream()
                .filter(c -> c.getEspecialidad().equalsIgnoreCase(especialidad))
                .collect(Collectors.toList());
    }

    // Filtrar citas por código de doctor
    public List<Cita> filtrarCitasPorDoctor(String codigoDoctor) {
        return citas.stream()
                .filter(c -> c.getDoctor().getCodigoDoctor().equals(codigoDoctor))
                .collect(Collectors.toList());
    }

    // Cancelar (eliminar) una cita
    public boolean cancelarCita(String idCita) {
        return citas.removeIf(c -> c.getId().equals(idCita));
    }

    // Verificar disponibilidad de horario
    public boolean verificarDisponibilidad(Doctor doctor, LocalDateTime fechaHora) {
        // Verificar que la fecha y hora estén dentro del horario de atención
        LocalTime hora = fechaHora.toLocalTime();
        boolean dentroDeHorario = !hora.isBefore(LocalTime.of(8, 0)) && !hora.isAfter(LocalTime.of(16, 0));

        if (!dentroDeHorario) {
            return false;
        }

        // Verificar que no exista otra cita para el mismo doctor en esa hora
        boolean existeCita = citas.stream()
                .anyMatch(c ->
                        c.getDoctor().getCodigoDoctor().equals(doctor.getCodigoDoctor()) &&
                                c.getFechaHora().equals(fechaHora)
                );

        return !existeCita;
    }
}