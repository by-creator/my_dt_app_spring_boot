package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.DossierFacturationProforma;
import sn.dakarterminal.dt.service.ProformaService;

import java.io.IOException;

@RestController
@RequestMapping("/api/dossiers")
@RequiredArgsConstructor
public class DossierFacturationProformaController {

    private final ProformaService proformaService;

    @PostMapping("/{dossierId}/proforma/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<DossierFacturationProforma> upload(@PathVariable Long dossierId,
                                                              @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(proformaService.upload(dossierId, file));
    }

    @PatchMapping("/proforma/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<DossierFacturationProforma> validate(@PathVariable Long id) {
        return ResponseEntity.ok(proformaService.validate(id));
    }

    @GetMapping("/{dossierId}/proforma")
    public ResponseEntity<DossierFacturationProforma> findByDossier(@PathVariable Long dossierId) {
        return ResponseEntity.ok(proformaService.findByDossierId(dossierId));
    }
}
