package nl.minvenj.nfi.websockets.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import nl.minvenj.nfi.websockets.dto.MessageDto;
import nl.minvenj.nfi.websockets.event.dto.ForensicCaseDTO;

public abstract class WebsocketEvent extends ApplicationEvent {
    final static String CASE_TOPIC = "/topic/case";
    final ForensicCaseDTO forensicCaseDTO;
    final SimpMessagingTemplate brokerMessagingTemplate;

    public WebsocketEvent(Object source, final ForensicCaseDTO forensicCaseDTO, final SimpMessagingTemplate brokerMessagingTemplate) {
        super(source);
        this.forensicCaseDTO = forensicCaseDTO;
        this.brokerMessagingTemplate = brokerMessagingTemplate;
    }

    public abstract void callSocketBroadcaster();

    void publishOnSocket(final String address, final String message) {
        brokerMessagingTemplate.convertAndSend(address, new MessageDto(message));
    }
}
