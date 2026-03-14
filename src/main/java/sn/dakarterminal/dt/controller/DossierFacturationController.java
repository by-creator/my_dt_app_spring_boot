package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.dto.DossierFacturationDto;
import sn.dakarterminal.dt.enums.StatutDossier;
import sn.dakarterminal.dt.service.DossierFacturationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dossiers")
@RequiredArgsConstructor
public class DossierFacturationController {

    private final DossierFacturationService dossierService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<List<DossierFacturationDto>> findAll() {
        return ResponseEntity.ok(dossierService.findAll());
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<Page<DossierFacturationDto>> findByStatut(@PathVariable StatutDossier statut, Pageable pageable) {
        return ResponseEntity.ok(dossierService.findByStatut(statut, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DossierFacturationDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(dossierService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION', 'CLIENT_FACTURATION')")
    public ResponseEntity<DossierFacturationDto> create(@RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(dossierService.create(body.get("userId"), body.get("rattachementBlId")));
    }

    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<DossierFacturationDto> valider(@PathVariable Long id) {
        return ResponseEntity.ok(dossierService.valider(id));
    }

    @PatchMapping("/{id}/rejeter")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<DossierFacturationDto> rejeter(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(dossierService.rejeter(id, body.get("motif")));
    }
}
