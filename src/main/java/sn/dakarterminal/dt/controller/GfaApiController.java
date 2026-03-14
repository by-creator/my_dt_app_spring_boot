package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.GuichetEntity;
import sn.dakarterminal.dt.entity.ServiceEntity;
import sn.dakarterminal.dt.entity.Agent;
import sn.dakarterminal.dt.repository.AgentRepository;
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
    private final AgentRepository agentRepository;

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
        if (nom.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le nom du service est obligatoire"));
        }
        ServiceEntity svc = ServiceEntity.builder()
                .nom(nom)
                .code(prefixe.isEmpty() ? null : prefixe)
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
            if (body.containsKey("prefixe")) {
                svc.setCode(prefixe.isEmpty() ? null : prefixe);
            }
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

    // ── AGENTS ───────────────────────────────────────────────────

    @GetMapping("/agents")
    public List<Map<String, Object>> getAgents() {
        return agentRepository.findByActifTrue().stream()
                .map(this::buildAgentMap)
                .toList();
    }

    @PostMapping("/agents")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_U')")
    public ResponseEntity<Map<String, Object>> createAgent(@RequestBody Map<String, Object> body) {
        String nom = String.valueOf(body.getOrDefault("nom", "")).trim().toUpperCase();
        String prenom = String.valueOf(body.getOrDefault("prenom", "")).trim();
        if (nom.isEmpty() || body.get("serviceId") == null || body.get("guichetId") == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nom, service et guichet sont obligatoires"));
        }

        Agent agent = Agent.builder()
                .nom(nom)
                .prenom(prenom)
                .actif(true)
                .build();

        Long serviceId = Long.valueOf(String.valueOf(body.get("serviceId")));
        Long guichetId = Long.valueOf(String.valueOf(body.get("guichetId")));
        serviceRepository.findById(serviceId).ifPresent(agent::setService);
        guichetRepository.findById(guichetId).ifPresent(agent::setGuichet);

        agent = agentRepository.save(agent);
        return ResponseEntity.ok(buildAgentMap(agent));
    }

    @PutMapping("/agents/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_U')")
    public ResponseEntity<Map<String, Object>> updateAgent(@PathVariable Long id,
                                                           @RequestBody Map<String, Object> body) {
        return agentRepository.findById(id).map(agent -> {
            String nom = String.valueOf(body.getOrDefault("nom", "")).trim().toUpperCase();
            String prenom = String.valueOf(body.getOrDefault("prenom", "")).trim();
            if (!nom.isEmpty()) {
                agent.setNom(nom);
            }
            agent.setPrenom(prenom);

            if (body.get("serviceId") != null) {
                Long serviceId = Long.valueOf(String.valueOf(body.get("serviceId")));
                serviceRepository.findById(serviceId).ifPresent(agent::setService);
            }
            if (body.get("guichetId") != null) {
                Long guichetId = Long.valueOf(String.valueOf(body.get("guichetId")));
                guichetRepository.findById(guichetId).ifPresent(agent::setGuichet);
            }

            agentRepository.save(agent);
            return ResponseEntity.ok(buildAgentMap(agent));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/agents/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_U')")
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        return agentRepository.findById(id).map(agent -> {
            agent.setActif(false);
            agentRepository.save(agent);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
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

    private Map<String, Object> buildAgentMap(Agent agent) {
        return Map.of(
                "id", agent.getId(),
                "nom", agent.getNom() != null ? agent.getNom() : "",
                "prenom", agent.getPrenom() != null ? agent.getPrenom() : "",
                "serviceId", agent.getService() != null ? agent.getService().getId() : "",
                "serviceNom", agent.getService() != null ? agent.getService().getNom() : "",
                "guichetId", agent.getGuichet() != null ? agent.getGuichet().getId() : "",
                "guichetNumero", agent.getGuichet() != null ? agent.getGuichet().getNumero() : ""
        );
    }
}
