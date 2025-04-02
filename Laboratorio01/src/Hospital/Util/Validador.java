package Hospital.Util;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class Validador {
    private static final Pattern PATRON_DUI = Pattern.compile("^\\d{8}-\\d$");
    private static final Pattern PATRON_CODIGO_DOCTOR = Pattern.compile("^ZNH-\\d[A-Z]\\d-MD-[A-Z]\\d$");
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Pattern PATRON_CODIGO_PACIENTE = Pattern.compile("^ZNP-\\d{6}$"); // Por ejemplo ZNP-123456

    public static boolean validarCodigoPaciente(String codigo) {
        return PATRON_CODIGO_PACIENTE.matcher(codigo).matches();
    }

    public static boolean validarDUI(String dui) {
        return PATRON_DUI.matcher(dui).matches();
    }

    public static boolean validarCodigoDoctor(String codigo) {
        return PATRON_CODIGO_DOCTOR.matcher(codigo).matches();
    }

    public static LocalDate validarFecha(String fecha) {
        try {
            return LocalDate.parse(fecha, FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean validarCampoObligatorio(String campo) {
        return campo != null && !campo.trim().isEmpty();
    }
}
