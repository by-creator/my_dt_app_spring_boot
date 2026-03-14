package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.EdiRecord;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.EdiRecordRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EdiService {

    private final EdiRecordRepository ediRecordRepository;
    private final FileStorageService fileStorageService;

    public EdiRecord ingest(MultipartFile file, String messageType, String sender, String receiver) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        String filePath = fileStorageService.store(file, "edi/" + messageType);

        EdiRecord record = EdiRecord.builder()
                .messageType(messageType)
                .sender(sender)
                .receiver(receiver)
                .fileName(file.getOriginalFilename())
                .filePath(filePath)
                .content(content)
                .statut("RECU")
                .processed(false)
                .build();

        return ediRecordRepository.save(record);
    }

    public EdiRecord process(Long id) {
        EdiRecord record = ediRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EDI record not found: " + id));

        try {
            parseAndProcess(record);
            record.setProcessed(true);
            record.setStatut("TRAITE");
            record.setProcessedAt(LocalDateTime.now());
        } catch (Exception e) {
            record.setStatut("ERREUR");
            record.setErrorMessage(e.getMessage());
            log.error("Error processing EDI record {}: {}", id, e.getMessage());
        }

        return ediRecordRepository.save(record);
    }

    private void parseAndProcess(EdiRecord record) {
        log.info("Processing EDI record type: {} reference: {}", record.getMessageType(), record.getReference());
        // Parse EDIFACT/XML content based on messageType
        // e.g., COPARN, BAPLIE, MOVINS etc.
    }

    @Transactional(readOnly = true)
    public List<EdiRecord> findUnprocessed() {
        return ediRecordRepository.findByProcessedFalse();
    }

    @Transactional(readOnly = true)
    public List<EdiRecord> findAll() {
        return ediRecordRepository.findAll();
    }
}
