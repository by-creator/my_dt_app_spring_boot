package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.OrdreApproche;
import sn.dakarterminal.dt.service.OrdreApprocheService;

import java.util.List;

@RestController
@RequestMapping("/api/ordre-approches")
@RequiredArgsConstructor
public class OrdreApprocheController {

    private final OrdreApprocheService ordreApprocheService;

    @GetMapping
    public ResponseEntity<List<OrdreApproche>> findAll() {
        return ResponseEntity.ok(ordreApprocheService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdreApproche> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ordreApprocheService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'OPERATIONS', 'PLANIFICATION')")
    public ResponseEntity<OrdreApproche> create(@RequestBody OrdreApproche ordreApproche) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordreApprocheService.create(ordreApproche));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'OPERATIONS', 'PLANIFICATION')")
    public ResponseEntity<OrdreApproche> update(@PathVariable Long id, @RequestBody OrdreApproche ordreApproche) {
        return ResponseEntity.ok(ordreApprocheService.update(id, ordreApproche));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ordreApprocheService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
