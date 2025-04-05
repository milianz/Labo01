package Hospital.DTO;

import Hospital.Entity.Cita;
import Hospital.Entity.Doctor;
import Hospital.Entity.Paciente;
import Hospital.Entity.Persona;
import Hospital.Util.GeneradorCodigos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    // Convertir CitaDTO a Cita
    public static Cita convertirACita(CitaDTO dto, List<Doctor> doctores, List<Paciente> pacientes) {
        // Buscar doctor y paciente por ID
        Doctor doctor = null;
        for (Doctor doc : doctores) {
            if (doc.getCodigoDoctor().equals(dto.getDoctorId())) {
                doctor = doc;
                break;
            }
        }

        Paciente paciente = null;
        for (Paciente pac : pacientes) {
            if (pac.getCodigoPaciente().equals(dto.getPacienteId())) {
                paciente = pac;
                break;
            }
        }

        if (doctor == null || paciente == null) {
            return null; // No se encontró doctor o paciente
        }

        String id = (dto.getId() != null && !dto.getId().isEmpty())
                ? dto.getId()
                : GeneradorCodigos.generarCodigo("CTI", 5);

        return new Cita(
                id,
                doctor,
                paciente,
                dto.getEspecialidad(),
                dto.getFechaHora(),
                dto.isCitaDelDia(),
                dto.isPacienteAsistio()
        );
    }

    // Convertir Cita a CitaDTO
    public static CitaDTO convertirACitaDTO(Cita cita) {
        return new CitaDTO(
                cita.getId(),
                cita.getDoctor().getCodigoDoctor(),
                cita.getPaciente().getCodigoPaciente(),
                cita.getEspecialidad(),
                cita.getFechaHora(),
                cita.isCitaDelDia(),
                cita.isPacienteAsistio()
        );
    }
}