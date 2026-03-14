package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.GuichetEntity;
import sn.dakarterminal.dt.entity.ServiceEntity;
import sn.dakarterminal.dt.repository.GuichetRepository;
import sn.dakarterminal.dt.repository.ServiceRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gfa/api")
@RequiredArgsConstructor
public class GfaApiController {

    private final ServiceRepository serviceRepository;
    private final GuichetRepository guichetRepository;

    // ── SERVICES ────────────────────────────────────────────────

    @GetMapping("/services")
    public List<Map<String, Object>> getServices() {
        return serviceRepository.findAll().stream()
                .map(s -> Map.<String, Object>of(
                        "id",      s.getId(),
                        "nom",     s.getNom(),
                        "prefixe", s.getCode() != null ? s.getCode() : ""
                ))
                .toList();
    }

    @PostMapping("/services")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_U')")
    public ResponseEntity<Map<String, Object>> createService(@RequestBody Map<String, String> body) {
        String nom     = body.getOrDefault("nom", "").trim().toUpperCase();
        String prefixe = body.getOrDefault("prefixe", "").trim().toUpperCase();
        if (nom.isEmpty() || prefixe.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Champs obligatoires manquants"));
        }
        ServiceEntity svc = ServiceEntity.builder()
                .nom(nom)
                .code(prefixe)
                .actif(true)
                .build();
        svc = serviceRepository.save(svc);
        return ResponseEntity.ok(Map.of("id", svc.getId(), "nom", svc.getNom(), "prefixe", svc.getCode()));
    }

    @PutMapping("/services/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_U')")
    public ResponseEntity<Map<String, Object>> updateService(@PathVariable Long id,
                                                              @RequestBody Map<String, String> body) {
        return serviceRepository.findById(id).map(svc -> {
            String nom     = body.getOrDefault("nom", "").trim().toUpperCase();
            String prefixe = body.getOrDefault("prefixe", "").trim().toUpperCase();
            if (!nom.isEmpty())     svc.setNom(nom);
            if (!prefixe.isEmpty()) svc.setCode(prefixe);
            serviceRepository.save(svc);
            return ResponseEntity.ok(Map.<String, Object>of("id", svc.getId(), "nom", svc.getNom(), "prefixe", svc.getCode()));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/services/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_U')")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        if (!serviceRepository.existsById(id)) return ResponseEntity.notFound().build();
        serviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── GUICHETS (agents in admin UI) ────────────────────────────

    @GetMapping("/guichets")
    public List<Map<String, Object>> getGuichets() {
        return guichetRepository.findAll().stream()
                .map(g -> Map.<String, Object>of(
                        "id",         g.getId(),
                        "numero",     g.getNumero(),
                        "infos",      g.getInfos() != null ? g.getInfos() : "",
                        "serviceId",  g.getService() != null ? g.getService().getId() : "",
                        "serviceNom", g.getService() != null ? g.getService().getNom() : ""
                ))
                .toList();
    }

    @PostMapping("/guichets")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_U')")
    public ResponseEntity<Map<String, Object>> createGuichet(@RequestBody Map<String, Object> body) {
        String numero = String.valueOf(body.getOrDefault("numero", "")).trim();
        String infos  = String.valueOf(body.getOrDefault("infos",  "")).trim();
        if (numero.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le nom du guichet est obligatoire"));
        }
        GuichetEntity g = GuichetEntity.builder()
                .numero(numero)
                .infos(infos)
                .actif(true)
                .build();
        if (body.get("serviceId") != null) {
            Long svcId = Long.valueOf(String.valueOf(body.get("serviceId")));
            serviceRepository.findById(svcId).ifPresent(g::setService);
        }
        g = guichetRepository.save(g);
        return ResponseEntity.ok(buildGuichetMap(g));
    }

    @PutMapping("/guichets/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_U')")
    public ResponseEntity<Map<String, Object>> updateGuichet(@PathVariable Long id,
                                                              @RequestBody Map<String, Object> body) {
        return guichetRepository.findById(id).map(g -> {
            String numero = String.valueOf(body.getOrDefault("numero", "")).trim();
            String infos  = String.valueOf(body.getOrDefault("infos",  "")).trim();
            if (!numero.isEmpty()) g.setNumero(numero);
            g.setInfos(infos);
            if (body.get("serviceId") != null) {
                Long svcId = Long.valueOf(String.valueOf(body.get("serviceId")));
                serviceRepository.findById(svcId).ifPresent(g::setService);
            } else {
                g.setService(null);
            }
            guichetRepository.save(g);
            return ResponseEntity.ok(buildGuichetMap(g));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/guichets/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_U')")
    public ResponseEntity<Void> deleteGuichet(@PathVariable Long id) {
        if (!guichetRepository.existsById(id)) return ResponseEntity.notFound().build();
        guichetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> buildGuichetMap(GuichetEntity g) {
        return Map.of(
                "id",         g.getId(),
                "numero",     g.getNumero(),
                "infos",      g.getInfos() != null ? g.getInfos() : "",
                "serviceId",  g.getService() != null ? g.getService().getId() : "",
                "serviceNom", g.getService() != null ? g.getService().getNom() : ""
        );
    }
}
