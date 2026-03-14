package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.OrdreApproche;
import sn.dakarterminal.dt.service.PlanificationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planification")
@RequiredArgsConstructor
public class PlanificationController {

    private final PlanificationService planificationService;

    @GetMapping("/planning")
    public ResponseEntity<List<OrdreApproche>> getPlanning(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(planificationService.getPlanning(debut, fin));
    }

    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<OrdreApproche>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(planificationService.getByStatut(statut));
    }
}
