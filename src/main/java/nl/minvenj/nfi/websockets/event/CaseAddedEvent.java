package nl.minvenj.nfi.websockets.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import nl.minvenj.nfi.websockets.controllers.rest.CaseDTO;
import nl.minvenj.nfi.websockets.event.dto.ForensicCaseDTO;

public class CaseAddedEvent extends WebsocketEvent {

    public CaseAddedEvent(Object source, ForensicCaseDTO forensicCaseDTO, SimpMessagingTemplate messagingTemplate) {
        super(source, forensicCaseDTO, messagingTemplate);
    }

    @Override
    public void callSocketBroadcaster() {
        brokerMessagingTemplate.convertAndSend("/topic/all_cases", CaseDTO.builder().forensicCaseNumber(forensicCaseDTO.getCaseNumber()).build());
        publishOnSocket("/topic/message", "Case with number %s added".formatted(forensicCaseDTO.getCaseNumber()));
    }
}
