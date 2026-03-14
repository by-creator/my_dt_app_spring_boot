package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.Facturation;
import sn.dakarterminal.dt.repository.FacturationRepository;

import java.util.List;

@RestController
@RequestMapping("/api/douane")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'DOUANE')")
public class DouaneController {

    private final FacturationRepository facturationRepository;

    @GetMapping("/facturations")
    public ResponseEntity<List<Facturation>> getFacturations(
            @RequestParam(required = false) String regime,
            @RequestParam(required = false) String statut) {
        List<Facturation> facturations;
        if (statut != null) {
            facturations = facturationRepository.findByStatutAndPeriode(statut, null, null);
        } else {
            facturations = facturationRepository.findAll();
        }
        return ResponseEntity.ok(facturations);
    }

    @GetMapping("/facturations/{id}")
    public ResponseEntity<Facturation> getFacturation(@PathVariable Long id) {
        return ResponseEntity.ok(facturationRepository.findById(id)
                .orElseThrow(() -> new sn.dakarterminal.dt.exception.ResourceNotFoundException("Facturation not found: " + id)));
    }
}
