package rmi.notificacao.model;

import java.io.Serializable;

public class Notificacao implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String tipo;
    private final String mensagem;
    private final long timestamp;

    public Notificacao(String tipo, String mensagem) {
        this.tipo = tipo == null || tipo.trim().isEmpty() ? "NOTIFICACAO" : tipo.trim().toUpperCase();
        if (mensagem == null || mensagem.trim().isEmpty()) throw new IllegalArgumentException("Mensagem obrigatoria.");
        this.mensagem = mensagem.trim();
        this.timestamp = System.currentTimeMillis();
    }

    public String getTipo() { return tipo; }
    public String getMensagem() { return mensagem; }
    public long getTimestamp() { return timestamp; }

    public String toJson() {
        return "{\"tipo\":\"" + escape(tipo) + "\",\"mensagem\":\"" + escape(mensagem)
                + "\",\"timestamp\":" + timestamp + "}";
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }
}
