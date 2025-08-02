package com.neoflex.dossier.controller;

import com.neoflex.dossier.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deal/document")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(summary = "Запрос на отправку документов клиенту")
    @PostMapping("/{statementId}/send")
    public ResponseEntity<String> sendDocuments(@PathVariable @NotNull UUID statementId) {
        logger.info("POST /deal/document/{}/send - received request to send documents", statementId);
        try {
            documentService.sendDocuments(statementId);
            logger.info("Documents sent successfully for statementId={}", statementId);
            return ResponseEntity.ok("Documents sent");
        } catch (Exception e) {
            logger.error("Error sending documents for statementId={}", statementId, e);
            return ResponseEntity.status(500).body("Error sending documents");
        }
    }

    @Operation(summary = "Запрос на подписание документов клиентом")
    @PostMapping("/{statementId}/sign")
    public ResponseEntity<String> signDocuments(@PathVariable @NotNull UUID statementId) {
        logger.info("POST /deal/document/{}/sign - received request to sign documents", statementId);
        try {
            documentService.signDocuments(statementId);
            logger.info("Documents signed successfully for statementId={}", statementId);
            return ResponseEntity.ok("Documents signed");
        } catch (Exception e) {
            logger.error("Error signing documents for statementId={}", statementId, e);
            return ResponseEntity.status(500).body("Error signing documents");
        }
    }

    @Operation(summary = "Подтверждение подписи документов")
    @PostMapping("/{statementId}/code")
    public ResponseEntity<String> confirmSignDocuments(@PathVariable @NotNull UUID statementId) {
        logger.info("POST /deal/document/{}/code - received request to confirm document signing", statementId);
        try {
            documentService.confirmSignDocuments(statementId);
            logger.info("Confirmation of signed documents succeeded for statementId={}", statementId);
            return ResponseEntity.ok("Document signing confirmed");
        } catch (Exception e) {
            logger.error("Error confirming signed documents for statementId={}", statementId, e);
            return ResponseEntity.status(500).body("Error confirming document signing");
        }
    }
}
