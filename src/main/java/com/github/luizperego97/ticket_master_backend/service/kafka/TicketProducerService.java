package com.github.luizperego97.ticket_master_backend.service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketProducerService {

    // Essa é a ferramenta do Spring que conecta no Kafka
    private final KafkaTemplate<String, String> kafkaTemplate;

    // Construtor para o Spring injetar o KafkaTemplate automaticamente
    public TicketProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTicketPurchasedEvent(String mensagem) {
        // O "tópico" é como se fosse o canal de TV onde vamos transmitir a notícia
        String topico = "ingresso-comprado-topic";

        System.out.println("====== PRODUCER KAFKA ======");
        System.out.println("Disparando mensagem para o canal '" + topico + "': " + mensagem);
        System.out.println("=============================");

        // Envia a mensagem de texto pura para a fila
        this.kafkaTemplate.send(topico, mensagem);
    }
}
