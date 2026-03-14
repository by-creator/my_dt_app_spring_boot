package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.Facturation;
import sn.dakarterminal.dt.repository.FacturationRepository;
import sn.dakarterminal.dt.service.ExcelService;
import sn.dakarterminal.dt.service.FacturationImportService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/facturation")
@RequiredArgsConstructor
public class FacturationController {

    private final FacturationRepository facturationRepository;
    private final FacturationImportService importService;
    private final ExcelService excelService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION', 'CLIENT_FACTURATION')")
    public ResponseEntity<List<Facturation>> findAll() {
        return ResponseEntity.ok(facturationRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION', 'CLIENT_FACTURATION')")
    public ResponseEntity<Facturation> findById(@PathVariable Long id) {
        return ResponseEntity.ok(facturationRepository.findById(id)
                .orElseThrow(() -> new sn.dakarterminal.dt.exception.ResourceNotFoundException("Facturation not found: " + id)));
    }

    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<Map<String, String>> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        String batchId = importService.importFromExcel(file);
        return ResponseEntity.ok(Map.of("batchId", batchId, "message", "Import started"));
    }

    @PostMapping("/consolidate/{batchId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<Void> consolidate(@PathVariable String batchId) {
        importService.consolidate(batchId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<byte[]> exportExcel() {
        List<Facturation> facturations = facturationRepository.findAll();
        byte[] data = excelService.generateFacturationReport(facturations);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=facturations.xlsx")
                .body(data);
    }
}
