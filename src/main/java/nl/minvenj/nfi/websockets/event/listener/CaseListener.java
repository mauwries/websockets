package nl.minvenj.nfi.websockets.event.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import nl.minvenj.nfi.websockets.event.WebsocketEvent;

@Component
public class CaseListener implements ApplicationListener<WebsocketEvent> {

    @Override
    public void onApplicationEvent(WebsocketEvent event) {
        event.callSocketBroadcaster();
    }
}
