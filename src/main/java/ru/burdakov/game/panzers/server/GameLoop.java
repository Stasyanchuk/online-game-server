package ru.burdakov.game.panzers.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import ru.burdakov.game.panzers.server.actors.Panzer;
import ru.burdakov.game.panzers.server.ws.WebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

@Component
public class GameLoop extends ApplicationAdapter {

    private static final float frameRate = 1 / 60f;
    private final WebSocketHandler socketHandler;
    private final Array<String> events = new Array<>();
    private final Json json;
    private float lastRender = 0;

    private final ObjectMap<String, Panzer> panzers = new ObjectMap<>();
    private final Array<Panzer> stateToSend = new Array<>();

    private final ForkJoinPool pool = ForkJoinPool.commonPool();

    public GameLoop(WebSocketHandler socketHandler, Json json) {
        this.socketHandler = socketHandler;
        this.json = json;
    }

    @Override
    public void create() {
        socketHandler.setConnectListener(session -> {
            Panzer panzer = new Panzer();
            panzer.setId(session.getId());
            panzers.put(session.getId(), panzer);

            try {
                session.getNativeSession().getBasicRemote().sendText(String.format("{\"class\":\"sessionKey\",\"id\":\"%s\"}", session.getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        socketHandler.setDisconnectListener(session -> {
            sendToEveryBody(
                    String.format("{\"class\":\"evict\",\"id\":\"%s\"}", session.getId())
            );
            panzers.remove(session.getId());
        });

        socketHandler.setMessageListener(((session, message) -> {
            pool.execute(() -> {
                String type = message.get("type").asText();
                switch (type) {
                    case "state":
                        Panzer panzer = panzers.get(session.getId());
                        panzer.setLeftPressed(message.get("leftPressed").asBoolean());
                        panzer.setRightPressed(message.get("rightPressed").asBoolean());
                        panzer.setUpPressed(message.get("upPressed").asBoolean());
                        panzer.setDownPressed(message.get("downPressed").asBoolean());
                        panzer.setAngle((float) message.get("angle").asDouble());
                        break;
                    default:
                        throw new RuntimeException("Unknown WS object type: " + type);
                }
            });
        }));
    }

    @Override
    public void render() {
        lastRender += Gdx.graphics.getDeltaTime();
        if (lastRender >= frameRate) {
            for (ObjectMap.Entry<String, Panzer> panzerEntry : panzers) {
                Panzer panzer = panzerEntry.value;
                panzer.act(lastRender);
                stateToSend.add(panzer);
            }
            lastRender = 0;

            String stateJson = json.toJson(stateToSend);
            sendToEveryBody(stateJson);
            //fixme возможно не надо
            stateToSend.clear();
        }
    }

    private void sendToEveryBody(String message){
        pool.execute(() -> {

            for (StandardWebSocketSession session : socketHandler.getSessions()) {
                try {
                    if (session.isOpen())
                        session.getNativeSession().getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
