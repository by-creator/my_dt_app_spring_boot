package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.Machine;
import sn.dakarterminal.dt.service.ExcelService;
import sn.dakarterminal.dt.service.MachineService;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;
    private final ExcelService excelService;

    @GetMapping
    public ResponseEntity<List<Machine>> findAll() {
        return ResponseEntity.ok(machineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Machine> findById(@PathVariable Long id) {
        return ResponseEntity.ok(machineService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'INFORMATIQUE')")
    public ResponseEntity<Machine> create(@RequestBody Machine machine) {
        return ResponseEntity.status(HttpStatus.CREATED).body(machineService.create(machine));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'INFORMATIQUE')")
    public ResponseEntity<Machine> update(@PathVariable Long id, @RequestBody Machine machine) {
        return ResponseEntity.ok(machineService.update(id, machine));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        machineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportExcel() {
        List<Machine> machines = machineService.findAll();
        byte[] data = excelService.generateMachineReport(machines);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=machines.xlsx")
                .body(data);
    }
}
