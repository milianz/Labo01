package Hospital.Util;


import java.util.Random;

public class GeneradorCodigos {
    private static final Random random = new Random();
    private static final String LETRAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Genera un código de paciente: ZNP-XXXXX (donde X son números)
    public static String generarCodigoPaciente() {
        StringBuilder codigo = new StringBuilder("ZNP-");

        // Agregar 5 dígitos aleatorios
        for (int i = 0; i < 5; i++) {
            codigo.append(random.nextInt(10));
        }

        return codigo.toString();
    }

    // Genera un código de doctor: ZNH-XAX-MD-AX (donde X son números y A son letras)
    public static String generarCodigoDoctor() {
        StringBuilder codigo = new StringBuilder("ZNH-");

        // Primera parte: XAX
        codigo.append(random.nextInt(10));
        codigo.append(LETRAS.charAt(random.nextInt(LETRAS.length())));
        codigo.append(random.nextInt(10));

        // Segunda parte: MD-AX
        codigo.append("-MD-");
        codigo.append(LETRAS.charAt(random.nextInt(LETRAS.length())));
        codigo.append(random.nextInt(10));

        return codigo.toString();
    }
}
