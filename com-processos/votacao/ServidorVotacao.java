package votacao;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class ServidorVotacao {
    private static final int TCP_PORT = 6001;
    private static final int MULTICAST_PORT = 6002;
    private static final String MULTICAST_IP = "230.0.0.2";

    private static List<Candidato> candidatos = Collections.synchronizedList(new ArrayList<>());
    private static volatile boolean votacaoAberta = true;
    private static DatagramSocket udpSocket;

    public static void main(String[] args) {
        candidatos.add(new Candidato(1, "Ada Lovelace"));
        candidatos.add(new Candidato(2, "Alan Turing"));

        try {
            udpSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException("Falha ao abrir socket UDP no servidor", e);
        }

        ExecutorService threadPool = Executors.newCachedThreadPool();

        // Temporizador da Votação (60 segundos de prazo fixo)
        threadPool.execute(() -> {
            try {
                Thread.sleep(60000);
                votacaoAberta = false;
                calcularResultados();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Loop de Aceitação TCP
        try (ServerSocket serverSocket = new ServerSocket(TCP_PORT)) {
            System.out.println("[Servidor] Votação iniciada na porta " + TCP_PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(() -> tratarConexao(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void tratarConexao(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String comando = in.readLine();
            if (comando == null) return;

            String[] args = comando.split("\\|");
            String acao = args[0];

            switch (acao) {
                case "LOGIN_ELEITOR":
                    out.println(votacaoAberta ? "OK|" + MULTICAST_IP + "|" + MULTICAST_PORT : "ENCERRADA");
                    break;
                case "LOGIN_ADMIN":
                    out.println("OK");
                    break;
                case "LISTAR":
                    out.println(gerarJsonCandidatos());
                    break;
                case "VOTAR":
                    if (!votacaoAberta) {
                        out.println("ERRO_PRAZO_EXPIRADO");
                    } else {
                        int idCandidato = Integer.parseInt(args[1]);
                        boolean sucesso = computarVoto(idCandidato);
                        out.println(sucesso ? "VOTO_COMPUTADO" : "CANDIDATO_INVALIDO");
                    }
                    break;
                case "ADD_CANDIDATO":
                    candidatos.add(new Candidato(Integer.parseInt(args[1]), args[2]));
                    out.println("ADICIONADO");
                    break;
                case "MULTICAST_NOTA":
                    dispararNotaInformativa(args[1]);
                    out.println("ENVIADO");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String gerarJsonCandidatos() {
        StringBuilder sb = new StringBuilder("[");
        synchronized (candidatos) {
            for (int i = 0; i < candidatos.size(); i++) {
                sb.append(candidatos.get(i).toJson());
                if (i < candidatos.size() - 1) sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static boolean computarVoto(int id) {
        synchronized (candidatos) {
            for (Candidato c : candidatos) {
                if (c.getId() == id) {
                    c.incrementarVoto();
                    return true;
                }
            }
        }
        return false;
    }

    private static void dispararNotaInformativa(String mensagem) {
        try {
            String jsonFormat = String.format("{\"tipo\": \"NOTA_INFORMATIVA\", \"conteudo\": \"%s\"}", mensagem);
            byte[] data = jsonFormat.getBytes();
            InetAddress group = InetAddress.getByName(MULTICAST_IP);
            DatagramPacket packet = new DatagramPacket(data, data.length, group, MULTICAST_PORT);
            udpSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void calcularResultados() {
        System.out.println("\n--- PRAZO ENCERRADO. CALCULANDO RESULTADOS ---");
        int totalVotos = 0;
        Candidato ganhador = null;

        synchronized (candidatos) {
            for (Candidato c : candidatos) {
                totalVotos += c.getVotos();
                if (ganhador == null || c.getVotos() > ganhador.getVotos()) {
                    ganhador = c;
                }
            }

            System.out.println("Total de Votos: " + totalVotos);
            for (Candidato c : candidatos) {
                double percentual = totalVotos == 0 ? 0 : ((double) c.getVotos() / totalVotos) * 100;
                System.out.printf("Candidato: %s | Votos: %d | Percentagem: %.2f%%\n", c.getNome(), c.getVotos(), percentual);
            }
            if (ganhador != null && totalVotos > 0) {
                System.out.println("Ganhador: " + ganhador.getNome());
                dispararNotaInformativa("Votacao encerrada. Ganhador: " + ganhador.getNome());
            }
        }
    }
}