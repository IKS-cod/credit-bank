package com.neoflex.dossier.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

@Service
public class EmailConsumer {

    @KafkaListener(topics = {
            "finish-registration",
            "create-documents",
            "send-documents",
            "send-ses",
            "credit-issued",
            "statement-denied"
    }, groupId = "${spring.kafka.consumer.group-id}")
    public void listenGroup(String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println("Received message: '" + message + "' from topic: " + topic);

        // Здесь логика обработки сообщения, например,
        // формирование и отправка письма клиенту по теме topic
    }
}
