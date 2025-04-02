package Hospital.DTO;

public class PacienteDTO extends PersonaDTO {
    private String sintomas;
    private String codigoPaciente;

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
}
