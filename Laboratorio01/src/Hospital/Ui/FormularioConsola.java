package Hospital.Ui;

import Hospital.DTO.CitaDTO;
import Hospital.Entity.Cita;
import Hospital.Entity.Doctor;
import Hospital.Entity.Paciente;
import Hospital.Util.Especialidades;
import Hospital.Util.GeneradorCodigos;
import Hospital.Util.Validador;
import Hospital.DTO.DoctorDTO;
import Hospital.DTO.PacienteDTO;
import Hospital.DTO.Convertidor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import static Hospital.Util.GeneradorCodigos.random;

public class FormularioConsola {
    private static final Scanner scanner = new Scanner(System.in);

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

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

        // Mostrar menú de especialidades y solicitar selección
        Especialidades.mostrarMenuEspecialidades();
        int seleccion = 0;
        boolean seleccionValida = false;

        while (!seleccionValida) {
            System.out.print("Seleccione una especialidad (1-" + Especialidades.getEspecialidades().size() + "): ");
            try {
                seleccion = Integer.parseInt(scanner.nextLine());
                seleccionValida = Especialidades.esSeleccionValida(seleccion);

                if (!seleccionValida) {
                    System.out.println("Selección inválida. Por favor, elija un número del menú.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }

        // Asignar la especialidad seleccionada
        dto.setEspecialidad(Especialidades.getEspecialidad(seleccion - 1));
        System.out.println("Especialidad seleccionada: " + dto.getEspecialidad());

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

        // Convertir DTO a entidad (el código se genera automáticamente)
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

    // Método para crear una nueva cita
    public static Cita crearCita(List<Doctor> doctores, List<Paciente> pacientes) {
        System.out.println("\n===== AGENDAR NUEVA CITA =====");

        if (doctores.isEmpty()) {
            System.out.println("Error: No hay doctores registrados en el sistema.");
            return null;
        }

        if (pacientes.isEmpty()) {
            System.out.println("Error: No hay pacientes registrados en el sistema.");
            return null;
        }

        CitaDTO dto = new CitaDTO();


        System.out.println("\n----- Seleccione un doctor -----");
        for (int i = 0; i < doctores.size(); i++) {
            System.out.println((i + 1) + ". " + doctores.get(i));
        }

        int seleccionDoctor = -1;
        while (seleccionDoctor < 0 || seleccionDoctor >= doctores.size()) {
            System.out.print("Seleccione un doctor (1-" + doctores.size() + "): ");
            try {
                seleccionDoctor = Integer.parseInt(scanner.nextLine()) - 1;
                if (seleccionDoctor < 0 || seleccionDoctor >= doctores.size()) {
                    System.out.println("Selección inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }

        Doctor doctorSeleccionado = doctores.get(seleccionDoctor);
        dto.setDoctorId(doctorSeleccionado.getCodigoDoctor());


        System.out.println("\n----- Seleccione un paciente -----");
        for (int i = 0; i < pacientes.size(); i++) {
            System.out.println((i + 1) + ". " + pacientes.get(i));
        }

        int seleccionPaciente = -1;
        while (seleccionPaciente < 0 || seleccionPaciente >= pacientes.size()) {
            System.out.print("Seleccione un paciente (1-" + pacientes.size() + "): ");
            try {
                seleccionPaciente = Integer.parseInt(scanner.nextLine()) - 1;
                if (seleccionPaciente < 0 || seleccionPaciente >= pacientes.size()) {
                    System.out.println("Selección inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }

        Paciente pacienteSeleccionado = pacientes.get(seleccionPaciente);
        dto.setPacienteId(pacienteSeleccionado.getCodigoPaciente());

        dto.setEspecialidad(doctorSeleccionado.getEspecialidad());


        System.out.print("\n¿Es una cita para hoy mismo? (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        boolean citaDelDia = respuesta.equals("S") || respuesta.equals("SI");
        dto.setCitaDelDia(citaDelDia);

        LocalDateTime fechaHoraCita;

        if (citaDelDia) {
            // Para citas del mismo día, solicitar hora exacta
            LocalTime hora = null;
            while (hora == null) {
                System.out.print("Ingrese la hora (formato 24h, HH:mm): ");
                String horaStr = scanner.nextLine();
                try {
                    hora = LocalTime.parse(horaStr, FORMATO_HORA);
                    // Validar que la hora esté dentro del horario de atención (8:00 - 16:00)
                    if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(16, 0))) {
                        System.out.println("El horario de atención es de 8:00 a 16:00.");
                        hora = null;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de hora incorrecto. Use HH:mm (Ej: 14:30)");
                }
            }

            fechaHoraCita = LocalDateTime.of(LocalDate.now(), hora);
            dto.setPacienteAsistio(true); // Si es cita del día, el paciente asiste por defecto
        } else {
            // Para citas futuras, solicitar fecha y asignar hora automáticamente
            LocalDate fecha = null;
            while (fecha == null) {
                System.out.print("Ingrese la fecha de la cita (dd/MM/yyyy): ");
                String fechaStr = scanner.nextLine();
                fecha = Validador.validarFecha(fechaStr);

                if (fecha == null) {
                    System.out.println("Formato de fecha incorrecto. Use dd/MM/yyyy");
                } else if (fecha.isBefore(LocalDate.now())) {
                    System.out.println("La fecha debe ser igual o posterior a hoy.");
                    fecha = null;
                }
            }

            // Asignar hora aleatoria entre 8:00 y 16:00 para citas futuras
            int hora = 8 + random.nextInt(8); // Horas entre 8 y 15
            int minutos = random.nextInt(4) * 15; // Minutos: 0, 15, 30 o 45

            fechaHoraCita = LocalDateTime.of(fecha, LocalTime.of(hora, minutos));
            dto.setPacienteAsistio(false); // Cita futura, aún no asiste
        }

        dto.setFechaHora(fechaHoraCita);

        // Generar un ID para la cita
        dto.setId(GeneradorCodigos.generarCodigo("CTI-", 5));

        // Convertir DTO a entidad
        Cita nuevaCita = Convertidor.convertirACita(dto, doctores, pacientes);

        if (nuevaCita != null) {
              // Agregar la cita a la lista
            System.out.println("\nCita guardada correctamente.");
        } else {
            System.out.println("\nError al agendar la cita. Verifique los datos e intente nuevamente.");
        }

        return nuevaCita;
    }
}
