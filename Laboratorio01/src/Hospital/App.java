package Hospital;

import Hospital.Entity.Cita;
import Hospital.Entity.Doctor;
import Hospital.Entity.Paciente;
import Hospital.Service.CitaService;
import Hospital.Ui.FormularioConsola;
import Hospital.Util.Especialidades;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Doctor> doctores = new ArrayList<>();
    private static final List<Paciente> pacientes = new ArrayList<>();
    private static final CitaService citaService = new CitaService();
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    Doctor nuevoDoctor = FormularioConsola.crearDoctor();
                    doctores.add(nuevoDoctor);
                    System.out.println("\nDoctor registrado exitosamente:");
                    System.out.println(nuevoDoctor);
                    break;

                case 2:
                    Paciente nuevoPaciente = FormularioConsola.crearPaciente();
                    pacientes.add(nuevoPaciente);
                    System.out.println("\nPaciente registrado exitosamente:");
                    System.out.println(nuevoPaciente);
                    break;

                case 3:
                    Cita nuevaCita = FormularioConsola.crearCita(doctores, pacientes);
                    if (nuevaCita != null) {
                        boolean agendada = citaService.agendarCita(nuevaCita);
                        if (!agendada) {
                            System.out.println("Error: No se pudo agendar la cita. Conflicto de horario.");
                        }
                    }
                    break;
                case 4:
                    mostrarSubmenuCitas();
                    break;
                case 5:
                    mostrarDoctores();
                    break;
                case 6:
                    mostrarPacientes();
                    break;
                case 7:
                    buscarCitasPorDoctor();
                    break;

                case 8:
                    cancelarCita();
                    break;
                case 9:
                    mostrarMensajeMundo();
                    break;

                case 0:
                    System.out.println("¡Gracias por usar el sistema! ¡Dr. Mundo salva vidas!");
                    break;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                    break;
            }

            if (opcion != 0) {
                System.out.println("\nPresione ENTER para continuar...");
                scanner.nextLine();
            }

        } while (opcion != 0);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n======= HOSPITAL NACIONAL DE ZAUN =======");
        System.out.println("1. Agregar nuevo doctor");
        System.out.println("2. Agregar nuevo paciente");
        System.out.println("3. Agendar nueva cita");
        System.out.println("4. Ver lista de citas");
        System.out.println("5. Ver lista de doctores");
        System.out.println("6. Ver lista de pacientes");
        System.out.println("7. Buscar citas por doctor");
        System.out.println("8. Cancelar cita");
        System.out.println("9. ¡Mundo salva vidas!");
        System.out.println("0. Salir");
        System.out.println("=========================================");
        System.out.print("Seleccione una opción: ");
    }

    private static void agendarNuevaCita() {
        Cita nuevaCita = FormularioConsola.crearCita(doctores, pacientes);
        if (nuevaCita != null) {
            boolean agendada = citaService.agendarCita(nuevaCita);
            if (agendada) {
                System.out.println("\nCita agendada exitosamente!");
            } else {
                System.out.println("\nError: No se pudo agendar la cita. Conflicto de horario.");
            }
        }
    }

    private static void mostrarSubmenuCitas() {
        System.out.println("\n===== LISTADO DE CITAS =====");
        System.out.println("1. Ver todas las citas");
        System.out.println("2. Ver citas del día");
        System.out.println("3. Ver citas futuras");
        System.out.println("4. Filtrar por fecha específica");
        System.out.println("5. Filtrar por especialidad");
        System.out.println("0. Volver al menú principal");
        System.out.println("============================");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();

        switch (opcion) {
            case 1:
                mostrarCitas(citaService.listarCitas(), "TODAS LAS CITAS");
                break;

            case 2:
                mostrarCitas(citaService.filtrarCitasDelDia(), "CITAS DEL DÍA");
                break;

            case 3:
                mostrarCitas(citaService.filtrarCitasFuturas(), "CITAS FUTURAS");
                break;

            case 4:
                System.out.print("Ingrese la fecha (dd/MM/yyyy): ");
                String fechaStr = scanner.nextLine();
                try {
                    LocalDate fecha = LocalDate.parse(fechaStr, FORMATO_FECHA);
                    mostrarCitas(citaService.filtrarCitasPorFecha(fecha), "CITAS PARA EL " + fechaStr);
                } catch (Exception e) {
                    System.out.println("Fecha inválida. Use el formato dd/MM/yyyy");
                }
                break;

            case 5:
                Especialidades.mostrarMenuEspecialidades();
                System.out.print("Seleccione una especialidad: ");
                int seleccion = leerOpcion() - 1;
                if (seleccion >= 0 && seleccion < Especialidades.getEspecialidades().size()) {
                    String especialidad = Especialidades.getEspecialidad(seleccion);
                    mostrarCitas(citaService.filtrarCitasPorEspecialidad(especialidad),
                            "CITAS DE " + especialidad.toUpperCase());
                } else {
                    System.out.println("Selección inválida.");
                }
                break;

            case 0:
                return;

            default:
                System.out.println("Opción no válida.");
                break;
        }
    }

    private static void buscarCitasPorDoctor() {
        if (doctores.isEmpty()) {
            System.out.println("No hay doctores registrados.");
            return;
        }

        System.out.println("\n===== BUSCAR CITAS POR DOCTOR =====");
        mostrarDoctores();
        System.out.print("\nSeleccione un doctor (1-" + doctores.size() + "): ");
        int seleccion = leerOpcion() - 1;

        if (seleccion >= 0 && seleccion < doctores.size()) {
            Doctor doctor = doctores.get(seleccion);
            List<Cita> citasDoctor = citaService.filtrarCitasPorDoctor(doctor.getCodigoDoctor());
            mostrarCitas(citasDoctor, "CITAS DEL DR. " + doctor.getNombreCompleto().toUpperCase());
        } else {
            System.out.println("Selección inválida.");
        }
    }

    private static void cancelarCita() {
        List<Cita> citas = citaService.listarCitas();
        if (citas.isEmpty()) {
            System.out.println("No hay citas para cancelar.");
            return;
        }

        System.out.println("\n===== CANCELAR CITA =====");
        mostrarCitas(citas, "CITAS ACTIVAS");
        System.out.print("\nIngrese el ID de la cita a cancelar: ");
        String idCita = scanner.nextLine();

        boolean cancelada = citaService.cancelarCita(idCita);
        if (cancelada) {
            System.out.println("Cita cancelada exitosamente.");
        } else {
            System.out.println("No se encontró una cita con ese ID.");
        }
    }

    private static void mostrarCitas(List<Cita> citas, String titulo) {
        System.out.println("\n===== " + titulo + " =====");
        if (citas.isEmpty()) {
            System.out.println("No hay citas para mostrar.");
        } else {
            citas.forEach(System.out::println);
        }
    }

    private static int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void mostrarDoctores() {
        System.out.println("\n===== LISTA DE DOCTORES =====");

        if (doctores.isEmpty()) {
            System.out.println("No hay doctores registrados.");
        } else {
            for (int i = 0; i < doctores.size(); i++) {
                System.out.println((i + 1) + ". " + doctores.get(i));
            }
        }
    }

    private static void mostrarPacientes() {
        System.out.println("\n===== LISTA DE PACIENTES =====");

        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
        } else {
            for (int i = 0; i < pacientes.size(); i++) {
                System.out.println((i + 1) + ". " + pacientes.get(i));
            }
        }
    }

    private static void mostrarMensajeMundo(){
        System.out.println("============================= ");
        System.out.println("=     MUNDO SALVA VIDAS     = ");
        System.out.println("============================= ");
        System.out.println("");
        System.out.println("(Este botón no sirve de nada pero se ve bonito)");
    }
}