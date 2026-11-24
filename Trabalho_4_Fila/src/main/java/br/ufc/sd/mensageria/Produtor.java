package br.ufc.sd.mensageria;

import br.ufc.sd.mensageria.model.Paciente;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class Produtor {
    private static final String EXCHANGE_NAME = "paciente_exchange";
    private static final String ROUTING_KEY = "paciente.create";
    private static final String QUEUE_NAME = "paciente_queue";

    static void main() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin123");

        ObjectMapper mapper = new ObjectMapper();

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

            for (int i = 1; i <= 20; i++) {
                Paciente paciente = new Paciente("Paciente " + i, (int) (Math.random() * 100));

                byte[] messageBytes = mapper.writeValueAsBytes(paciente);

                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, messageBytes);

                System.out.println("[Produtor] Enviado: " + mapper.writeValueAsString(paciente));
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
