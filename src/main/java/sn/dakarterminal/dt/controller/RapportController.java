package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.service.RapportService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/rapports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION', 'GFA')")
public class RapportController {

    private final RapportService rapportService;

    @GetMapping(value = "/facturation/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportFacturationsExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        byte[] data = rapportService.exportFacturationsToExcel(debut, fin);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=rapport-facturation.xlsx")
                .body(data);
    }

    @GetMapping(value = "/yard/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportYardExcel(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        byte[] data = rapportService.exportYardToExcel(debut, fin);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=rapport-yard.xlsx")
                .body(data);
    }
}
