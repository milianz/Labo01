package Hospital.Entity;

import Hospital.Util.GeneradorCodigos;

import java.time.LocalDate;
import java.time.Period;

public class Paciente extends Persona {
    private String sintomas;
    private String codigoPaciente; // Formato ZNP-XXXXX
    private static final String DUI_MENOR = "00000000-0";

    // Constructor
    public Paciente(String nombre, String apellido, String dui, LocalDate fechaNacimiento, String sintomas) {
        super(nombre, apellido, esMenorEdad(fechaNacimiento) ? DUI_MENOR : dui, fechaNacimiento);
        this.sintomas = sintomas;
        this.codigoPaciente = GeneradorCodigos.generarCodigoPaciente();
    }

    // Constructor adicional que permite especificar el código (para convertidores)
    public Paciente(String nombre, String apellido, String dui, LocalDate fechaNacimiento,
                    String sintomas, String codigoPaciente) {
        super(nombre, apellido, esMenorEdad(fechaNacimiento) ? DUI_MENOR : dui, fechaNacimiento);
        this.sintomas = sintomas;
        this.codigoPaciente = codigoPaciente;
    }

    // Metodo estático para verificar si es menor de edad
    private static boolean esMenorEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears() < 18;
    }

    // Getters y setters
    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(String codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }


    @Override
    public String toString() {
        return super.getNombreCompleto() + " | DUI: " + getDui() +
                " | Edad: " + getEdad() + " años | Código: " + codigoPaciente +
                " | Síntomas: " + sintomas;
    }
}
