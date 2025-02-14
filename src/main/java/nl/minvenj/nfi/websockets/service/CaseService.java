package nl.minvenj.nfi.websockets.service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.minvenj.nfi.websockets.controllers.rest.CaseDTO;
import nl.minvenj.nfi.websockets.db.domain.ForensicCase;
import nl.minvenj.nfi.websockets.db.repo.ForensicCaseRepo;
import nl.minvenj.nfi.websockets.event.dto.ForensicCaseDTO;
import nl.minvenj.nfi.websockets.event.publisher.CasePublisher;

@Service
@Transactional
public class CaseService {
    private final CasePublisher casePublisher;
    private final ForensicCaseRepo forensicCaseRepo;

    public CaseService(final CasePublisher casePublisher, final ForensicCaseRepo forensicCaseRepo) {
        this.casePublisher = casePublisher;
        this.forensicCaseRepo = forensicCaseRepo;
    }

    public List<CaseDTO> findAll() {
        return forensicCaseRepo.findAll().stream().map(CaseDTO::fromEntity).toList();
    }

    public CaseDTO createCase(final CaseDTO forensicCase) {
        final ForensicCase updatedForensicCase = forensicCaseRepo.save(CaseDTO.toEntity(forensicCase));
        casePublisher.caseAdded(ForensicCaseDTO.mapFrom(updatedForensicCase.getForensicCaseNumber(),
                                                        Map.of("sin", updatedForensicCase.getSin(), "status", updatedForensicCase.getStatus(), "user", forensicCase.getUsername())));
        return CaseDTO.fromEntity(updatedForensicCase);
    }

    public CaseDTO updateCase(final CaseDTO forensicCase) {
        final Map<String, Object> changes = new HashMap<>();
        final String forensicCaseNumber = forensicCase.getForensicCaseNumber();

        final ForensicCase originalCase = forensicCaseRepo.findByForensicCaseNumber(forensicCaseNumber)
            .orElseThrow(() -> new RuntimeException("Forensic case for number %s not found".formatted(forensicCaseNumber)));

        if (isNotBlank(forensicCase.getSin()) && !forensicCase.getSin().equals(originalCase.getSin())) {
            changes.put("sin", forensicCase.getSin());
            originalCase.setSin(forensicCase.getSin());
        }
        if (isNotBlank(forensicCase.getStatus()) && !forensicCase.getStatus().equals(originalCase.getStatus())) {
            changes.put("status", forensicCase.getStatus());
            originalCase.setStatus(forensicCase.getStatus());
        }
        if (isNotBlank(forensicCase.getUsername()) && !forensicCase.getUsername().equals(originalCase.getUsername())) {
            changes.put("username", forensicCase.getUsername());
            originalCase.setUsername(forensicCase.getUsername());
        }
        casePublisher.caseUpdated(ForensicCaseDTO.builder().caseNumber(forensicCaseNumber).updatedFields(changes).build());

        return CaseDTO.fromEntity(originalCase);
    }

    public void removeCase(final String caseNumber) {
        final Optional<ForensicCase> originalCase = forensicCaseRepo.findByForensicCaseNumber(caseNumber);
        if (originalCase.isPresent()) {
            forensicCaseRepo.delete(originalCase.get());
            casePublisher.caseDeleted(ForensicCaseDTO.builder().caseNumber(caseNumber).build());

        }
    }
}
