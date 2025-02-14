package nl.minvenj.nfi.websockets.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import nl.minvenj.nfi.websockets.controllers.rest.CaseDTO;
import nl.minvenj.nfi.websockets.event.dto.ForensicCaseDTO;

public class CaseDeletedEvent extends WebsocketEvent {

    public CaseDeletedEvent(Object source, ForensicCaseDTO forensicCaseDTO, SimpMessagingTemplate messagingTemplate) {
        super(source, forensicCaseDTO, messagingTemplate);
    }

    @Override
    public void callSocketBroadcaster() {
        brokerMessagingTemplate.convertAndSend("/topic/all_cases", CaseDTO.builder().forensicCaseNumber(forensicCaseDTO.getCaseNumber()).deleted(true).build());
        publishOnSocket("%s/%s".formatted(CASE_TOPIC, forensicCaseDTO.getCaseNumber()), "Case with number %s deleted.".formatted(forensicCaseDTO.getCaseNumber()));
    }
}
