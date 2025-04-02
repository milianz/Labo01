package Hospital.DTO;

import Hospital.Entity.Doctor;
import Hospital.Entity.Paciente;
import Hospital.Entity.Persona;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Convertidor {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Convertir PersonaDTO a Persona
    public static Persona convertirAPersona(PersonaDTO dto) {
        LocalDate fechaNac = LocalDate.parse(dto.getFechaNacimiento(), FORMATO_FECHA);
        return new Persona(dto.getNombre(), dto.getApellido(), dto.getDui(), fechaNac);
    }

    // Convertir DoctorDTO a Doctor
    public static Doctor convertirADoctor(DoctorDTO dto) {
        LocalDate fechaNac = LocalDate.parse(dto.getFechaNacimiento(), FORMATO_FECHA);
        LocalDate fechaRec = LocalDate.parse(dto.getFechaReclutamiento(), FORMATO_FECHA);

        if (dto.getCodigoDoctor() != null && !dto.getCodigoDoctor().isEmpty()) {
            // Si ya tiene código, usar el existente
            return new Doctor(
                    dto.getNombre(),
                    dto.getApellido(),
                    dto.getDui(),
                    fechaNac,
                    dto.getEspecialidad(),
                    fechaRec,
                    dto.getCodigoDoctor()
            );
        } else {
            // Si no tiene código, generar uno nuevo
            return new Doctor(
                    dto.getNombre(),
                    dto.getApellido(),
                    dto.getDui(),
                    fechaNac,
                    dto.getEspecialidad(),
                    fechaRec
            );
        }
    }

    // Convertir PacienteDTO a Paciente
    public static Paciente convertirAPaciente(PacienteDTO dto) {
        LocalDate fechaNac = LocalDate.parse(dto.getFechaNacimiento(), FORMATO_FECHA);

        if (dto.getCodigoPaciente() != null && !dto.getCodigoPaciente().isEmpty()) {
            // Si ya tiene código, usar el existente
            return new Paciente(
                    dto.getNombre(),
                    dto.getApellido(),
                    dto.getDui(),
                    fechaNac,
                    dto.getSintomas(),
                    dto.getCodigoPaciente()
            );
        } else {
            // Si no tiene código, generar uno nuevo
            return new Paciente(
                    dto.getNombre(),
                    dto.getApellido(),
                    dto.getDui(),
                    fechaNac,
                    dto.getSintomas()
            );
        }
    }
}
