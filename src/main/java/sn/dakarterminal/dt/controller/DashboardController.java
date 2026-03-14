package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.dakarterminal.dt.repository.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final DossierFacturationRepository dossierRepository;
    private final TicketRepository ticketRepository;
    private final YardRepository yardRepository;
    private final FacturationRepository facturationRepository;
    private final MachineRepository machineRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalDossiers", dossierRepository.count());
        stats.put("totalYardEntries", yardRepository.count());
        stats.put("totalFacturations", facturationRepository.count());
        stats.put("totalMachines", machineRepository.count());
        return ResponseEntity.ok(stats);
    }
}
