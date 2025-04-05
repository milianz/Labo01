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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            return false;
        }

        LocalDateTime fechaHora = nuevaCita.getFechaHora();
        LocalDate fechaCita = fechaHora.toLocalDate();
        LocalTime horaCita = fechaHora.toLocalTime();
        String idPaciente = nuevaCita.getPaciente().getCodigoPaciente();
        String idDoctor = nuevaCita.getDoctor().getCodigoDoctor();

        // 1. Validar horario de atención (8:00-16:00)
        if (horaCita.isBefore(LocalTime.of(8, 0)) ||
                horaCita.isAfter(LocalTime.of(16, 0).minusMinutes(1))) {
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
            return false;
        }

        // 4. Validar duración mínima de la cita (30 minutos)
        boolean citaProxima = citas.stream()
                .anyMatch(c ->
                        c.getDoctor().getCodigoDoctor().equals(idDoctor) &&
                                Math.abs(Duration.between(c.getFechaHora(), fechaHora).toMinutes()) < 30
                );

        if (citaProxima) {
            return false;
        }

        // Asignar ID si no tiene
        if (nuevaCita.getId() == null || nuevaCita.getId().isEmpty()) {
            nuevaCita.setId(GeneradorCodigos.generarCodigo("CTI-", 5));
        }

        citas.add(nuevaCita);
        return true;
    }

    // Método para sugerir una nueva hora para cita del mismo día
    public LocalDateTime sugerirNuevaHoraMismoDia(Doctor doctor, LocalDate fecha) {
        LocalTime horaInicio = LocalTime.of(8, 0);
        LocalTime horaFin = LocalTime.of(16, 0);

        // Obtener todas las citas del doctor para ese día
        List<LocalDateTime> horasOcupadas = citas.stream()
                .filter(c -> c.getDoctor().getCodigoDoctor().equals(doctor.getCodigoDoctor()) &&
                        c.getFechaHora().toLocalDate().equals(fecha))
                .map(Cita::getFechaHora)
                .sorted()
                .collect(Collectors.toList());

        // Si no hay citas, sugerir la primera hora del día
        if (horasOcupadas.isEmpty()) {
            return LocalDateTime.of(fecha, horaInicio);
        }

        // Primero verificar si hay hueco a primera hora (8:00)
        LocalTime primeraHoraOcupada = horasOcupadas.get(0).toLocalTime();
        if (Duration.between(horaInicio, primeraHoraOcupada).toMinutes() >= 30) {
            return LocalDateTime.of(fecha, horaInicio);
        }

        // Buscar huecos entre citas (mínimo 30 minutos)
        for (int i = 0; i < horasOcupadas.size() - 1; i++) {
            LocalTime horaActual = horasOcupadas.get(i).toLocalTime();
            LocalTime horaSiguiente = horasOcupadas.get(i + 1).toLocalTime();

            long minutosEntre = Duration.between(horaActual, horaSiguiente).toMinutes();
            if (minutosEntre >= 60) { // Necesitamos al menos 60 minutos para poner una cita de 30 min
                // Sugerir 30 minutos después de la cita actual
                return LocalDateTime.of(fecha, horaActual.plusMinutes(30));
            }
        }

        // Si no hay huecos, verificar si hay espacio al final del día
        LocalTime ultimaHoraOcupada = horasOcupadas.get(horasOcupadas.size() - 1).toLocalTime();
        if (Duration.between(ultimaHoraOcupada, horaFin).toMinutes() >= 30) {
            // Sugerir 30 minutos después de la última cita
            return LocalDateTime.of(fecha, ultimaHoraOcupada.plusMinutes(30));
        }

        // Si no hay espacio en este día, buscar el siguiente día
        return sugerirNuevaHoraOtroDia(doctor, fecha.plusDays(1));
    }

    // Método para sugerir hora en otro día
    public LocalDateTime sugerirNuevaHoraOtroDia(Doctor doctor, LocalDate fecha) {
        // Para otro día, comenzamos a primera hora (8:00)
        return LocalDateTime.of(fecha, LocalTime.of(8, 0));
    }

    // Método para procesar una cita con sugerencias de horario alternativo
    public Map<String, Object> procesarCita(Cita nuevaCita) {
        Map<String, Object> resultado = new HashMap<>();

        boolean citaAgendada = agendarCita(nuevaCita);
        resultado.put("exito", citaAgendada);

        if (citaAgendada) {
            resultado.put("mensaje", "Cita agendada exitosamente");
        } else {
            // Si la cita es para hoy, sugerir otra hora el mismo día
            // Si no, sugerir para mañana
            LocalDate fechaCita = nuevaCita.getFechaHora().toLocalDate();
            boolean esHoy = fechaCita.equals(LocalDate.now());

            LocalDateTime horaSugerida;
            if (esHoy) {
                horaSugerida = sugerirNuevaHoraMismoDia(nuevaCita.getDoctor(), fechaCita);
            } else {
                horaSugerida = sugerirNuevaHoraOtroDia(nuevaCita.getDoctor(), fechaCita.plusDays(1));
            }

            resultado.put("mensaje", "No fue posible agendar la cita");
            resultado.put("horaSugerida", horaSugerida);
        }

        return resultado;
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