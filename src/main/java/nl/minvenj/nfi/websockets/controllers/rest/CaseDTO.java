package nl.minvenj.nfi.websockets.controllers.rest;

import lombok.Builder;
import lombok.Getter;

import nl.minvenj.nfi.websockets.db.domain.ForensicCase;

@Getter
@Builder
public class CaseDTO {
    private final String forensicCaseNumber;
    private final String sin;
    private final String status;
    private final String username;
    private final boolean deleted;

    public static ForensicCase toEntity(CaseDTO forensicCaseDTO) {
        return new ForensicCase(forensicCaseDTO.getForensicCaseNumber(), forensicCaseDTO.getSin(), forensicCaseDTO.getStatus(), forensicCaseDTO.getUsername());
    }

    public static CaseDTO fromEntity(ForensicCase forensicCase) {
        return CaseDTO.builder()
            .forensicCaseNumber(forensicCase.getForensicCaseNumber())
            .sin(forensicCase.getSin())
            .status(forensicCase.getStatus())
            .username(forensicCase.getUsername())
            .deleted(false)
            .build();
    }
}
