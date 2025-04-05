package Hospital.DTO;

import java.time.LocalDateTime;

public class CitaDTO {
    private String id;
    private String doctorId;
    private String pacienteId;
    private String especialidad;
    private LocalDateTime fechaHora;
    private boolean citaDelDia;
    private boolean pacienteAsistio;

    // Constructor vacío para crear objetos desde formularios
    public CitaDTO() {
    }

    // Constructor completo
    public CitaDTO(String id, String doctorId, String pacienteId, String especialidad,
                   LocalDateTime fechaHora, boolean citaDelDia, boolean pacienteAsistio) {
        this.id = id;
        this.doctorId = doctorId;
        this.pacienteId = pacienteId;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.citaDelDia = citaDelDia;
        this.pacienteAsistio = pacienteAsistio;
    }

    // Getters y setters
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getDoctorId() {

        return doctorId;
    }

    public void setDoctorId(String doctorId) {

        this.doctorId = doctorId;
    }

    public String getPacienteId() {

        return pacienteId;
    }

    public void setPacienteId(String pacienteId) {

        this.pacienteId = pacienteId;
    }

    public String getEspecialidad() {

        return especialidad;
    }

    public void setEspecialidad(String especialidad) {

        this.especialidad = especialidad;
    }

    public LocalDateTime getFechaHora() {

        return fechaHora;
    }

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
}