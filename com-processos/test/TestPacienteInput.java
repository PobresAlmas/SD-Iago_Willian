package test;

import java.io.FileInputStream;
import java.io.InputStream;

import streams.PacienteInputStream;

public class TestPacienteInput {
    public static void test(InputStream fluxo){
        try (PacienteInputStream testPaciente = new PacienteInputStream(fluxo)) {
            testPaciente.recoveryData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (FileInputStream file = new FileInputStream("pacientes.bin")) {
            // test(System.in);
            test(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
