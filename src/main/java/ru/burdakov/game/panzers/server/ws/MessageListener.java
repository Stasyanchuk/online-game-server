package ru.burdakov.game.panzers.server.ws;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;

public interface MessageListener {

    void handleMessage(StandardWebSocketSession session, JsonNode message);

}
