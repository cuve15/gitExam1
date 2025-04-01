<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>WebSocket 1:1 채팅</title>
</head>
<body>
    <h2>WebSocket 1:1 채팅</h2>
    <label>내 ID:</label>
    <input type="text" id="userId">
    <button onclick="connect()">접속</button>
    <br><br>
    
    <label>받는 사람 ID:</label>
    <input type="text" id="receiverId">
    <br><br>

    <label>메시지:</label>
    <input type="text" id="message">
    <button onclick="sendMessage()">보내기</button>

    <h3>채팅 기록</h3>
    <div id="chatLog"></div>

    <script>
        let socket;

        function connect() {
            const userId = document.getElementById("userId").value;
            socket = new WebSocket("ws://localhost:8080/chat?userId=" + userId);

            socket.onopen = function () {
                console.log("WebSocket 연결됨!");
                document.getElementById("chatLog").innerHTML += "<p>서버와 연결됨</p>";
            };

            socket.onmessage = function (event) {
                document.getElementById("chatLog").innerHTML += "<p><b>받은 메시지:</b> " + event.data + "</p>";
            };

            socket.onclose = function () {
                console.log("WebSocket 연결 종료됨");
                document.getElementById("chatLog").innerHTML += "<p>서버와 연결 종료됨</p>";
            };
        }

        function sendMessage() {
            const receiverId = document.getElementById("receiverId").value;
            const message = document.getElementById("message").value;
            
            if (socket && socket.readyState === WebSocket.OPEN) {
                socket.send(receiverId + ":" + message);
                document.getElementById("chatLog").innerHTML += "<p><b>보낸 메시지:</b> " + message + "</p>";
            } else {
                alert("WebSocket 연결이 닫혀 있습니다.");
            }
        }
    </script>
</body>
</html>
