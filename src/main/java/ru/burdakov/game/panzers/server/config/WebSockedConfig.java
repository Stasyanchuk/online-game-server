package ru.burdakov.game.panzers.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.burdakov.game.panzers.server.ws.WebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSockedConfig implements WebSocketConfigurer {

    private final WebSocketHandler webSocketHandler;

    public WebSockedConfig(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws").setAllowedOrigins("*");
    }
}
