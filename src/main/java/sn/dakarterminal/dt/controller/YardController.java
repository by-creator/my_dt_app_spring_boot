package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.Yard;
import sn.dakarterminal.dt.service.ExcelService;
import sn.dakarterminal.dt.service.YardImportService;
import sn.dakarterminal.dt.service.YardService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/yard")
@RequiredArgsConstructor
public class YardController {

    private final YardService yardService;
    private final YardImportService yardImportService;
    private final ExcelService excelService;

    @GetMapping
    public ResponseEntity<List<Yard>> findAll() {
        return ResponseEntity.ok(yardService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Yard> findById(@PathVariable Long id) {
        return ResponseEntity.ok(yardService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'OPERATIONS')")
    public ResponseEntity<Yard> create(@RequestBody Yard yard) {
        return ResponseEntity.ok(yardService.create(yard));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'OPERATIONS')")
    public ResponseEntity<Yard> update(@PathVariable Long id, @RequestBody Yard yard) {
        return ResponseEntity.ok(yardService.update(id, yard));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        yardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'OPERATIONS')")
    public ResponseEntity<Map<String, String>> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        String batchId = yardImportService.importFromExcel(file);
        return ResponseEntity.ok(Map.of("batchId", batchId));
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportExcel() {
        List<Yard> yards = yardService.findAll();
        byte[] data = excelService.generateYardReport(yards);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=yard.xlsx")
                .body(data);
    }
}
