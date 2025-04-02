package Hospital;

import Hospital.Entity.Doctor;
import Hospital.Entity.Paciente;
import Hospital.Ui.FormularioConsola;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Doctor> doctores = new ArrayList<>();
    private static final List<Paciente> pacientes = new ArrayList<>();

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
                    mostrarDoctores();
                    break;

                case 4:
                    mostrarPacientes();
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
        System.out.println("3. Ver lista de doctores");
        System.out.println("4. Ver lista de pacientes");
        System.out.println("0. Salir");
        System.out.println("=========================================");
        System.out.print("Seleccione una opción: ");
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
}