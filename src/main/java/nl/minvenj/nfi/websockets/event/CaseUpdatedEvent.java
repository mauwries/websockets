package nl.minvenj.nfi.websockets.event;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import nl.minvenj.nfi.websockets.event.dto.ForensicCaseDTO;

public class CaseUpdatedEvent extends WebsocketEvent {

    public CaseUpdatedEvent(Object source, ForensicCaseDTO forensicCaseDTO, SimpMessagingTemplate messagingTemplate) {
        super(source, forensicCaseDTO, messagingTemplate);
    }

    @Override
    public void callSocketBroadcaster() {
        final Set<String> setOfChanges = forensicCaseDTO.getUpdatedFields().entrySet().stream()
            .map(entry -> entry.getKey() + " - " + entry.getValue())
            .collect(Collectors.toSet());
        publishOnSocket("%s/%s".formatted(CASE_TOPIC, forensicCaseDTO.getCaseNumber()), "Case with number %s updated, fields %s.".formatted(forensicCaseDTO.getCaseNumber(), setOfChanges));
    }
}

