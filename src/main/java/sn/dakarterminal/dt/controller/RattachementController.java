package sn.dakarterminal.dt.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.dto.RattachementBlDto;
import sn.dakarterminal.dt.service.RattachementService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rattachements")
@RequiredArgsConstructor
public class RattachementController {

    private final RattachementService rattachementService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<List<RattachementBlDto>> findAll() {
        return ResponseEntity.ok(rattachementService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RattachementBlDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rattachementService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RattachementBlDto>> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(rattachementService.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<RattachementBlDto> create(@Valid @RequestBody RattachementBlDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rattachementService.create(dto));
    }

    @PatchMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<RattachementBlDto> valider(@PathVariable Long id) {
        return ResponseEntity.ok(rattachementService.validate(id));
    }

    @PatchMapping("/{id}/rejeter")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'FACTURATION')")
    public ResponseEntity<RattachementBlDto> rejeter(@PathVariable Long id,
                                                      @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(rattachementService.reject(id, body.get("motif")));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rattachementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
