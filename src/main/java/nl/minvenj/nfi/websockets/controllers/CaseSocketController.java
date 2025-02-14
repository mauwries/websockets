package nl.minvenj.nfi.websockets.controllers;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import nl.minvenj.nfi.websockets.controllers.rest.CaseDTO;
import nl.minvenj.nfi.websockets.dto.CaseMessage;
import nl.minvenj.nfi.websockets.dto.MessageDto;
import nl.minvenj.nfi.websockets.service.CaseService;

@Controller
public class CaseSocketController {

    private final CaseService caseService;

    public CaseSocketController(final CaseService caseService) {
        this.caseService = caseService;
    }

    @MessageMapping("/forensic_case/{caseNumber}")
    @SendTo("/topic/case/{caseNumber}")
    public CaseMessage caseUpdate(@DestinationVariable String caseNumber, MessageDto message) {
        System.out.printf("On endpoint '/forensic_case/{caseNumber}', received case: %s with message: %s%n", caseNumber, message.message());
        return new CaseMessage(caseNumber, HtmlUtils.htmlEscape(message.message()));
    }

    @SubscribeMapping("/forensic_case/{caseNumber}")
    public MessageDto subscribeToCase(@DestinationVariable String caseNumber) {
        // This method sends the data only to the subscriber, it is not broadcast to all subscribers
        System.out.printf("Received message subscription on endpoint '/forensic_case/{caseNumber} for case %s%n", caseNumber);
        return new MessageDto("Placeholder for all data present in the database for case %s".formatted(caseNumber));
    }

    @SubscribeMapping("/all-cases")
    public List<CaseDTO> getAllCases() {
        return caseService.findAll();
    }
}
