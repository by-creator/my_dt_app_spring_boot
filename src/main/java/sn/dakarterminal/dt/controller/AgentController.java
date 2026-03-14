package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.Agent;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.AgentRepository;

import java.util.List;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentRepository agentRepository;

    @GetMapping
    public ResponseEntity<List<Agent>> findAll() {
        return ResponseEntity.ok(agentRepository.findByActifTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agent> findById(@PathVariable Long id) {
        return ResponseEntity.ok(agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found: " + id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<Agent> create(@RequestBody Agent agent) {
        return ResponseEntity.ok(agentRepository.save(agent));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<Agent> update(@PathVariable Long id, @RequestBody Agent updated) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent not found: " + id));
        agent.setNom(updated.getNom());
        agent.setPrenom(updated.getPrenom());
        agent.setActif(updated.getActif());
        return ResponseEntity.ok(agentRepository.save(agent));
    }
}
