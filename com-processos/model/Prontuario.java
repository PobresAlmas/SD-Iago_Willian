package model;

public class Prontuario {
    
    private final int idPaciente;
    private String remedios;
    private String exames;

    public Prontuario(int idPaciente, String remedios, String exames){
        this.idPaciente = idPaciente;
        this.remedios = remedios;
        this.exames = exames;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public String getRemedios() {
        return remedios;
    }

    public void setRemedios(String remedios) {
        this.remedios = remedios;
    }

    public String getExames() {
        return exames;
    }

    public void setExames(String exames) {
        this.exames = exames;
    }
}
