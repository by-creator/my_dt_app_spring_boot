package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.UserAccount;
import sn.dakarterminal.dt.repository.UserAccountRepository;

import java.util.List;

@RestController
@RequestMapping("/api/gfa")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'GFA')")
public class GfaController {

    private final UserAccountRepository userAccountRepository;

    @GetMapping("/accounts")
    public ResponseEntity<List<UserAccount>> findAllAccounts() {
        return ResponseEntity.ok(userAccountRepository.findAll());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<UserAccount> findAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(userAccountRepository.findById(id)
                .orElseThrow(() -> new sn.dakarterminal.dt.exception.ResourceNotFoundException("Account not found: " + id)));
    }
}
