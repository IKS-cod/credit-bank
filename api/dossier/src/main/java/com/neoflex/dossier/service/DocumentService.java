package com.neoflex.dossier.service;


import com.neoflex.dossier.dto.EmailMessage;
import com.neoflex.dossier.enums.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private static final String TOPIC_DOCUMENT_SEND = "send-documents";
    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public DocumentService(KafkaTemplate<String, EmailMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDocuments(UUID statementId) {
        logger.info("Starting to send documents for statementId={}", statementId);
        Statement statement = statementReposytory.findId(statementId);
        EmailMessage message = new EmailMessage();
        message.setStatementId(statementId);
        message.setAddress(statement.getEmail);  // пример, заменить на реальный email
        message.setTheme(Theme.SEND_DOCUMENTS);    // enum, который соответствует send-documents
        message.setText("Ваши документы готовы к отправке.");

        logger.debug("Prepared EmailMessage: {}", message);

        kafkaTemplate.send(TOPIC_DOCUMENT_SEND, message);

        logger.info("EmailMessage sent to Kafka topic '{}' for statementId={}", TOPIC_DOCUMENT_SEND, statementId);

    }

    public void signDocuments(UUID statementId) {
        logger.info("Starting document signing process for statementId={}", statementId);


        // написать логику

        logger.info("Document signing process completed for statementId={}", statementId);
    }

    public void confirmSignDocuments(UUID statementId) {
        logger.info("Confirming signed documents for statementId={}", statementId);


     //   написать логику


        logger.info("Confirmed signed documents for statementId={}", statementId);
    }
}

