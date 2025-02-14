package nl.minvenj.nfi.websockets.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import nl.minvenj.nfi.websockets.dto.HelloMessageDto;
import nl.minvenj.nfi.websockets.dto.MessageDto;
import nl.minvenj.nfi.websockets.dto.MessageFromUserDto;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public MessageDto caseUpdate(HelloMessageDto message) {
        System.out.printf("On endpoint '/hello', received name: %s%n", message.getName());
        return new MessageDto("Hello " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public MessageDto newMessage(MessageFromUserDto message) {
        System.out.printf("On endpoint '/message', received message: %s%n", message.message());
        return new MessageDto("%s: %s".formatted(HtmlUtils.htmlEscape(message.name()), HtmlUtils.htmlEscape(message.message())));
    }
}
