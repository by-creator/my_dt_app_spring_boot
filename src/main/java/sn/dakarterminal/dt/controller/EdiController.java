package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.EdiRecord;
import sn.dakarterminal.dt.service.EdiService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/edi")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'OPERATIONS')")
public class EdiController {

    private final EdiService ediService;

    @GetMapping
    public ResponseEntity<List<EdiRecord>> findAll() {
        return ResponseEntity.ok(ediService.findAll());
    }

    @GetMapping("/unprocessed")
    public ResponseEntity<List<EdiRecord>> findUnprocessed() {
        return ResponseEntity.ok(ediService.findUnprocessed());
    }

    @PostMapping("/ingest")
    public ResponseEntity<EdiRecord> ingest(@RequestParam("file") MultipartFile file,
                                             @RequestParam String messageType,
                                             @RequestParam(required = false) String sender,
                                             @RequestParam(required = false) String receiver) throws IOException {
        return ResponseEntity.ok(ediService.ingest(file, messageType, sender, receiver));
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<EdiRecord> process(@PathVariable Long id) {
        return ResponseEntity.ok(ediService.process(id));
    }
}
