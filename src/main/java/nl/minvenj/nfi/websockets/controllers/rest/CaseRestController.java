package nl.minvenj.nfi.websockets.controllers.rest;

import java.util.List;
import java.util.Objects;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import nl.minvenj.nfi.websockets.service.CaseService;

@RequestMapping("api/v1/forensic-case")
@RestController
public class CaseRestController {

    private final CaseService caseService;

    public CaseRestController(final CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping
    public List<CaseDTO> findAll() {
        return caseService.findAll();
    }

    @PostMapping
    public CaseDTO createCase(@RequestBody final CaseDTO forensicCase) {
        return caseService.createCase(forensicCase);
    }

    @PutMapping("{caseNumber}")
    public CaseDTO updateCase(@PathVariable("caseNumber") final String caseNumber,
                              @RequestBody final CaseDTO forensicCase) {
        Objects.requireNonNull(forensicCase, "Mag niet null zijn");
        if (!caseNumber.equalsIgnoreCase(forensicCase.getForensicCaseNumber())) {
            throw new IllegalArgumentException("Key field does not equal");
        }
        return caseService.updateCase(forensicCase);
    }

    @DeleteMapping("{caseNumber}")
    public void removeCase(@PathVariable("caseNumber") final String caseNumber) {
        caseService.removeCase(caseNumber);
    }
}
