package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.ServiceEntity;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.ServiceRepository;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceRepository serviceRepository;

    @GetMapping
    public ResponseEntity<List<ServiceEntity>> findAll() {
        return ResponseEntity.ok(serviceRepository.findByActifTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntity> findById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<ServiceEntity> create(@RequestBody ServiceEntity service) {
        return ResponseEntity.ok(serviceRepository.save(service));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<ServiceEntity> update(@PathVariable Long id, @RequestBody ServiceEntity updated) {
        ServiceEntity service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + id));
        service.setNom(updated.getNom());
        service.setCode(updated.getCode());
        service.setActif(updated.getActif());
        return ResponseEntity.ok(serviceRepository.save(service));
    }
}
