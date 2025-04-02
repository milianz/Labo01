package Hospital.Ui;

import Hospital.Entity.Doctor;
import Hospital.Entity.Paciente;
import Hospital.Util.Validador;
import Hospital.DTO.DoctorDTO;
import Hospital.DTO.PacienteDTO;
import Hospital.DTO.Convertidor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FormularioConsola {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Metodo para crear un nuevo doctor usando DTO
    public static Doctor crearDoctor() {
        System.out.println("\n===== AGREGAR NUEVO DOCTOR =====");

        DoctorDTO dto = new DoctorDTO();

        // Solicitar nombre
        System.out.print("Nombre: ");
        dto.setNombre(scanner.nextLine());
        while (!Validador.validarCampoObligatorio(dto.getNombre())) {
            System.out.println("El nombre es obligatorio.");
            System.out.print("Nombre: ");
            dto.setNombre(scanner.nextLine());
        }

        // Solicitar apellido
        System.out.print("Apellido: ");
        dto.setApellido(scanner.nextLine());
        while (!Validador.validarCampoObligatorio(dto.getApellido())) {
            System.out.println("El apellido es obligatorio.");
            System.out.print("Apellido: ");
            dto.setApellido(scanner.nextLine());
        }

        // Solicitar DUI
        System.out.print("DUI (formato 12345678-9): ");
        dto.setDui(scanner.nextLine());
        while (!Validador.validarDUI(dto.getDui())) {
            System.out.println("DUI inválido. Debe tener el formato 12345678-9.");
            System.out.print("DUI: ");
            dto.setDui(scanner.nextLine());
        }

        // Solicitar fecha de nacimiento
        dto.setFechaNacimiento(solicitarFechaComoCadena("Fecha de nacimiento (dd/MM/yyyy): "));

        // Solicitar especialidad
        System.out.print("Especialidad: ");
        dto.setEspecialidad(scanner.nextLine());
        while (!Validador.validarCampoObligatorio(dto.getEspecialidad())) {
            System.out.println("La especialidad es obligatoria.");
            System.out.print("Especialidad: ");
            dto.setEspecialidad(scanner.nextLine());
        }

        // Solicitar fecha de reclutamiento
        dto.setFechaReclutamiento(solicitarFechaComoCadena("Fecha de reclutamiento (dd/MM/yyyy): "));

        // Validar que la fecha de reclutamiento sea posterior a la fecha de nacimiento + 18 años
        LocalDate fechaNacimiento = Validador.validarFecha(dto.getFechaNacimiento());
        LocalDate fechaReclutamiento = Validador.validarFecha(dto.getFechaReclutamiento());

        while (fechaReclutamiento.isBefore(fechaNacimiento.plusYears(18))) {
            System.out.println("La fecha de reclutamiento debe ser al menos 18 años después de la fecha de nacimiento.");
            dto.setFechaReclutamiento(solicitarFechaComoCadena("Fecha de reclutamiento (dd/MM/yyyy): "));
            fechaReclutamiento = Validador.validarFecha(dto.getFechaReclutamiento());
        }


        // Convertir DTO a entidad
        Doctor doctor = Convertidor.convertirADoctor(dto);
        System.out.println("Código generado automáticamente: " + doctor.getCodigoDoctor());
        return doctor;
    }

    // Metodo para crear un nuevo paciente usando DTO
    public static Paciente crearPaciente() {
        System.out.println("\n===== AGREGAR NUEVO PACIENTE =====");

        PacienteDTO dto = new PacienteDTO();

        // Solicitar nombre
        System.out.print("Nombre: ");
        dto.setNombre(scanner.nextLine());
        while (!Validador.validarCampoObligatorio(dto.getNombre())) {
            System.out.println("El nombre es obligatorio.");
            System.out.print("Nombre: ");
            dto.setNombre(scanner.nextLine());
        }

        // Solicitar apellido
        System.out.print("Apellido: ");
        dto.setApellido(scanner.nextLine());
        while (!Validador.validarCampoObligatorio(dto.getApellido())) {
            System.out.println("El apellido es obligatorio.");
            System.out.print("Apellido: ");
            dto.setApellido(scanner.nextLine());
        }

        // Solicitar fecha de nacimiento
        dto.setFechaNacimiento(solicitarFechaComoCadena("Fecha de nacimiento (dd/MM/yyyy): "));

        // Determinar si es menor de edad
        LocalDate fechaNacimiento = Validador.validarFecha(dto.getFechaNacimiento());
        boolean esMenor = LocalDate.now().getYear() - fechaNacimiento.getYear() < 18;

        // Solicitar DUI (solo si no es menor de edad)
        if (!esMenor) {
            System.out.print("DUI (formato 12345678-9): ");
            dto.setDui(scanner.nextLine());
            while (!Validador.validarDUI(dto.getDui())) {
                System.out.println("DUI inválido. Debe tener el formato 12345678-9.");
                System.out.print("DUI: ");
                dto.setDui(scanner.nextLine());
            }
        } else {
            System.out.println("Paciente menor de edad detectado. Se asignará DUI: 00000000-0");
            dto.setDui("00000000-0");
        }

        // Solicitar síntomas
        System.out.print("Síntomas: ");
        dto.setSintomas(scanner.nextLine());

        // Convertir DTO a entidad
        Paciente paciente = Convertidor.convertirAPaciente(dto);
        System.out.println("Código de expediente generado automáticamente: " + paciente.getCodigoPaciente());
        return paciente;
    }

    // Metodo auxiliar para validar y solicitar fechas
    private static String solicitarFechaComoCadena(String mensaje) {
        String fechaStr = null;
        LocalDate fecha = null;

        while (fecha == null) {
            System.out.print(mensaje);
            fechaStr = scanner.nextLine();
            fecha = Validador.validarFecha(fechaStr);

            if (fecha == null) {
                System.out.println("Formato de fecha inválido. Use dd/MM/yyyy.");
            }
        }

        return fechaStr;
    }
}
