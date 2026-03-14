package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.TiersIpaki;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.TiersIpakiRepository;

import java.util.List;

@RestController
@RequestMapping("/api/ipaki")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'IPAKI')")
public class IpakiController {

    private final TiersIpakiRepository tiersIpakiRepository;

    @GetMapping("/tiers")
    public ResponseEntity<List<TiersIpaki>> findAll() {
        return ResponseEntity.ok(tiersIpakiRepository.findByActifTrue());
    }

    @GetMapping("/tiers/{id}")
    public ResponseEntity<TiersIpaki> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tiersIpakiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tiers IPAKI not found: " + id)));
    }

    @PostMapping("/tiers")
    public ResponseEntity<TiersIpaki> create(@RequestBody TiersIpaki tiers) {
        return ResponseEntity.ok(tiersIpakiRepository.save(tiers));
    }

    @PutMapping("/tiers/{id}")
    public ResponseEntity<TiersIpaki> update(@PathVariable Long id, @RequestBody TiersIpaki updated) {
        TiersIpaki existing = tiersIpakiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tiers IPAKI not found: " + id));
        existing.setNom(updated.getNom());
        existing.setAdresse(updated.getAdresse());
        existing.setTelephone(updated.getTelephone());
        existing.setEmail(updated.getEmail());
        existing.setNinea(updated.getNinea());
        existing.setActif(updated.getActif());
        return ResponseEntity.ok(tiersIpakiRepository.save(existing));
    }
}
