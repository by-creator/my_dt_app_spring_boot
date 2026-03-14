package sn.dakarterminal.dt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.dakarterminal.dt.entity.ScanToken;

import java.util.Optional;

public interface ScanTokenRepository extends JpaRepository<ScanToken, Long> {
    Optional<ScanToken> findByToken(String token);
}
