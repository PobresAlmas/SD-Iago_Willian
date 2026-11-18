package br.ufc.sd.mensageria;

import br.ufc.sd.mensageria.model.Paciente;
import br.ufc.sd.mensageria.model.Prontuario;
import com.rabbitmq.client.*;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class Consumidor {
    private static final String QUEUE_NAME = "paciente_queue";

    static void main() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin123");

        factory.setAutomaticRecoveryEnabled(true);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        channel.basicQos(1);

        ObjectMapper mapper = new ObjectMapper();
        System.out.println("[Consumidor] Aguardando mensagens na fila '" + QUEUE_NAME + "'...");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            long deliveryTag = message.getEnvelope().getDeliveryTag();

            try {
                Paciente paciente = mapper.readValue(message.getBody(), Paciente.class);
                System.out.println("[Consumidor] Mensagem recebida: " + paciente);


                Thread.sleep(2000);

                channel.basicAck(deliveryTag, false);
                System.out.println("[Consumidor] Concluído e confirmado (ACK): " + paciente.getId());

                Prontuario prontuario = new Prontuario(paciente.getId(), "Remédio bom");

                System.out.println("[Consumidor] Prontuário criado: " + prontuario);
            } catch (Exception e) {
                System.err.println("[Consumidor] Falha ao processar mensagem: " + e.getMessage());

                channel.basicNack(deliveryTag, false, true);
            }
        });

        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("[Consumidor] Consumo cancelado pelo broker: " + consumerTag);
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
