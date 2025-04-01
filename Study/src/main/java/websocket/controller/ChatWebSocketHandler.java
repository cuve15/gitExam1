package websocket.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class ChatWebSocketHandler extends TextWebSocketHandler {
	
	// ����ں� WebSocket ������ �����ϴ� Map
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserId(session);
        sessions.put(userId, session);
        System.out.println(userId + " ���� �����Ͽ����ϴ�.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // �޽��� ����: "�޴»��ID:�޽�������"
        String payload = message.getPayload();
        String[] splitMessage = payload.split(":", 2);

        if (splitMessage.length < 2) {
            session.sendMessage(new TextMessage("�߸��� �޽��� �����Դϴ�."));
            return;
        }

        String receiverId = splitMessage[0];  // �޴� ��� ID
        String chatMessage = splitMessage[1]; // �޽��� ����

        WebSocketSession receiverSession = sessions.get(receiverId);
        if (receiverSession != null && receiverSession.isOpen()) {
            receiverSession.sendMessage(new TextMessage(chatMessage));
        } else {
            session.sendMessage(new TextMessage("������ ���� ���� �ƴմϴ�."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserId(session);
        sessions.remove(userId);
        System.out.println(userId + " ���� ������ �����Ͽ����ϴ�.");
    }

    private String getUserId(WebSocketSession session) {
        // ����� ID�� WebSocket ���ǿ��� �������� ���� (��: URL �Ķ���� �Ǵ� ���� ���� Ȱ��)
        return session.getId(); // ���������� WebSocket ���� ID�� ���
    }
}
