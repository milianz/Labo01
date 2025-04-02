package Hospital.DTO;

public class DoctorDTO extends PersonaDTO {
    private String especialidad;
    private String fechaReclutamiento;
    private String codigoDoctor;

    // Getters y setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getFechaReclutamiento() {
        return fechaReclutamiento;
    }

    public void setFechaReclutamiento(String fechaReclutamiento) {
        this.fechaReclutamiento = fechaReclutamiento;
    }

    public String getCodigoDoctor() {
        return codigoDoctor;
    }

    public void setCodigoDoctor(String codigoDoctor) {
        this.codigoDoctor = codigoDoctor;
    }
}
