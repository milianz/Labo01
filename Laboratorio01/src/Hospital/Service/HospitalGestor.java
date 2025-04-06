package Hospital.Service;

import Hospital.Entity.Doctor;
import Hospital.Entity.Paciente;

import java.util.ArrayList;
import java.util.List;

public class HospitalGestor {
        private List<Doctor> doctores;
        private List<Paciente> pacientes;

        public HospitalGestor() {
            doctores = new ArrayList<>();
            pacientes = new ArrayList<>();
        }

        // Verificar si un DUI ya existe en el sistema
        public boolean duiExiste(String dui) {
            // Verificar entre doctores
            for (Doctor doctor : doctores) {
                if (doctor.getDui().equals(dui)) {
                    return true;
                }
            }

            // Verificar entre pacientes (excluyendo DUI de menores de edad)
            for (Paciente paciente : pacientes) {
                if (!paciente.getDui().equals("00000000-0") && paciente.getDui().equals(dui)) {
                    return true;
                }
            }

            return false;
        }

        // Métodos para agregar doctores y pacientes con validación de DUI
        public boolean agregarDoctor(Doctor doctor) {
            // Verificar que el DUI no existe (excepto si es el mismo doctor)
            if (duiExiste(doctor.getDui())) {
                return false;
            }

            doctores.add(doctor);
            return true;
        }

        public boolean agregarPaciente(Paciente paciente) {
            // Si es un menor de edad (DUI 00000000-0), no es necesario validar el DUI
            if (paciente.getDui().equals("00000000-0") || !duiExiste(paciente.getDui())) {
                pacientes.add(paciente);
                return true;
            }
            return false;
        }

        // Getters para las listas
        public List<Doctor> getDoctores() {
            return doctores;
        }

        public List<Paciente> getPacientes() {
            return pacientes;
        }
}

