package ru.burdakov.game.panzers.server.ws;

import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

public interface DisconnectListener {

    void handle(StandardWebSocketSession session);

}
