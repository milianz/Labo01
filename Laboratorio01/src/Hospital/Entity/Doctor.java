package Hospital.Entity;


import Hospital.Util.GeneradorCodigos;

import java.time.LocalDate;

public class Doctor extends Persona{
    private String especialidad;
    private LocalDate fechaReclutamiento;
    private String codigoDoctor; // Formato ZNH-XAX-MD-AX

    // Constructor
    public Doctor(String nombre, String apellido, String dui, LocalDate fechaNacimiento,
                  String especialidad, LocalDate fechaReclutamiento) {
        super(nombre, apellido, dui, fechaNacimiento);
        this.especialidad = especialidad;
        this.fechaReclutamiento = fechaReclutamiento;
        this.codigoDoctor = GeneradorCodigos.generarCodigoDoctor();
    }

    // Constructor adicional que permite especificar el código (para convertidores)
    public Doctor(String nombre, String apellido, String dui, LocalDate fechaNacimiento,
                  String especialidad, LocalDate fechaReclutamiento, String codigoDoctor) {
        super(nombre, apellido, dui, fechaNacimiento);
        this.especialidad = especialidad;
        this.fechaReclutamiento = fechaReclutamiento;
        this.codigoDoctor = codigoDoctor;
    }

    // Getters y setters
    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public LocalDate getFechaReclutamiento() {
        return fechaReclutamiento;
    }

    public void setFechaReclutamiento(LocalDate fechaReclutamiento) {
        this.fechaReclutamiento = fechaReclutamiento;
    }

    public String getCodigoDoctor() {
        return codigoDoctor;
    }

    public void setCodigoDoctor(String codigoDoctor) {
        this.codigoDoctor = codigoDoctor;
    }

    @Override
    public String toString() {
        return "Dr. " + super.getNombreCompleto() + " | Especialidad: " + especialidad +
                " | Código: " + codigoDoctor + " | Fecha reclutamiento: " + fechaReclutamiento;
    }
}
