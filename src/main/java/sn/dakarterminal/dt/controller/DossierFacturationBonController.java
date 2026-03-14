package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.DossierFacturationBon;
import sn.dakarterminal.dt.service.BonService;

import java.io.IOException;

@RestController
@RequestMapping("/api/dossiers")
@RequiredArgsConstructor
public class DossierFacturationBonController {

    private final BonService bonService;

    @PostMapping("/{dossierId}/bon/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<DossierFacturationBon> upload(@PathVariable Long dossierId,
                                                         @RequestParam("file") MultipartFile file,
                                                         @RequestParam(required = false) String numeroBon) throws IOException {
        return ResponseEntity.ok(bonService.upload(dossierId, file, numeroBon));
    }

    @PatchMapping("/bon/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<DossierFacturationBon> validate(@PathVariable Long id) {
        return ResponseEntity.ok(bonService.validate(id));
    }

    @GetMapping("/{dossierId}/bon")
    public ResponseEntity<DossierFacturationBon> findByDossier(@PathVariable Long dossierId) {
        return ResponseEntity.ok(bonService.findByDossierId(dossierId));
    }
}
