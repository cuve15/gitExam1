package websocket.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatWebSocketHandler extends TextWebSocketHandler {
	
	// 사용자별 WebSocket 세션을 저장하는 Map
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        sessions.put(userId, session);
        System.out.println(userId + " 님이 접속하였습니다.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지 포맷: "받는사람ID:메시지내용"
        String payload = message.getPayload();
        String[] splitMessage = payload.split(":", 2);

        if (splitMessage.length < 2) {
            session.sendMessage(new TextMessage("잘못된 메시지 형식입니다."));
            return;
        }

        String receiverId = splitMessage[0];  // 받는 사람 ID
        String chatMessage = splitMessage[1]; // 메시지 내용

        WebSocketSession receiverSession = sessions.get(receiverId);
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(chatMessage));
        } else {
            session.sendMessage(new TextMessage("상대방이 접속 중이 아닙니다."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserId(session);
        sessions.remove(userId);
        System.out.println(userId + " 님이 연결을 종료하였습니다.");
    }

    private String getUserId(WebSocketSession session) {
        // 사용자 ID를 WebSocket 세션에서 가져오는 로직 (예: URL 파라미터 또는 인증 정보 활용)
        return session.getId(); // 예제에서는 WebSocket 세션 ID를 사용
    }
}
