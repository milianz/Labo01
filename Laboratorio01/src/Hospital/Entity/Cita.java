package Hospital.Entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cita {
    private String id;
    private Doctor doctor;
    private Paciente paciente;
    private String especialidad;
    private LocalDateTime fechaHora;
    private boolean citaDelDia;
    private boolean pacienteAsistio;

    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Cita(String id, Doctor doctor, Paciente paciente, String especialidad, LocalDateTime fechaHora,
                boolean citaDelDia, boolean pacienteAsistio) {
        this.id = id;
        this.doctor = doctor;
        this.paciente = paciente;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.citaDelDia = citaDelDia;
        this.pacienteAsistio = pacienteAsistio;
    }

    // Getters y setters
    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public Doctor getDoctor() { return doctor; }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Paciente getPaciente() { return paciente; }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getEspecialidad() { return especialidad; }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public LocalDateTime getFechaHora() { return fechaHora; }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public boolean isCitaDelDia() {
        return citaDelDia;
    }

    public void setCitaDelDia(boolean citaDelDia) {
        this.citaDelDia = citaDelDia;
    }

    public boolean isPacienteAsistio() {
        return pacienteAsistio;
    }

    public void setPacienteAsistio(boolean pacienteAsistio) {
        this.pacienteAsistio = pacienteAsistio;
    }

    @Override
    public String toString() {
        String estadoAsistencia = pacienteAsistio ? "Asistió" : (fechaHora.isAfter(LocalDateTime.now()) ? "Pendiente" : "No asistió");
        String tipoCita = citaDelDia ? "Cita del día" : "Cita agendada";

        return "ID: " + id +
                " | Paciente: " + paciente.getNombreCompleto() +
                " | Doctor: Dr. " + doctor.getNombreCompleto() +
                " | Especialidad: " + especialidad +
                " | Fecha y hora: " + fechaHora.format(FORMATO_FECHA_HORA) +
                " | " + tipoCita +
                " | Estado: " + estadoAsistencia;
    }
}