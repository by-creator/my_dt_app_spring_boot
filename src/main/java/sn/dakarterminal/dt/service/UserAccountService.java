package sn.dakarterminal.dt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.dakarterminal.dt.entity.User;
import sn.dakarterminal.dt.entity.UserAccount;
import sn.dakarterminal.dt.exception.ResourceNotFoundException;
import sn.dakarterminal.dt.repository.UserAccountRepository;
import sn.dakarterminal.dt.repository.UserRepository;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserAccount findByUserId(Long userId) {
        return userAccountRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for user: " + userId));
    }

    public UserAccount create(Long userId, String numeroCompte) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        UserAccount account = UserAccount.builder()
                .user(user)
                .numeroCompte(numeroCompte)
                .solde(BigDecimal.ZERO)
                .devise("XOF")
                .actif(true)
                .build();

        return userAccountRepository.save(account);
    }

    public UserAccount updateSolde(Long accountId, BigDecimal amount) {
        UserAccount account = userAccountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
        account.setSolde(account.getSolde().add(amount));
        return userAccountRepository.save(account);
    }
}
