package ru.burdakov.game.panzers.server.ws;

import org.springframework.web.socket.WebSocketSession;

public interface MessageListener {

    void handleMessage(WebSocketSession session, String message);

}
