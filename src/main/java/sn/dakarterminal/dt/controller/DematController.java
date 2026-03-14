package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.dakarterminal.dt.entity.EmployeeTemporaireDemande;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.EmployeeTemporaireDemandeRepository;
import sn.dakarterminal.dt.service.FileStorageService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/demat")
@RequiredArgsConstructor
public class DematController {

    private final EmployeeTemporaireDemandeRepository demandeRepository;
    private final FileStorageService fileStorageService;

    @GetMapping("/demandes")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<List<EmployeeTemporaireDemande>> findAll() {
        return ResponseEntity.ok(demandeRepository.findAll());
    }

    @GetMapping("/demandes/{id}")
    public ResponseEntity<EmployeeTemporaireDemande> findById(@PathVariable Long id) {
        return ResponseEntity.ok(demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found: " + id)));
    }

    @PostMapping("/demandes")
    public ResponseEntity<EmployeeTemporaireDemande> create(@RequestBody EmployeeTemporaireDemande demande) {
        demande.setStatut("EN_ATTENTE");
        return ResponseEntity.ok(demandeRepository.save(demande));
    }

    @PatchMapping("/demandes/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<EmployeeTemporaireDemande> valider(@PathVariable Long id,
                                                              @RequestBody Map<String, String> body) {
        EmployeeTemporaireDemande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found: " + id));
        demande.setStatut("VALIDE");
        demande.setNumeroBadge(body.get("numeroBadge"));
        return ResponseEntity.ok(demandeRepository.save(demande));
    }

    @PatchMapping("/demandes/{id}/rejeter")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<EmployeeTemporaireDemande> rejeter(@PathVariable Long id) {
        EmployeeTemporaireDemande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found: " + id));
        demande.setStatut("REJETE");
        return ResponseEntity.ok(demandeRepository.save(demande));
    }

    @PostMapping("/demandes/{id}/document")
    public ResponseEntity<Map<String, String>> uploadDocument(@PathVariable Long id,
                                                               @RequestParam("file") MultipartFile file) throws IOException {
        demandeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande not found: " + id));
        String path = fileStorageService.store(file, "demat/" + id);
        return ResponseEntity.ok(Map.of("path", path));
    }
}
