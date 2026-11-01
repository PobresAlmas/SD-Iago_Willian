package rmi.notificacao.server;

import rmi.notificacao.api.OuvinteRemoto;
import rmi.notificacao.api.ServicoNotificacoes;
import rmi.notificacao.model.CanalNotificacoes;
import rmi.notificacao.model.Notificacao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** O servidor agrega um canal e os clientes remotos inscritos. */
public class ServicoNotificacoesImpl extends UnicastRemoteObject implements ServicoNotificacoes {
    private static final long serialVersionUID = 1L;
    private final transient Map<String, OuvinteRemoto> clientes = new ConcurrentHashMap<>();
    private final transient CanalNotificacoes canal = new CanalNotificacoes();

    public ServicoNotificacoesImpl() throws RemoteException { super(); }

    @Override public String entrar(String clienteJson, OuvinteRemoto ouvinte) throws RemoteException {
        String nome = campo(clienteJson, "nome");
        if (nome.isEmpty() || ouvinte == null) return "{\"ok\":false,\"erro\":\"Cliente ou callback invalido\"}";
        clientes.put(nome, ouvinte);
        return "{\"ok\":true,\"mensagem\":\"Inscrito\",\"clientes\":" + clientes.size() + "}";
    }

    @Override public String sair(String nomeCliente) {
        boolean removido = clientes.remove(nomeCliente) != null;
        return "{\"ok\":" + removido + ",\"clientes\":" + clientes.size() + "}";
    }

    @Override public String publicar(String administradorJson, String notificacaoJson) throws RemoteException {
        String administrador = campo(administradorJson, "nome");
        String tipo = campo(notificacaoJson, "tipo");
        String mensagem = campo(notificacaoJson, "mensagem");
        if (administrador.isEmpty() || mensagem.isEmpty()) return "{\"ok\":false,\"erro\":\"Dados invalidos\"}";
        Notificacao notificacao = new Notificacao(tipo, mensagem);
        canal.registrar(notificacao);
        for (Map.Entry<String, OuvinteRemoto> cliente : clientes.entrySet()) {
            try { cliente.getValue().receberNotificacao(notificacao.toJson()); }
            catch (RemoteException desconectado) { clientes.remove(cliente.getKey(), cliente.getValue()); }
        }
        return "{\"ok\":true,\"publicadoPor\":\"" + escapar(administrador)
                + "\",\"destinatarios\":" + clientes.size() + "}";
    }

    @Override public String listarHistorico() {
        StringBuilder json = new StringBuilder("[");
        boolean primeiro = true;
        for (Notificacao n : canal.getHistorico()) {
            if (!primeiro) json.append(',');
            json.append(n.toJson()); primeiro = false;
        }
        return json.append(']').toString();
    }

    @Override public String status() { return "{\"clientesConectados\":" + clientes.size() + "}"; }

    private static String campo(String json, String chave) {
        if (json == null) return "";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\\"" + chave + "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"").matcher(json);
        return m.find() ? m.group(1).replace("\\\"", "\"").replace("\\\\", "\\") : "";
    }
    private static String escapar(String s) { return s.replace("\\", "\\\\").replace("\"", "\\\""); }
}
