package sn.dakarterminal.dt.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.RattachementBl;
import sn.dakarterminal.dt.repository.RattachementBlRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/facturation/api/rattachements")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
@RequiredArgsConstructor
public class RattachementApiController {

    private final RattachementBlRepository rattachementBlRepository;

    @GetMapping
    public List<RattachementBl> list(@RequestParam(required = false) String statut) {
        if (statut != null && !statut.isBlank()) {
            return rattachementBlRepository.findByStatut(statut);
        }
        return rattachementBlRepository.findAll();
    }

    @PatchMapping("/{id}/valider")
    public ResponseEntity<Map<String, String>> valider(@PathVariable Long id) {
        return rattachementBlRepository.findById(id).map(r -> {
            r.setStatut("VALIDE");
            rattachementBlRepository.save(r);
            return ResponseEntity.ok(Map.of("status", "ok", "statut", "VALIDE"));
        }).orElse(ResponseEntity.notFound().<Map<String, String>>build());
    }

    @PatchMapping("/{id}/rejeter")
    public ResponseEntity<Map<String, String>> rejeter(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String motif = body.getOrDefault("motif", "");
        return rattachementBlRepository.findById(id).map(r -> {
            r.setStatut("REJETE");
            r.setMotifRejet(motif);
            rattachementBlRepository.save(r);
            return ResponseEntity.ok(Map.of("status", "ok", "statut", "REJETE"));
        }).orElse(ResponseEntity.notFound().<Map<String, String>>build());
    }
}
