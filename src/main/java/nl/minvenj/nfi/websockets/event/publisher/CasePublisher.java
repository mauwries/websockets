package nl.minvenj.nfi.websockets.event.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import nl.minvenj.nfi.websockets.event.CaseAddedEvent;
import nl.minvenj.nfi.websockets.event.CaseDeletedEvent;
import nl.minvenj.nfi.websockets.event.CaseUpdatedEvent;
import nl.minvenj.nfi.websockets.event.WebsocketEvent;
import nl.minvenj.nfi.websockets.event.dto.ForensicCaseDTO;

@Component
public class CasePublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    public CasePublisher(final ApplicationEventPublisher applicationEventPublisher, final SimpMessagingTemplate messagingTemplate) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.messagingTemplate = messagingTemplate;
    }

    public void caseAdded(final ForensicCaseDTO forensicCaseDTO) {
        WebsocketEvent caseAddedEvent = new CaseAddedEvent(this, forensicCaseDTO, messagingTemplate);
        applicationEventPublisher.publishEvent(caseAddedEvent);
    }

    public void caseUpdated(final ForensicCaseDTO forensicCaseDTO) {
        WebsocketEvent caseUpdatedEvent = new CaseUpdatedEvent(this, forensicCaseDTO, messagingTemplate);
        applicationEventPublisher.publishEvent(caseUpdatedEvent);
    }

    public void caseDeleted(final ForensicCaseDTO forensicCaseDTO) {
        applicationEventPublisher.publishEvent(new CaseDeletedEvent(this, forensicCaseDTO, messagingTemplate));
    }
}
