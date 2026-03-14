package sn.dakarterminal.dt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sn.dakarterminal.dt.entity.UserAccount;
import sn.dakarterminal.dt.service.UserAccountService;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'GFA', 'FACTURATION')")
    public ResponseEntity<UserAccount> findByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userAccountService.findByUserId(userId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'GFA')")
    public ResponseEntity<UserAccount> create(@RequestBody Map<String, Object> body) {
        Long userId = Long.valueOf(body.get("userId").toString());
        String numeroCompte = (String) body.get("numeroCompte");
        return ResponseEntity.ok(userAccountService.create(userId, numeroCompte));
    }

    @PatchMapping("/{id}/solde")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_U', 'GFA')")
    public ResponseEntity<UserAccount> updateSolde(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        return ResponseEntity.ok(userAccountService.updateSolde(id, amount));
    }
}
