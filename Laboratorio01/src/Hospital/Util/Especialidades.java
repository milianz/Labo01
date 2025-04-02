package Hospital.Util;

import java.util.ArrayList;
import java.util.List;

public class Especialidades {
    // Lista predefinida de especialidades
    private static final List<String> LISTA_ESPECIALIDADES = new ArrayList<>();

    // Inicializar la lista de especialidades
    static {
        LISTA_ESPECIALIDADES.add("Cardiología");
        LISTA_ESPECIALIDADES.add("Dermatología");
        LISTA_ESPECIALIDADES.add("Endocrinología");
        LISTA_ESPECIALIDADES.add("Gastroenterología");
        LISTA_ESPECIALIDADES.add("Geriatría");
        LISTA_ESPECIALIDADES.add("Ginecología");
        LISTA_ESPECIALIDADES.add("Hematología");
        LISTA_ESPECIALIDADES.add("Infectología");
        LISTA_ESPECIALIDADES.add("Medicina Familiar");
        LISTA_ESPECIALIDADES.add("Medicina Interna");
        LISTA_ESPECIALIDADES.add("Nefrología");
        LISTA_ESPECIALIDADES.add("Neurología");
        LISTA_ESPECIALIDADES.add("Oftalmología");
        LISTA_ESPECIALIDADES.add("Oncología");
        LISTA_ESPECIALIDADES.add("Ortopedia");
        LISTA_ESPECIALIDADES.add("Pediatría");
        LISTA_ESPECIALIDADES.add("Psiquiatría");
        LISTA_ESPECIALIDADES.add("Traumatología");
        LISTA_ESPECIALIDADES.add("Urología");
    }

    // Método para obtener todas las especialidades
    public static List<String> getEspecialidades() {
        return new ArrayList<>(LISTA_ESPECIALIDADES);
    }

    // Método para obtener una especialidad por su índice
    public static String getEspecialidad(int indice) {
        if (indice >= 0 && indice < LISTA_ESPECIALIDADES.size()) {
            return LISTA_ESPECIALIDADES.get(indice);
        }
        return null;
    }

    // Método para mostrar el menú de especialidades
    public static void mostrarMenuEspecialidades() {
        System.out.println("\n=== ESPECIALIDADES DISPONIBLES ===");
        for (int i = 0; i < LISTA_ESPECIALIDADES.size(); i++) {
            System.out.println((i + 1) + ". " + LISTA_ESPECIALIDADES.get(i));
        }
        System.out.println("===================================");
    }

    // Método para validar la selección
    public static boolean esSeleccionValida(int seleccion) {
        return seleccion > 0 && seleccion <= LISTA_ESPECIALIDADES.size();
    }
}
