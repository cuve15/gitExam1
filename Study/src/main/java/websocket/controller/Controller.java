package websocket.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
@RequestMapping("/chat")
public class Controller {
	@GetMapping
    public String chatPage() {
        return "chat"; // chat.jsp¸¦ ¹ÝÈ¯
    }
}
