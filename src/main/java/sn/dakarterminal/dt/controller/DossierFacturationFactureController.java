package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.DossierFacturationFacture;
import sn.dakarterminal.dt.service.FactureService;

import java.io.IOException;

@RestController
@RequestMapping("/api/dossiers")
@RequiredArgsConstructor
public class DossierFacturationFactureController {

    private final FactureService factureService;

    @PostMapping("/{dossierId}/facture/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<DossierFacturationFacture> upload(@PathVariable Long dossierId,
                                                             @RequestParam("file") MultipartFile file,
                                                             @RequestParam(required = false) String numeroFacture) throws IOException {
        return ResponseEntity.ok(factureService.upload(dossierId, file, numeroFacture));
    }

    @PatchMapping("/facture/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<DossierFacturationFacture> validate(@PathVariable Long id) {
        return ResponseEntity.ok(factureService.validate(id));
    }

    @GetMapping("/{dossierId}/facture")
    public ResponseEntity<DossierFacturationFacture> findByDossier(@PathVariable Long dossierId) {
        return ResponseEntity.ok(factureService.findByDossierId(dossierId));
    }
}
