package nl.minvenj.nfi.websockets.db.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.minvenj.nfi.websockets.db.domain.ForensicCase;

public interface ForensicCaseRepo extends JpaRepository<ForensicCase, Long> {
    Optional<ForensicCase> findByForensicCaseNumber(String forensicCaseNumber);
}
