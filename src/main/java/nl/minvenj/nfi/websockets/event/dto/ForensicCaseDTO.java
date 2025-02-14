package nl.minvenj.nfi.websockets.event.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ForensicCaseDTO {
    private final String caseNumber;
    private final Map<String, Object> updatedFields;

    public static ForensicCaseDTO mapFrom(final String caseNumber, final Map<String, Object> updatedFields) {
        return ForensicCaseDTO.builder()
            .caseNumber(caseNumber)
            .updatedFields(updatedFields)
            .build();
    }
}
